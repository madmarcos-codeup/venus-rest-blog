//   <button type="button" class="float-end btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
export function getNotificationElement() {
    return `
        <div id="notification-hanger">
        </div>`;
}

export function showNotification(messageText, messageType) {
    // TODO: change display of message based on message type
    const notifyHanger = document.querySelector("#notification-hanger");
    const notifyBox = document.createElement("div");
    notifyBox.className = '';
    notifyBox.classList.add("alert");
    notifyBox.classList.add("alert-" + messageType);
    notifyBox.classList.add("alert-dismissible");
    notifyBox.classList.add("alert");
    notifyBox.classList.add("fade");
    notifyBox.classList.add("show");
    notifyBox.innerHTML = `
    <span>${messageText}</span>
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  </button>`;
    notifyHanger.appendChild(notifyBox);
}