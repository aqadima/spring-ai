export function $(selector, context = document) {
    return context.querySelector(selector);
}

export function renderMarkdownToElement(el, text) {
    if (typeof marked === 'undefined' || typeof DOMPurify === 'undefined') {
        console.warn('Marked or DOMPurify not loaded');
        return text;
    }
    el.innerHTML = DOMPurify.sanitize(marked.parse(text));
}

export function escapeHtml(text) {
    const map = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' };
    return text.replace(/[&<>"']/g, m => map[m]);
}