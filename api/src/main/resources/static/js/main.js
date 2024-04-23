'use strict';

// Selecting elements from the DOM
const signInPage = document.querySelector('#signIn-page');
const signUpPage = document.querySelector('#signUp-page');
const chatPage = document.querySelector('#chat-page');
const signInForm = document.querySelector('#signInForm');
const signUpForm = document.querySelector('#signUpForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

// Initializing variables
let stompClient = null;
let nickname = null;
let fullname = null;
let selectedUserId = null;
let password = null;
let signUpNickname = null;
let signUpPassword = null;
let signUpFullName = null;

// Function to establish connection after signing in
function connect(event) {
    // Extracting values from sign-in form
    nickname = document.querySelector('#signInNickname').value.trim();
    password = document.querySelector('#signInPassword').value.trim();

    // Checking if nickname and password are not empty
    if (nickname && password) {
        // Hiding sign-in page and showing chat page
        signInPage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        // Establishing WebSocket connection
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        // Connecting to WebSocket server
        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault(); // Preventing default form submission behavior
}

// Function to sign up a new user
function signUp(event) {
    // Extracting values from sign-up form
    signUpNickname = document.querySelector('#signUpNickname').value.trim();
    signUpPassword = document.querySelector('#signUpPassword').value.trim();
    signUpFullName = document.querySelector('#signUpFullName').value.trim();

    // Checking if all sign-up fields are filled
    if (signUpNickname && signUpPassword && signUpFullName) {
        // Establishing WebSocket connection
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        // Connecting to WebSocket server to add new user
        stompClient.connect({}, addUser, onError);
    }
    event.preventDefault(); // Preventing default form submission behavior
}

// Function to add a new user after signing up
function addUser() {
    // Subscribing to user's private queue and public queue
    stompClient.subscribe(`/user/${signUpNickname}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);

    // Registering the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({nickName: signUpNickname, fullName: signUpFullName, status: 'ONLINE', password: signUpPassword})
    );

    // Displaying toast message for successful user addition
    showToast('User Added Successfully !!!');

    // Hiding sign-up page and showing sign-in page after 2 seconds
    setTimeout(() => {
        signUpPage.classList.add('hidden');
        signInPage.classList.remove('hidden');
    }, 2000);
}

function onConnected() {
    // Make a REST API call to register the connected user
    const basicAuthHeader = 'Basic ' + btoa(nickname + ':' + password);
    fetch("/user/signin", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ token: basicAuthHeader })
    })
    .then(response => {
        if (!response.ok) {
            chatPagex.classList.add('hidden');
            signInPage.classList.remove('hidden');
            showToast('UnAuthorized !!!');
        }else{
            stompClient.subscribe(`/user/${nickname}/queue/messages`, onMessageReceived);
            stompClient.subscribe(`/user/login/public`, onMessageReceived);
            // Sending login request to server
            stompClient.send("/app/user.login",
                {},
                JSON.stringify({token: basicAuthHeader})
            );
            // Once the user is registered successfully, update UI and subscribe to STOMP destinations
            document.querySelector('#connected-user-fullname').textContent = fullname;
            return findAndDisplayConnectedUsers(); // Assuming findAndDisplayConnectedUsers returns a promise
        }
    })

    .catch(error => {
         alert(error);
    });
}

// Function to fetch and display connected users
async function findAndDisplayConnectedUsers() {
    // Fetching connected users from server
    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();

    // Filtering out current user from the list
    connectedUsers = connectedUsers.filter(user => user.nickName !== nickname);

    // Clearing connected users list
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    // Appending each connected user to the list
    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);

        // Adding separator after each user except the last one
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

// Function to append user element to connected users list
function appendUserElement(user, connectedUsersList) {
    // Creating list item for user
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.nickName;

    // Creating user image element
    const userImage = document.createElement('img');
    userImage.src = '../img/user_icon.png';
    userImage.alt = user.fullName;

    // Creating span element for username
    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.fullName;

    // Creating span element for received messages count
    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    // Appending elements to list item
    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    // Adding click event listener to list item
    listItem.addEventListener('click', userItemClick);

    // Appending list item to connected users list
    connectedUsersList.appendChild(listItem);
}

// Function to handle click event on user item
function userItemClick(event) {
    // Removing 'active' class from all user items
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });

    // Removing 'hidden' class from message form
    messageForm.classList.remove('hidden');

    // Adding 'active' class to clicked user item
    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    // Extracting selected user's ID
    selectedUserId = clickedUser.getAttribute('id');

    // Fetching and displaying chat history with selected user
    fetchAndDisplayUserChat().then();

    // Hiding message notification for selected user
    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';
}

// Function to display message in chat area
function displayMessage(senderId, content) {
    // Creating message container element
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');

    // Adding appropriate class based on sender ID
    if (senderId === nickname) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }

    // Creating message element
    const message = document.createElement('p');
    message.textContent = content;

    // Appending message element to message container
    messageContainer.appendChild(message);

    // Appending message container to chat area
    chatArea.appendChild(messageContainer);
}

// Function to fetch and display chat history with selected user
async function fetchAndDisplayUserChat() {
    // Fetching chat history with selected user from server
    const userChatResponse = await fetch(`/messages?senderId=${nickname}&recipientId=${selectedUserId}`);
    const userChat = await userChatResponse.json();

    // Clearing chat area
    chatArea.innerHTML = '';

    // Displaying chat messages in chat area
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });

    // Scrolling chat area to bottom
    chatArea.scrollTop = chatArea.scrollHeight;
}

// Function to handle WebSocket connection error
function onError() {
    // Displaying error message
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// Function to send message to selected user
function sendMessage(event) {
    // Extracting message content from input field
    const messageContent = messageInput.value.trim();

    // Checking if message content is not empty and WebSocket client is connected
    if (messageContent && stompClient) {
        // Creating chat message object
        const chatMessage = {
            senderId: nickname,
            recipientId: selectedUserId,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };

        // Sending chat message to server
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));

        // Displaying sent message in chat area
        displayMessage(nickname, messageInput.value.trim());

        // Clearing message input field
        messageInput.value = '';
    }

    // Scrolling chat area to bottom
    chatArea.scrollTop = chatArea.scrollHeight;

    event.preventDefault(); // Preventing default form submission behavior
}

// Function to handle message received event
async function onMessageReceived(payload) {
    // Fetching and displaying connected users
    await findAndDisplayConnectedUsers();

    // Logging received message payload
    console.log('Message received', payload);

    // Parsing received message payload
    const message = JSON.parse(payload.body);

    // Checking if received message is from selected user
    if (selectedUserId && selectedUserId === message.senderId) {
        // Displaying received message in chat area
        displayMessage(message.senderId, message.content);

        // Scrolling chat area to bottom
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    // Adding 'active' class to selected user item in connected users list
    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        // Hiding message form if no user is selected
        messageForm.classList.add('hidden');
    }

    // Checking if received message is from a user who is not currently active
    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        // Displaying message notification for the user
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

// Function to handle user logout
function onLogout() {
    // Sending disconnect user request to server
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'OFFLINE'})
    );

    // Reloading the page
    window.location.reload();
}

// Function to open sign-up page
function openSignUpPage() {
    // Hiding sign-in page and showing sign-up page
    signInPage.classList.add('hidden');
    signUpPage.classList.remove('hidden');
}

// Function to display toast message
function showToast(message) {
    // Creating toast container element
    const toastContainer = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.classList.add('toast');
    toast.textContent = message;

    // Appending toast element to toast container
    toastContainer.appendChild(toast);

    // Showing toast for 2 seconds
    setTimeout(() => {
        toast.classList.add('show');
        setTimeout(() => {
            toast.classList.remove('show');
            setTimeout(() => {
                toast.remove();
            }, 300);
        }, 2000);
    }, 100);
}

// Adding event listeners to form submissions and logout button
signInForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
signUpForm.addEventListener('submit', signUp, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();
