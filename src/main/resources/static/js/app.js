import { $ } from './utils.js';
import { initSidebar } from './sidebar.js';
import { initContextMenus } from './contextMenu.js';
import { initModals } from './modal.js';
import { initEditChatTitle } from './chatTitle.js';
import { initMessages } from './messages.js';
import { initStreaming } from './streaming.js';
import { initArchive } from './archive.js';

document.addEventListener('DOMContentLoaded', () => {
    const overlay = $('#overlay');

    const onOverlayClick = () => {
        const { closeAll: closeCtx } = require('./contextMenu.js');
        const { closeAllModals } = require('./modal.js');
        closeCtx();
        closeAllModals({ overlay });
    };

    initSidebar({
        toggleBtn: $('#sidebarToggle'),
        overlay,
        onOverlayClick
    });

    initContextMenus();

    initModals({
        triggers: {
            newChatModal: $('#newChatBtn'),
            languageModal: $('#languageBtn')
        },
        overlay
    });

    initEditChatTitle({ overlay });

    initMessages();

    initStreaming({
        messagesContainer: $('#chatMessages'),
        input: $('#chatInput'),
        sendBtn: $('#sendBtn'),
        chatIdMeta: $('#chatId')
    });

    initArchive({
        triggerBtn: $('#archivedChatsBtn'),
        overlay
    });
});