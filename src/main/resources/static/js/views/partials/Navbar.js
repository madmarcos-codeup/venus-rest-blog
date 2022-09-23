export default function Navbar(props) {
    return `
        <nav>
            <a href="/" data-link class="my-link">Home</a>
            <a href="/posts" data-link class="my-link">Posts</a>
            <a href="/about" data-link class="my-link">About</a>

            <a href="/login" data-link class="my-link">Login via Google</a>
            <a href="/logout" data-link class="my-link">Logout</a>

            <a href="/register" data-link class="my-link">Register</a>
            <a href="/me" data-link class="my-link">About ME</a>
        </nav>
    `;
//                <a href="/oauth2/authorization/google" data-link class="my-bypass my-link">Login via Google</a>
}