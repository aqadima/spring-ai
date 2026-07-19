import {renderMarkdownToElement} from './utils.js';

// -- Сворачивание сообщений --
function createToggleButton(target) {
    const btn = document.createElement('button');
    btn.className = 'toggle-collapse-btn';
    btn.textContent = 'Show more';
    btn.addEventListener('click', e => {
        e.stopPropagation();
        target.classList.toggle('collapsed');
        btn.textContent = target.classList.contains('collapsed') ? 'Show more' : 'Show less';
    });
    return btn;
}

function applyCollapsibleToElement(el) {
    if (el.dataset.collapsibleProcessed) return;
    el.dataset.collapsibleProcessed = 'true';
    const lineHeight = parseFloat(getComputedStyle(el).lineHeight);
    if (isNaN(lineHeight)) return;
    if (el.scrollHeight > lineHeight * 10 + 2) {
        el.classList.add('collapsed');
        el.parentNode.appendChild(createToggleButton(el));
    }
}

function initCollapsibleMessages(container = document) {
    container.querySelectorAll('.message.user .rendered-markdown').forEach(applyCollapsibleToElement);
}

// -- Копирование --
function initCopyFeature() {
    document.addEventListener('click', e => {
        const btn = e.target.closest('.copy-btn');
        if (!btn) return;
        e.stopPropagation();
        const rendered = btn.closest('.message-body')?.querySelector('.rendered-markdown');
        if (rendered) {
            navigator.clipboard.writeText(rendered.innerText).then(() => {
                btn.textContent = '✓';
                setTimeout(() => { btn.textContent = '📋'; }, 1000);
            }).catch(err => console.error('Copy failed', err));
        }
    });
}

function initEditMessage() {
    document.addEventListener('click', e => {
        const editBtn = e.target.closest('.edit-btn');
        if (!editBtn) return;
        e.stopPropagation();
        const messageBody = editBtn.closest('.message-body');
        const contentDiv = messageBody?.querySelector('.message-content');
        const rendered = contentDiv?.querySelector('.rendered-markdown');
        if (!rendered || contentDiv.querySelector('.edit-textarea')) return;

        const original = rendered.innerText;
        contentDiv.innerHTML = '';
        const textarea = Object.assign(document.createElement('textarea'), {
            className: 'edit-textarea',
            value: original,
            rows: Math.min(10, original.split('\n').length),
            style: 'width:100%'
        });
        const actions = document.createElement('div');
        actions.className = 'edit-actions';
        const saveBtn = document.createElement('button');
        saveBtn.textContent = 'Save';
        const cancelBtn = document.createElement('button');
        cancelBtn.textContent = 'Cancel';
        actions.append(saveBtn, cancelBtn);
        contentDiv.append(textarea, actions);
        textarea.focus();

        const finishEdit = (newContent) => {
            contentDiv.innerHTML = '';
            const newRendered = document.createElement('div');
            newRendered.className = 'rendered-markdown';
            renderMarkdownToElement(newRendered, newContent || original);
            contentDiv.appendChild(newRendered);
            applyCollapsibleToElement(newRendered);
        };

        cancelBtn.addEventListener('click', () => finishEdit());
        saveBtn.addEventListener('click', () => {
            const newContent = textarea.value.trim();
            if (newContent && newContent !== original) {
                const messageId = messageBody.closest('.message')?.dataset.messageId;
                if (messageId) {
                    fetch(`/chat/message/${messageId}`, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ content: newContent })
                    }).catch(err => console.error('Update failed', err));
                }
            }
            finishEdit(newContent);
        });
    });
}

// -- Первичный рендеринг markdown из скрытых скриптов --
function renderInitialMarkdown() {
    document.querySelectorAll('script.raw-markdown').forEach(script => {
        const target = script.nextElementSibling;
        if (target?.classList.contains('rendered-markdown')) {
            renderMarkdownToElement(target, script.textContent);
        }
        script.remove();
    });
}

export function initMessages() {
    renderInitialMarkdown();
    initCollapsibleMessages();
    initCopyFeature();
    initEditMessage();
}