document.addEventListener('DOMContentLoaded', function() {
    // ---------- Sidebar Toggle ----------
    const sidebarToggle = document.getElementById('sidebarToggle');
    const overlay = document.getElementById('overlay');
    const body = document.body;

    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', () => {
            body.classList.toggle('sidebar-open');
        });
    }

    overlay.addEventListener('click', () => {
        body.classList.remove('sidebar-open');
        closeAllContextMenus();
        closeAllModals();
    });

    // ---------- Context Menus ----------
    const contextTriggers = document.querySelectorAll('.context-trigger');
    contextTriggers.forEach(trigger => {
        trigger.addEventListener('click', (e) => {
            e.stopPropagation();
            const targetId = trigger.getAttribute('data-target');
            const menu = document.getElementById(targetId);
            if (!menu) return;
            // Close other menus first
            document.querySelectorAll('.context-menu.open').forEach(m => {
                if (m !== menu) m.classList.remove('open');
            });
            menu.classList.toggle('open');
            positionContextMenu(trigger, menu);
        });
    });

    // Close context menus when clicking outside
    document.addEventListener('click', (e) => {
        if (!e.target.closest('.context-menu') && !e.target.closest('.context-trigger')) {
            closeAllContextMenus();
        }
    });

    function closeAllContextMenus() {
        document.querySelectorAll('.context-menu.open').forEach(m => m.classList.remove('open'));
    }

    function positionContextMenu(trigger, menu) {
        // Simple positioning: use CSS already set, but adjust if off-screen
        // Not implemented fully for brevity; relies on CSS (position:absolute)
    }

    // ---------- Modals ----------
    const modalTriggers = {
        newChat: document.getElementById('newChatBtn'),
        language: document.getElementById('languageBtn')
    };
    const modals = {
        newChat: document.getElementById('newChatModal'),
        editChat: document.getElementById('editChatModal'),
        language: document.getElementById('languageModal')
    };

    // Open new chat modal
    if (modalTriggers.newChat && modals.newChat) {
        modalTriggers.newChat.addEventListener('click', () => openModal('newChat'));
    }
    // Open language modal
    if (modalTriggers.language && modals.language) {
        modalTriggers.language.addEventListener('click', (e) => {
            e.stopPropagation();
            closeAllContextMenus();
            openModal('language');
        });
    }
    // Edit chat modal from context menu
    document.querySelectorAll('.edit-chat-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            closeAllContextMenus();
            const chatId = btn.getAttribute('data-chat-id');
            const chatTitle = btn.getAttribute('data-chat-title');
            openEditModal(chatId, chatTitle);
        });
    });

    function openModal(modalName) {
        const modal = modals[modalName];
        if (modal) {
            modal.classList.add('open');
            overlay.style.display = 'block'; // ensure overlay visible
        }
    }

    function openEditModal(chatId, currentTitle) {
        const modal = modals.editChat;
        if (!modal) return;
        document.getElementById('editChatTitle').value = currentTitle;
        // Update form action placeholder – real endpoint required
        document.getElementById('editChatForm').action = `/chat/${chatId}/update-title`;
        modal.classList.add('open');
        overlay.style.display = 'block';
    }

    // Close buttons in modals
    document.querySelectorAll('.modal-close, .modal-close-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const modal = btn.closest('.modal');
            if (modal) closeModal(modal);
        });
    });

    // Close modal on overlay click
    overlay.addEventListener('click', () => {
        closeAllModals();
    });

    function closeAllModals() {
        Object.values(modals).forEach(m => m.classList.remove('open'));
        overlay.style.display = 'none';
    }
    function closeModal(modal) {
        modal.classList.remove('open');
        if (!document.querySelector('.modal.open')) {
            overlay.style.display = 'none';
        }
    }

    // ---------- Markdown Rendering ----------
    const rawMarkdowns = document.querySelectorAll('.raw-markdown');
    rawMarkdowns.forEach(raw => {
        const text = raw.textContent;
        // Render markdown to HTML and sanitize
        const html = marked.parse(text);
        const sanitized = DOMPurify.sanitize(html);
        const renderTarget = raw.nextElementSibling;
        if (renderTarget && renderTarget.classList.contains('rendered-markdown')) {
            renderTarget.innerHTML = sanitized;
        }
        raw.remove(); // clean up
    });

    // ---------- Collapsible User Messages ----------
    document.querySelectorAll('.message.user .rendered-markdown').forEach(el => {
        const lineHeight = parseFloat(getComputedStyle(el).lineHeight);
        const maxHeight = lineHeight * 10;
        if (el.scrollHeight > maxHeight + 2) { // small tolerance
            el.classList.add('collapsed');
            const btn = document.createElement('button');
            btn.className = 'toggle-collapse-btn';
            btn.textContent = 'Show more';
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                el.classList.toggle('collapsed');
                btn.textContent = el.classList.contains('collapsed') ? 'Show more' : 'Show less';
            });
            el.parentNode.appendChild(btn);
        }
    });

    // ---------- Copy Message ----------
    document.querySelectorAll('.copy-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            const messageDiv = btn.closest('.message-body');
            // Get raw markdown (already removed, so use rendered text)
            const rendered = messageDiv.querySelector('.rendered-markdown');
            if (rendered) {
                const text = rendered.innerText;
                navigator.clipboard.writeText(text).then(() => {
                    // Optional: show tooltip
                });
            }
        });
    });

    // ---------- Edit Message (user only) ----------
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            const messageBody = btn.closest('.message-body');
            const contentDiv = messageBody.querySelector('.message-content');
            const rendered = contentDiv.querySelector('.rendered-markdown');
            if (!rendered) return;

            // Switch to edit mode
            const originalHtml = rendered.innerHTML;
            const textarea = document.createElement('textarea');
            textarea.value = rendered.innerText;
            textarea.className = 'edit-textarea';
            textarea.style.width = '100%';
            textarea.rows = 6;

            const saveBtn = document.createElement('button');
            saveBtn.textContent = 'Save';
            saveBtn.className = 'btn-icon';
            const cancelBtn = document.createElement('button');
            cancelBtn.textContent = 'Cancel';
            cancelBtn.className = 'btn-icon';

            const actions = document.createElement('div');
            actions.className = 'edit-actions';
            actions.append(saveBtn, cancelBtn);

            // Replace content
            contentDiv.innerHTML = '';
            contentDiv.append(textarea, actions);

            cancelBtn.addEventListener('click', () => {
                contentDiv.innerHTML = '';
                const newRendered = document.createElement('div');
                newRendered.className = 'rendered-markdown';
                newRendered.innerHTML = originalHtml;
                contentDiv.appendChild(newRendered);
            });

            saveBtn.addEventListener('click', () => {
                // Placeholder: send update to server
                const newContent = textarea.value.trim();
                if (newContent && newContent !== rendered.innerText) {
                    // Here you would POST to update the message
                    console.log('Update message to:', newContent);
                }
                // Revert UI (show markdown)
                contentDiv.innerHTML = '';
                const newRendered = document.createElement('div');
                newRendered.className = 'rendered-markdown';
                newRendered.innerHTML = DOMPurify.sanitize(marked.parse(newContent));
                contentDiv.appendChild(newRendered);
            });
        });
    });

});