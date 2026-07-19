import { openModal, closeModal } from './modal.js';
import { closeAll as closeContextMenus } from './contextMenu.js';

export function initEditChatTitle({ overlay }) {
    let currentChatId = null;

    document.addEventListener('click', e => {
        const editBtn = e.target.closest('.edit-chat-btn');
        if (!editBtn) return;
        e.stopPropagation();
        closeContextMenus();

        currentChatId = editBtn.dataset.chatId;
        const title = editBtn.dataset.chatTitle;
        const input = document.getElementById('editChatTitle');
        if (input) input.value = title;

        openModal('editChatModal', { overlay });
    });

    const form = document.getElementById('editChatForm');
    if (!form) return;
    form.addEventListener('submit', async e => {
        e.preventDefault();
        const newTitle = document.getElementById('editChatTitle')?.value.trim();
        if (!newTitle || !currentChatId) {
            alert('Title cannot be empty');
            return;
        }
        try {
            const res = await fetch(`/api/chat/${currentChatId}/title?title=${encodeURIComponent(newTitle)}`, { method: 'PATCH' });
            if (!res.ok) throw new Error(`Server error: ${res.status}`);

            // Обновляем заголовок в основном интерфейсе
            const chatTitleMain = document.querySelector('.chat-title-main');
            const chatIdMeta = document.getElementById('chatId');
            if (chatIdMeta && chatIdMeta.content === currentChatId && chatTitleMain) {
                chatTitleMain.textContent = newTitle;
            }
            // Обновляем в боковой панели
            const li = document.querySelector(`li[data-chat-id="${currentChatId}"]`);
            if (li) {
                const titleSpan = li.querySelector('.chat-title');
                if (titleSpan) titleSpan.textContent = newTitle;
            }
            closeModal(document.getElementById('editChatModal'), { overlay });
        } catch (error) {
            console.error(error);
            alert('Error updating chat title.');
        }
    });
}