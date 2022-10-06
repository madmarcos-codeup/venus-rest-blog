import {getUser, isLoggedIn} from "../../auth.js";

export default function Navbar(props) {
    let nav = `
        <nav>
            <a href="/" data-link class="my-link">Home</a>
            <a href="/posts" data-link class="my-link">Posts</a>
            <a href="/about" data-link class="my-link">About</a>
        `;

    if (isLoggedIn()) {
        nav += `
            <a href="/logout" data-link class="my-link">Logout</a>
            <a href="/me" data-link class="my-link">About ME</a>
        `;
    } else {
        nav += `
            <a href="/login" data-link class="my-link">Login via Google</a>
            <a href="/register" data-link class="my-link">Register</a>
        `;
    }

    let loginName = "Not logged in";
    let loginPhoto = "";
    if (isLoggedIn()) {
        const loggedInUser = getUser();
        if (loggedInUser) {
            console.log(loggedInUser);

            loginName = "Logged in as " + loggedInUser.userName;
            loginPhoto = loggedInUser.profilePic;
        }
    }
    nav += `
        <div id="login-name">
            <span>${loginName}</span>
            <img src="${loginPhoto}"> 
        </div>
        `;

    nav += `</nav>`;
    return nav;
//                <a href="/oauth2/authorization/google" data-link class="my-bypass my-link">Login via Google</a>
}