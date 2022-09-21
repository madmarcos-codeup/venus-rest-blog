import createView from "../createView.js";

export default function Logout(props) {
    return `<h1>Logging out...</h1>`;
}

export function LogoutEvent() {
    window.setTimeout(function() {
        window.localStorage.removeItem("access_token");
        window.localStorage.removeItem("refresh_token");
        createView("/login");
    }, 5)
}