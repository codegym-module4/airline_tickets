// src/main/resources/static/user/js/firebase-auth.js

// 1) Cấu hình từ Firebase console (copy lại config mình đang dùng trên web)
const firebaseConfig = {
    apiKey: "AIzaSyAuEH7f5xJFMKDN-GXIYr_kqc7UfeNfqqs",
    authDomain: "airline-tickets-52c6e.firebaseapp.com",
    projectId: "airline-tickets-52c6e",
    storageBucket: "airline-tickets-52c6e.firebasestorage.app",
    messagingSenderId: "580653878855",
    appId: "1:580653878855:web:ba26e8d29735757b022b62",
    measurementId: "G-B6TT0D6DM1"
};
// 2) Khởi tạo
firebase.initializeApp(firebaseConfig);

// 3) Hàm gọi Google Sign-In và gửi token về server
async function signInWithGoogle() {
    const provider = new firebase.auth.GoogleAuthProvider();
    try {
        const result = await firebase.auth().signInWithPopup(provider);
        const idToken = await result.user.getIdToken();

        // Tạo form POST
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/login/google';

        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'idToken';
        input.value = idToken;
        form.appendChild(input);

        document.body.appendChild(form);
        form.submit();

    } catch (err) {
        console.error(err);
        alert('Không thể đăng nhập Google');
    }
}


// Gán sự kiện cho nút
document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('googleSignIn');
    if (btn) btn.onclick = signInWithGoogle;
});
