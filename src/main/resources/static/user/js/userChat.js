
let stompClient = null; // Đưa ra ngoài để tái sử dụng
let handleSendClick = null;
let handleEnterKeydown = null;

window.onload = function () {
    const chatToggle = document.getElementById('chat-toggle');
    const chatBox = document.getElementById('chat-box');
    const chatClose = document.getElementById('chat-close');
    const startChatButtonContainer = document.getElementById('start-chat-button-container');
    const startChatButton = document.getElementById('start-chat-button');
    let sendingName = '';

    chatToggle.addEventListener('click', () => {
        chatBox.style.display = (chatBox.style.display === 'none' || chatBox.style.display === '') ? 'flex' : 'none';
    });

    chatClose.addEventListener('click', () => {
        chatBox.style.display = 'none';
    });

    startChatButton.addEventListener('click', () => {
        startChatButtonContainer.style.display = 'none';
        startChat();
    });

    function startChat() {
        chatBox.style.display = 'flex';
        document.getElementById('chat-controls').style.display = 'block';

        const socket = new SockJS('/ws-chat');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);

            const sessionId = /\/([^\/]+)\/websocket/.exec(socket._transport.url)[1];
            stompClient.subscribe('/topic/errors/' + sessionId, function (message) {
                alert(message.body);
            });

            stompClient.subscribe('/user/queue/messages', function (message) {
                showMessage(JSON.parse(message.body));
            });

            stompClient.subscribe('/user/queue/history', function (message) {
                const data = JSON.parse(message.body);
                sendingName = data.sender;
                const history = data.history;
                if (Array.isArray(history)) {
                    history.forEach(msg => showMessage(msg));
                }
            });

            stompClient.subscribe('/user/queue/end', function (message) {
                const chatBody = document.getElementById('chat-messages');
                const endMessage = document.createElement('p');
                endMessage.className = 'mb-0 text-danger fw-bold';
                endMessage.innerHTML = message.body;
                chatBody.appendChild(endMessage);
                chatBody.scrollTop = chatBody.scrollHeight;

                document.getElementById('chat-controls').style.display = 'none';
                startChatButtonContainer.style.display = 'block';
                sendingName = '';


                const sendBtn = document.getElementById('send-button');
                const msgInput = document.getElementById('message-input');

                // Gỡ các listener khi kết thúc chat
                if (handleSendClick) {
                    sendBtn.removeEventListener('click', handleSendClick);
                }
                if (handleEnterKeydown) {
                    msgInput.removeEventListener('keydown', handleEnterKeydown);
                }

                stompClient.disconnect(() => {
                    console.log('Disconnected');
                    alert("Bạn đã kết thúc phiên hỗ trợ.");
                });
            });

            stompClient.send('/app/history', {}, JSON.stringify({}));
        });

        const messageInput = document.getElementById('message-input');
        const newSendButton = document.getElementById('send-button');

        // Gỡ nếu đã tồn tại trước đó
        if (handleSendClick) {
            newSendButton.removeEventListener('click', handleSendClick);
        }

        handleSendClick = function () {
            const message = messageInput.value;
            showSenderMessage();
            if (message && stompClient && stompClient.connected) {
                stompClient.send('/app/chat', {}, JSON.stringify({ 'content': message }));
                messageInput.value = '';
            }
        };
        newSendButton.addEventListener('click', handleSendClick);

        // Gỡ Enter listener cũ nếu có
        if (handleEnterKeydown) {
            messageInput.removeEventListener('keydown', handleEnterKeydown);
        }

        handleEnterKeydown = function (event) {
            if (event.key === 'Enter' && !event.shiftKey) {
                const message = messageInput.value;
                event.preventDefault(); // Ngăn xuống dòng
                showSenderMessage();
                if (message && stompClient && stompClient.connected) {
                    stompClient.send('/app/chat', {}, JSON.stringify({ 'content': message }));
                    messageInput.value = '';
                }
            }
        };
        messageInput.addEventListener('keydown', handleEnterKeydown);

        function showMessage(displayMessage) {
            const chatBody = document.getElementById('chat-messages');
            const messageElement = document.createElement('p');
            messageElement.className = 'mb-0';
            messageElement.innerHTML = `<strong>${displayMessage.senderName}</strong>: ${displayMessage.content}`;
            chatBody.appendChild(messageElement);
            chatBody.scrollTop = chatBody.scrollHeight;
        }

        function showSenderMessage() {
            const chatBody = document.getElementById('chat-messages');
            const messageElement = document.createElement('p');
            messageElement.className = 'mb-0';
            messageElement.innerHTML = `<strong>${sendingName}</strong>: ${messageInput.value}`;
            chatBody.appendChild(messageElement);
            chatBody.scrollTop = chatBody.scrollHeight;
        }
    }
};
