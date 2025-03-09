let currentUserEmail = "";

function loadCurrentUser() {
    fetch("/user/current", { credentials: "same-origin" })
        .then(response => response.json())
        .then(user => {
        currentUserId = user.id;
            currentUserEmail = user.email;
            const userInfoDiv = document.getElementById("user-info");
            if (userInfoDiv) {
                userInfoDiv.innerHTML = `
                    <img src="${user.profileImage ? user.profileImage : '/css/default-avatar.png'}" alt="Mi Foto" width="50">
                    <span>${user.username}</span>
                    <button id="changePhotoBtn">Cambiar foto</button>
                `;
                document.getElementById("changePhotoBtn").addEventListener("click", function() {
                    const newFileInput = document.createElement("input");
                    newFileInput.type = "file";
                    newFileInput.accept = "image/*";
                    newFileInput.onchange = function(e) {
                        const file = e.target.files[0];
                        const reader = new FileReader();
                        reader.onload = function(event) {
                            const newProfileImage = event.target.result;
                            updateProfileImage(newProfileImage);
                        };
                        reader.readAsDataURL(file);
                    };
                    newFileInput.click();
                });
            }
        })
        .catch(err => console.error(err));
}

function updateProfileImage(newProfileImage) {
    fetch("/user/updateProfileImage", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "same-origin",
        body: JSON.stringify({ profileImage: newProfileImage })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al actualizar la imagen");
        }
        return response.text();
    })
    .then(message => {
        alert(message);
        loadCurrentUser();
    })
    .catch(err => {
        console.error(err);
        alert(err.message);
    });
}

