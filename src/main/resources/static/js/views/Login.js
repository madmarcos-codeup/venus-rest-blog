import {setTokens} from "../auth.js";
import createView from "../createView.js";

export default function Login(props) {
    return `<div id="login-container">
</div>`;
}

export function LoginEvent() {
    google.accounts.id.initialize({
        client_id: CLIENT_ID,
        callback: handleCredentialResponse
    });
    google.accounts.id.prompt();
}

function handleCredentialResponse(loginInfo) {
    const jwt = loginInfo.credential;
    console.log(jwt);
    setTokens(jwt);
    createView("/");
}