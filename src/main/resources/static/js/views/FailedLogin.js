import createView from "../createView.js";

export default function FailedLogin(props) {
    return `<h1>Login failed<h1>
<h6>Please check your credentials and try again.</h6>`;
}

export function FailedLoginEvent() {
    window.setTimeout(function() {
        createView("/login");
    }, 3000)
}