function loadMatches() {
    fetch("/matches/all", { credentials: "same-origin" })
        .then(response => response.json())
        .then(matches => {
            let container = document.getElementById("matches-list");
            if (!container) {
                container = document.createElement("div");
                container.id = "matches-list";
                container.style.position = "fixed";
                container.style.top = "60px";
                container.style.right = "10px";
                container.style.background = "#fff";
                container.style.border = "1px solid #ccc";
                container.style.padding = "10px";
                container.style.maxHeight = "300px";
                container.style.overflowY = "auto";
                document.body.appendChild(container);
            } else {
                // Toggle: si ya visible, ocultar y terminar
                if (container.style.display !== "none") {
                    container.style.display = "none";
                    return;
                } else {
                    container.style.display = "block";
                }
            }
            container.innerHTML = "<h4>Matches</h4>";
            matches.forEach(match => {
                let otherUser = (match.user1.email === currentUserEmail) ? match.user2 : match.user1;
                const matchItem = document.createElement("div");
                matchItem.classList.add("match-item");
                matchItem.style.cursor = "pointer";
                matchItem.style.marginBottom = "5px";
                matchItem.innerHTML = `
                    <img src="${otherUser.profileImage ? otherUser.profileImage : '/css/default-avatar.png'}" alt="Match" width="40">
                    <span>${otherUser.username}</span>
                `;
                matchItem.addEventListener("click", function() {
                    window.location.href = `/conversation.html?receiverId=${otherUser.id}`;
                });
                container.appendChild(matchItem);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Error al cargar los matches.");
        });
}

function loadConversation() {
    const params = new URLSearchParams(window.location.search);
    const receiverId = params.get("receiverId");
    if (!receiverId) {
        document.getElementById("chatContainer").innerHTML = "<p>No se especificó un receptor.</p>";
        return;
    }
    fetch(`/messages/conversation/${receiverId}`, { credentials: "same-origin" })
        .then(response => response.json())
        .then(messages => {
            const chatContainer = document.getElementById("chatContainer");
            chatContainer.innerHTML = "";
            messages.forEach(msg => {
                const msgDiv = document.createElement("div");
                                msgDiv.classList.add("message");
                                // Asignar clase según el remitente
                                if (msg.senderId === currentUserId) {
                                    msgDiv.classList.add("current");
                                } else {
                                    msgDiv.classList.add("other");
                                }
                                msgDiv.textContent = msg.content;
                                chatContainer.appendChild(msgDiv);
            });
        })
        .catch(err => {
            console.error(err);
            document.getElementById("chatContainer").innerHTML = "<p>Error al cargar la conversación.</p>";
        });
}

document.addEventListener("DOMContentLoaded", function() {

    // Cargar info del usuario actual (header)
    loadCurrentUser();

    // Botón de matches (toggle)
    const matchesBtn = document.getElementById("matchesBtn");
    if (matchesBtn) {
        matchesBtn.addEventListener("click", function() {
            let container = document.getElementById("matches-list");
            if (container && container.style.display !== "none") {
                container.style.display = "none";
            } else {
                loadMatches();
            }
        });
    }

    // -----------------------------
    // Registro de usuario (con imagen en base64)
    // -----------------------------
    const registerForm = document.getElementById("registerForm");
    if (registerForm) {
        registerForm.addEventListener("submit", function(e) {
            e.preventDefault();
            const username = document.getElementById("username").value;
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;
            const fileInput = document.getElementById("profileFile");

            const sendRegisterRequest = (profileImage) => {
                const payload = { username, email, password, profileImage };
                fetch("/user/register", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload)
                })
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(text => { throw new Error(text); });
                    }
                    return response.json();
                })
                .then(data => {
                    alert("Usuario registrado correctamente");
                    window.location.href = "/login.html";
                })
                .catch(err => {
                    console.error(err);
                    alert("Error al registrar el usuario: " + err.message);
                });
            };

            if (fileInput && fileInput.files && fileInput.files[0]) {
                const file = fileInput.files[0];
                const reader = new FileReader();
                reader.onload = function(event) {
                    const profileImage = event.target.result;
                    sendRegisterRequest(profileImage);
                };
                reader.onerror = function(error) {
                    console.error("Error al leer el archivo:", error);
                    alert("Error al procesar la imagen de perfil");
                };
                reader.readAsDataURL(file);
            } else {
                sendRegisterRequest(null);
            }
        });
    }

    // -----------------------------
    // Inicio de sesión (login)
    // -----------------------------
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", function(e) {
            e.preventDefault();
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;

            fetch("/user/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Credenciales inválidas");
                }
                return response.json();
            })
            .then(data => {
                window.location.href = "/profiles.html";
            })
            .catch(err => {
                console.error(err);
                alert("Credenciales inválidas");
            });
        });
    }

    // -----------------------------
    // Cargar el siguiente perfil para interacción (like/dislike)
    // -----------------------------
    const profileContainer = document.getElementById("profile-container");
    if (profileContainer) {
        fetch("/profiles/next", { credentials: "same-origin" })
            .then(response => response.json())
            .then(profile => {
                if (profile.message) {
                    profileContainer.innerHTML = `<p>${profile.message}</p>`;
                    return;
                }
                if (!profile || !profile.username) {
                    profileContainer.innerHTML = "<p>No hay más perfiles disponibles.</p>";
                    return;
                }
                profileContainer.innerHTML = `
                    <h3>${profile.username}</h3>
                    ${
                        profile.profileImage
                        ? `<img src="${profile.profileImage}" alt="Imagen de perfil" width="200">`
                        : `<p>Sin foto de perfil</p>`
                    }
                `;
                profileContainer.dataset.userid = profile.id;
            })
            .catch(err => {
                console.error(err);
                profileContainer.innerHTML = "<p>Error al cargar el perfil.</p>";
            });
    }

    // -----------------------------
    // Botones de Like y Dislike
    // -----------------------------
    const likeBtn = document.getElementById("likeBtn");
    const dislikeBtn = document.getElementById("dislikeBtn");

    if (likeBtn) {
        likeBtn.addEventListener("click", function() {
            const profileId = document.getElementById("profile-container").dataset.userid;
            fetch(`/interaction/like/${profileId}`, { method: "POST", credentials: "same-origin" })
                .then(response => response.text())
                .then(message => {
                    // Mostrar alerta solo si se produjo un match
                    if (message.includes("Match")) {
                        alert(message);
                    }
                    window.location.reload();
                })
                .catch(err => {
                    console.error(err);
                });
        });
    }

    if (dislikeBtn) {
        dislikeBtn.addEventListener("click", function() {
            const profileId = document.getElementById("profile-container").dataset.userid;
            fetch(`/interaction/dislike/${profileId}`, { method: "POST", credentials: "same-origin" })
                .then(response => response.text())
                .then(message => {
                    window.location.reload();
                })
                .catch(err => {
                    console.error(err);
                });
        });
    }

    // -----------------------------
    // Cargar conversación en la página de chat (si existe)
    // -----------------------------
    const messageForm = document.getElementById("messageForm");
    if (messageForm) {
        // Cargar la conversación al iniciar la página de chat
        loadConversation();

        messageForm.addEventListener("submit", function(e) {
            e.preventDefault();
            const messageInput = document.getElementById("messageInput");
            const messageContent = messageInput.value;
            const receiverId = new URLSearchParams(window.location.search).get("receiverId");
            fetch("/messages/send", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: "same-origin",
                body: JSON.stringify({ receiverId, content: messageContent })
            })
            .then(response => response.json())
            .then(data => {
                messageInput.value = "";
                loadConversation(); // Recargar conversación
            })
            .catch(err => {
                console.error(err);
                alert("Error al enviar el mensaje");
            });
        });
    }
});

function loadConversation() {
    const params = new URLSearchParams(window.location.search);
    const receiverId = params.get("receiverId");
    if (!receiverId) {
        document.getElementById("chatContainer").innerHTML = "<p>No se especificó un receptor.</p>";
        return;
    }
    fetch(`/messages/conversation/${receiverId}`, { credentials: "same-origin" })
        .then(response => response.json())
        .then(messages => {
            const chatContainer = document.getElementById("chatContainer");
            chatContainer.innerHTML = "";
            messages.forEach(msg => {
                const msgDiv = document.createElement("div");
                msgDiv.className = "chat-message";
                // Puedes personalizar para mostrar remitente y fecha
                msgDiv.textContent = msg.content;
                chatContainer.appendChild(msgDiv);
            });
        })
        .catch(err => {
            console.error(err);
            document.getElementById("chatContainer").innerHTML = "<p>Error al cargar la conversación.</p>";
        });
}
