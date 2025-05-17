
let stompClient = null; // Đưa biến stompClient ra ngoài để dùng lại
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

            stompClient.subscribe('/user/queue/messages', function (message) {
                console.log('Received message:', message.body);
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

            stompClient.send('/app/history', {}, JSON.stringify({}));
        });

        const newSendButton = document.getElementById('send-button');
        const messageInput = document.getElementById('message-input');

        // Gỡ listener cũ nếu có
        if (handleSendClick) {
            newSendButton.removeEventListener('click', handleSendClick);
        }
        if (handleEnterKeydown) {
            messageInput.removeEventListener('keydown', handleEnterKeydown);
        }


        handleSendClick = function () {
            const message = messageInput.value;
            showSenderMessage();
            if (message && stompClient && stompClient.connected) {
                stompClient.send('/app/reply', {}, JSON.stringify({ 'content': message }));
                messageInput.value = '';
            }
        };
        newSendButton.addEventListener('click', handleSendClick);


        handleEnterKeydown = function (event) {
            if (event.key === 'Enter' && !event.shiftKey) {
                const message = messageInput.value;
                event.preventDefault(); // Ngăn xuống dòng
                showSenderMessage();
                if (message && stompClient && stompClient.connected) {
                    stompClient.send('/app/reply', {}, JSON.stringify({ 'content': message }));
                    messageInput.value = '';
                }
            }
        };
        messageInput.addEventListener('keydown', handleEnterKeydown);

        const endButton = document.getElementById('end-button');
        endButton.addEventListener('click', function () {
            stompClient.send('/app/end', {}, JSON.stringify({content: 'Hỗ trợ đã kết thúc.'}));
            alert('Bạn đã kết thúc phiên hỗ trợ.');
            chatBox.style.display = 'none';
            showEndMessage();
        });

        function showMessage(displayMessage) {
            const chatBody = document.getElementById('chat-messages');
            const messageElement = document.createElement('p');
            messageElement.className = 'mb-0';
            messageElement.innerHTML = `<strong>${displayMessage.senderName}</strong>: ${displayMessage.content}`;
            chatBody.appendChild(messageElement);
            chatBody.scrollTop = chatBody.scrollHeight;
        }

        function showEndMessage() {
            const chatBody = document.getElementById('chat-messages');
            const messageElement = document.createElement('p');
            messageElement.className = 'mb-0 text-danger fw-bold';
            messageElement.textContent = 'Hỗ trợ đã kết thúc.';
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
