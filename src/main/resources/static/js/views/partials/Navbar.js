import {isLoggedIn} from "../../auth.js";

export default function Navbar(props) {
    let navbar = `<nav>
        <a href="/" data-link>Home</a>
        <a href="/about" data-link>About</a>
        <a href="/posts" data-link>Posts</a>
    `;
    if(isLoggedIn()) {
        navbar += `
            <a href="/me" data-link>About ME</a>
            <a href="/logout" data-link>Logout</a>
        `;
    } else {
        navbar += `
            <a href="/login" data-link>Login</a>
            <a href="/register" data-link>Register</a>
        `;
    }
    navbar += `</nav>`;
    return navbar;
}