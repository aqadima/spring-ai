export function initSidebar({ toggleBtn, overlay, onOverlayClick }) {
    if (!toggleBtn || !overlay) return;
    toggleBtn.addEventListener('click', () => {
        document.body.classList.toggle('sidebar-open');
    });
    overlay.addEventListener('click', () => {
        document.body.classList.remove('sidebar-open');
        onOverlayClick();
    });
}