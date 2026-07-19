let allMenus = [];

function position(trigger, menu) {
    menu.classList.remove('flip');
    const menuRect = menu.getBoundingClientRect();
    const triggerRect = trigger.getBoundingClientRect();
    if (triggerRect.bottom + menuRect.height > window.innerHeight) {
        menu.classList.add('flip');
    }
}

export function closeAll(exceptMenu = null) {
    allMenus.forEach(m => {
        if (m !== exceptMenu) m.classList.remove('open');
    });
}

export function initContextMenus(container = document) {
    allMenus = Array.from(container.querySelectorAll('.context-menu'));

    container.addEventListener('click', e => {
        const trigger = e.target.closest('.context-trigger');
        if (trigger) {
            e.stopPropagation();
            const targetId = trigger.dataset.target;
            const menu = document.getElementById(targetId);
            if (!menu) return;
            if (menu.classList.contains('open')) {
                menu.classList.remove('open');
            } else {
                closeAll(menu);
                menu.classList.add('open');
                position(trigger, menu);
            }
            return;
        }

        // Закрываем все меню при клике вне
        if (!e.target.closest('.context-menu') && !e.target.closest('.context-trigger')) {
            closeAll();
        }
    });

    window.addEventListener('resize', () => closeAll());
}