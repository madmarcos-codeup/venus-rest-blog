import createView from "../createView.js";

export default function DoLogin(props) {
    return `<div id="login-result">

</div>`;
}

export function DoLoginEvents() {
    const urlSearchParams = new URLSearchParams(window.location.hash);
    console.log(window.location);
    const params = Object.fromEntries(urlSearchParams.entries());
    let accessToken = params.access_token;
    // if(window.location.hash.length === 0) {
    if(!accessToken || accessToken.length === 0) {
        // createView("/logingoogle");
        return;
    }
    const div = document.querySelector("#login-result");
    div.innerText = accessToken;
}
