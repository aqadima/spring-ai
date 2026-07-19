import {openModal} from './modal.js';
import {closeAll as closeContextMenus, initContextMenus} from './contextMenu.js';
import {escapeHtml, renderMarkdownToElement} from './utils.js';

export function initArchive({ triggerBtn, overlay }) {
    if (!triggerBtn) return;

    const modalId = 'archiveModal';
    triggerBtn.addEventListener('click', e => {
        e.stopPropagation();
        closeContextMenus();
        openModal(modalId, { overlay });
        loadArchivedChats();
    });

    document.addEventListener('click', async e => {
        const unarchive = e.target.closest('.unarchive-btn');
        if (unarchive) {
            e.stopPropagation();
            const chatId = unarchive.dataset.chatId;
            try {
                if (await fetch(`/chat/${chatId}/unarchive`, { method: 'POST' }).then(r => r.ok)) {
                    removeArchiveItem(chatId);
                    alert('Chat restored. Refresh to see changes.');
                } else alert(`Failed to restore chat. Chat id: ${chatId}`);
            } catch { alert('Network error.'); }
            closeContextMenus();
            return;
        }
        const deleteBtn = e.target.closest('.delete-archive-btn');
        if (deleteBtn) {
            e.stopPropagation();
            const chatId = deleteBtn.dataset.chatId;
            if (!confirm('Permanently delete this chat?')) return;
            try {
                if (await fetch(`/chat/${chatId}`, { method: 'DELETE' }).then(r => r.ok)) {
                    removeArchiveItem(chatId);
                } else alert('Failed to delete.');
            } catch { alert('Network error.'); }
            closeContextMenus();
        }
    });

    // Перехват форм в сайдбаре (archive/delete)
    document.addEventListener('submit', async e => {
        const form = e.target;
        if (!form.closest('.context-menu') || !form.closest('#sidebar')) return;
        const action = form.action;
        const method = (form.querySelector('input[name="_method"]')?.value || 'post').toLowerCase();
        if (!action) return;
        e.preventDefault();
        try {
            const res = await fetch(action, { method });
            if (res.ok) {
                const item = form.closest('.chat-item');
                if (item) item.remove();
                const currentId = document.getElementById('chatId')?.content;
                if (currentId && action.includes(currentId)) window.location.href = '/';
            } else alert('Operation failed.');
        } catch { alert('Network error.'); }
    });
}

// Вспомогательные функции для внутреннего использования
let currentPreviewChatId = null;

async function loadArchivedChats() {
    const list = document.getElementById('archiveChatList');
    if (!list) return;
    list.innerHTML = '';
    const loading = document.getElementById('archiveLoading');
    if (loading) loading.style.display = 'block';
    try {
        const res = await fetch('/api/chat/archive');
        if (!res.ok) throw new Error('Failed');
        const chats = await res.json();
        if (loading) loading.style.display = 'none';
        if (!chats.length) {
            list.innerHTML = '<li class="archive-empty">No archived chats</li>';
            return;
        }
        chats.forEach(chat => {
            const li = document.createElement('li');
            li.className = 'archive-chat-item';
            li.dataset.chatId = chat.id;
            li.innerHTML = `
        <span class="chat-title">${escapeHtml(chat.title)}</span>
        <div class="context-wrapper">
          <button class="context-trigger" data-target="archiveMenu-${chat.id}">⋯</button>
          <div id="archiveMenu-${chat.id}" class="context-menu context-menu-bottom-right">
            <ul>
              <li><button class="menu-item unarchive-btn" data-chat-id="${chat.id}">Unarchive</button></li>
              <li><button class="menu-item menu-item-danger delete-archive-btn" data-chat-id="${chat.id}">Delete</button></li>
            </ul>
          </div>
        </div>`;
            li.addEventListener('click', ev => {
                if (ev.target.closest('.context-menu') || ev.target.closest('.context-trigger')) return;
                loadArchivePreview(chat.id);
                list.querySelectorAll('.archive-chat-item').forEach(item => item.classList.remove('active'));
                li.classList.add('active');
            });
            list.appendChild(li);
        });
        initContextMenus(list);
    } catch (err) {
        console.error(err);
        if (loading) loading.textContent = 'Error loading archived chats.';
    }
}

async function loadArchivePreview(chatId) {
    const preview = document.getElementById('archivePreview');
    if (!preview) return;
    currentPreviewChatId = chatId;
    preview.innerHTML = '<div class="archive-preview-loading">Loading...</div>';
    try {
        const res = await fetch(`/api/chat/${chatId}`);
        if (!res.ok) throw new Error('Failed');
        const data = await res.json();
        renderPreview(data);
    } catch {
        preview.innerHTML = '<div class="archive-preview-error">Error loading messages.</div>';
    }
}

function renderPreview(chatData) {
    const preview = document.getElementById('archivePreview');
    if (!preview) return;
    preview.innerHTML = '';
    if (!chatData.messages?.length) {
        preview.innerHTML = '<div class="archive-preview-empty">No messages</div>';
        return;
    }
    const frag = document.createDocumentFragment();
    chatData.messages.forEach(msg => {
        const div = document.createElement('div');
        div.className = `archive-message ${msg.role.toLowerCase()}`;
        const content = document.createElement('div');
        content.className = 'rendered-markdown';
        renderMarkdownToElement(content, msg.content);
        div.appendChild(content);
        frag.appendChild(div);
    });
    preview.appendChild(frag);
}

function removeArchiveItem(chatId) {
    document.querySelector(`.archive-chat-item[data-chat-id="${chatId}"]`)?.remove();
    if (currentPreviewChatId === chatId) {
        const preview = document.getElementById('archivePreview');
        if (preview) preview.innerHTML = '<div class="archive-preview-placeholder">Select a chat to preview</div>';
        currentPreviewChatId = null;
    }
}