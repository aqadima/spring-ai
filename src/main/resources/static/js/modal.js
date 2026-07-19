import { closeAll as closeContextMenus } from './contextMenu.js';

let overlay;

function anyOpen() {
    return document.querySelectorAll('.modal.open').length > 0;
}

export function openModal(modalId, { overlay: ov } = {}) {
    if (ov) overlay = ov;
    const modal = document.getElementById(modalId);
    if (!modal) return;
    closeContextMenus();
    modal.classList.add('open');
    if (overlay) overlay.style.display = 'block';
}

export function closeModal(modal, { overlay: ov } = {}) {
    if (ov) overlay = ov;
    modal.classList.remove('open');
    if (!anyOpen() && overlay) overlay.style.display = 'none';
}

export function closeAllModals({ overlay: ov } = {}) {
    if (ov) overlay = ov;
    document.querySelectorAll('.modal').forEach(m => m.classList.remove('open'));
    if (overlay) overlay.style.display = 'none';
}

export function initModals({ triggers, overlay: ov, closeButtonsSelector = '.modal-close, .modal-close-btn' } = {}) {
    if (ov) overlay = ov;
    // Кнопки закрытия
    document.addEventListener('click', e => {
        const closeBtn = e.target.closest(closeButtonsSelector);
        if (closeBtn) {
            const modal = closeBtn.closest('.modal');
            if (modal) closeModal(modal, { overlay });
        }
    });

    // Оверлей закрывает модалки
    if (overlay) {
        overlay.addEventListener('click', () => closeAllModals({ overlay }));
    }

    // Триггеры открытия
    if (triggers) {
        for (const [id, btn] of Object.entries(triggers)) {
            if (btn) {
                btn.addEventListener('click', e => {
                    e.stopPropagation();
                    openModal(id, { overlay });
                });
            }
        }
    }
}