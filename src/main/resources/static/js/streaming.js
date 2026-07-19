import { renderMarkdownToElement } from './utils.js';

export function initStreaming({ messagesContainer, input, sendBtn, chatIdMeta }) {
    if (!messagesContainer || !input || !sendBtn || !chatIdMeta) return;
    const chatId = chatIdMeta.content;
    if (!chatId) return;

    function createMessageElement(role, content = '') {
        const div = document.createElement('div');
        div.className = `message message-${role}`;
        div.innerHTML = `
      <div class="message-body">
        <div class="message-content">
          <div class="rendered-markdown"></div>
        </div>
        <div class="message-actions">
          <button class="btn-icon copy-btn" title="Copy">📋</button>
          ${role === 'user' ? '<button class="btn-icon edit-btn" title="Edit">✏️</button>' : ''}
        </div>
      </div>`;
        const md = div.querySelector('.rendered-markdown');
        if (content) renderMarkdownToElement(md, content);
        return div;
    }

    function scrollToBottom() {
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    function sendMessage() {
        const content = input.value.trim();
        if (!content) return;
        input.value = '';
        input.style.height = 'auto';

        messagesContainer.appendChild(createMessageElement('user', content));
        const assistantMsg = createMessageElement('assistant');
        messagesContainer.appendChild(assistantMsg);
        const assistantContent = assistantMsg.querySelector('.rendered-markdown');
        scrollToBottom();

        fetch(`/api/chat/${chatId}/message_stream`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Accept': 'text/event-stream' },
            body: JSON.stringify({ content })
        })
            .then(async response => {
                if (!response.ok) throw new Error(`Server error: ${response.status}`);
                const reader = response.body.getReader();
                const decoder = new TextDecoder();
                let buffer = '';
                while (true) {
                    const { done, value } = await reader.read();
                    if (done) {
                        const final = assistantContent.textContent;
                        if (final) renderMarkdownToElement(assistantContent, final);
                        scrollToBottom();
                        break;
                    }
                    buffer += decoder.decode(value, { stream: true });
                    const lines = buffer.split('\n');
                    buffer = lines.pop() || '';
                    for (const line of lines) {
                        if (!line.startsWith('data:')) continue;
                        const data = line.slice(5).trim();
                        if (data === '[DONE]' || data === '') continue;
                        try {
                            const parsed = JSON.parse(data);
                            if (parsed.text) {
                                assistantContent.textContent += parsed.text;
                                scrollToBottom();
                            }
                        } catch { console.warn('Non-JSON SSE:', data); }
                    }
                }
            })
            .catch(err => {
                console.error(err);
                assistantContent.textContent = 'Error: ' + err.message;
            });
    }

    sendBtn.addEventListener('click', sendMessage);
    input.addEventListener('keydown', e => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });
    input.addEventListener('input', function() {
        this.style.height = 'auto';
        this.style.height = this.scrollHeight + 'px';
    });
}