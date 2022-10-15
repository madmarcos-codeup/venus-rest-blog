
let post = false;

export default function PostDetail(props) {
    post = props.post;
    return `
        <h2>${post.title}</h2>
        <h4>${post.author.userName}</h4>
        <h4>${post.content}</h4>
    `;
}

export function PostDetailEvents() {

}