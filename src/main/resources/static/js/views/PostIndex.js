import CreateView from "../createView.js";

export default function PostIndex(props) {
    const postsHTML = generatePostsHTML(props.posts);

    return `
        <header>
            <h1>Posts Page</h1>
        </header>
        <main>
              <h3>Lists of posts</h3>
            <div>
                ${postsHTML}   
            </div>
            
            <h3>Add a post</h3>
            <form>
                <label for="title">Title</label><br>
                <input id="title" name="title" type="text" placeholder="Enter title">
                <br>
                <label for="content">Content</label><br>
                <textarea id="content" name="content" rows="10" cols="50" placeholder="Enter content"></textarea>
                <button id="addPost" name="addPost">Add Post</button>
            </form>
            
        </main>
    `;
}

function generatePostsHTML(posts) {
    let postsHTML = `
        <table class="table">
        <thead>
        <tr>
            <th scope="col">Title</th>
            <th scope="col">Content</th>
        </tr>
        </thead>
        <tbody>
    `;
    for (let i = 0; i < posts.length; i++) {
        const post = posts[i];
        postsHTML += `<tr>
            <td>${post.title}</td>
            <td>${post.content}</td>
            <td><button data-id=${post.id} class="editPost">Edit</button></td>
            <td><button data-id=${post.id} class="deletePost">Delete</button></td>
            </tr>`;
    }
    postsHTML += `</tbody></table>`;
    return postsHTML;
}

export function postSetup() {
    addPostHandler();
    editPostHandlers();
    deletePostHandlers();
}

function editPostHandlers() {
    const editButtons = document.querySelectorAll(".editPost");
    for (let i = 0; i < editButtons.length; i++) {
        editButtons[i].addEventListener("click", function(event) {
            console.log("edit a post");
        });
    }
}

function deletePostHandlers() {

}

function addPostHandler() {
    const addButton = document.querySelector("#addPost");
    addButton.addEventListener("click", function(event) {
        const titleField = document.querySelector("#title");
        const contentField = document.querySelector("#content");

        let newPost = {
            title: titleField.value,
            content: contentField.value
        }

        let request = {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(newPost)
        }

        fetch("http://localhost:8081/api/posts", request)
            .then(response => {
                console.log(response.status);
                CreateView("/posts");
            })
    });
}