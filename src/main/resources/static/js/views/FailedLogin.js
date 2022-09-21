import createView from "../createView.js";

export default function FailedLogin(props) {
    return `<h1>Login failed<h1><p>Please check your credentials and try again.</p>`;
}

export function FailedLoginEvent() {
    window.setTimeout(function() {
        createView("/login");
    }, 3000)
}