import createView from "../createView.js";

export default function Logout(props) {
    return `<div id="logout-container">

</div>`;
}

export function LogoutEvent() {
    window.localStorage.removeItem("access_token");
    window.localStorage.removeItem("refresh_token");
    window.localStorage.removeItem("user");
    createView("/");
}
