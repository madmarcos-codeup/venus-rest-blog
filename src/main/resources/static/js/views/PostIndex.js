import CreateView from "../createView.js";
import {getHeaders, getUser, isLoggedIn} from "../auth.js";

let posts;
let loggedInUser;
let categories;

export default function PostIndex(props) {
    // refresh the currently logged in user
    loggedInUser = getUser();

    const postsHTML = generatePostsHTML(props.posts);
    // save this for loading edits later
    posts = props.posts;
    categories = props.categories;

    const addPostHTML = generateAddPostHTML();

    return `
        <header>
            <h1>Posts Page</h1>
        </header>
        <main>
              <h3>Lists of posts</h3>
            <div>
                ${postsHTML}   
            </div>
            
            ${addPostHTML}
            
        </main>
    `;
}

function generateAddPostHTML() {
    let addHTML = ``;

    if(!isLoggedIn()) {
        return addHTML;
    }

    const categoryHTML = generateCategoryHTML(categories);

    addHTML = `<h3>Add a post</h3>
            <form>
                <div>
                    <label for="title">Title</label><br>
                    <input id="title" name="title" class="form-control" type="text" placeholder="Enter title">
                    <div class="invalid-feedback">
                        Title cannot be blank.
                    </div>
                    <div class="valid-feedback">
                        Your title is ok!
                    </div>
                </div>
                
                <div>
                    <label for="content">Content</label><br>
                    <textarea id="content" class="form-control" name="content" rows="5" cols="50" placeholder="Enter content"></textarea>
                    <div class="invalid-feedback">
                        Content cannot be blank.
                    </div>
                    <div class="valid-feedback">
                        Content is ok!
                    </div>
                </div>
                
                <h6 class="my-category-group">Categories</h6>
                ${categoryHTML}
                
                <button data-id="0" id="savePost" name="savePost" type="button" class="my-button button btn-primary">Save Post</button>
            </form>`;

    return addHTML;
}

function generateCategoryHTML(categories) {
    let catHTML = ``;
    if(!categories) {
        return catHTML;
    }
    for (let i = 0; i < categories.length; i++) {
        const category = categories[i];

        catHTML += `
            <div class="form-check">
                <input class="form-check-input category-checkbox" type="checkbox" value="" data-id="${category.id}" id="category_${category.id}">
                <label class="form-check-label" for="flexCheckDefault">
                    ${category.name}
                </label>
            </div>`;
    }
    return catHTML;
}

function generatePostsHTML(posts) {
    let postsHTML = `
        <table class="table">
        <thead>
        <tr>
            <th scope="col">Title</th>
            <th scope="col">Content</th>
            <th scope="col">Categories</th>
            <th scope="col">Author</th>
        </tr>
        </thead>
        <tbody>
    `;

    for (let i = 0; i < posts.length; i++) {
        const post = posts[i];

        let categories = '';
        if(post.categories) {
            for (let j = 0; j < post.categories.length; j++) {
                if (categories !== "") {
                    categories += ", ";
                }
                categories += post.categories[j].name;
            }
        }
        let authorName = "";
        if(post.author) {
            authorName = post.author.userName;
        }
        postsHTML += `<tr>
            <td>${post.title}</td>
            <td>${post.content}</td>
            <td>${categories}</td>
            <td>${authorName}</td>`;

        // only admins and the author of the post can edit/delete it
        if(loggedInUser) {
            if (loggedInUser.role === 'ADMIN' || loggedInUser.userName === post.author.userName) {
                postsHTML += `<td><button data-id=${post.id} class="row-button button btn-primary editPost">Edit</button></td>
                <td><button data-id=${post.id} class="row-button button btn-danger deletePost">Delete</button></td>`;
            } else {
                postsHTML += `<td></td><td></td>`;
            }
        }
        postsHTML += `</tr>`;
    }
    postsHTML += `</tbody></table>`;
    return postsHTML;
}




export function postSetup() {
    setupSaveHandler();
    setupEditHandlers();
    setupDeleteHandlers();
    setupValidationHandlers();
    validateFields();
}

function setupValidationHandlers() {
    let input = document.querySelector("#title");
    input.addEventListener("keyup", validateFields);
    input = document.querySelector("#content");
    input.addEventListener("keyup", validateFields);
}

function validateFields() {
    let isValid = true;
    let input = document.querySelector("#title");
    if(input.value.trim().length < 1) {
        input.classList.add("is-invalid");
        input.classList.remove("is-valid");
        isValid = false;
    } else {
        input.classList.add("is-valid");
        input.classList.remove("is-invalid");
    }

    input = document.querySelector("#content");
    if(input.value.trim().length < 1) {
        input.classList.add("is-invalid");
        input.classList.remove("is-valid");
        isValid = false;
    } else {
        input.classList.add("is-valid");
        input.classList.remove("is-invalid");
    }

    return isValid;
}

function setupEditHandlers() {
    // target all delete buttons
    const editButtons = document.querySelectorAll(".editPost");
    // add click handler to all delete buttons
    for (let i = 0; i < editButtons.length; i++) {
        editButtons[i].addEventListener("click", function(event) {

            // get the post id of the delete button
            const postId = parseInt(this.getAttribute("data-id"));

            loadPostIntoForm(postId);
        });
    }
}

function loadPostIntoForm(postId) {
    // go find the post in the posts data that matches postId
    const post = fetchPostById(postId);
    if(!post) {
        console.log("did not find post for id " + postId);
        return;
    }

    // load the post data into the form
    const titleField = document.querySelector("#title");
    const contentField = document.querySelector("#content");
    titleField.value = post.title;
    contentField.value = post.content;

    // load the post's categories
    const checkboxes = document.querySelectorAll(".category-checkbox");
    for (let i = 0; i < checkboxes.length; i++) {
        checkboxes[i].checked = false;
        if(post.categories) {
            for (let j = 0; j < post.categories.length; j++) {
                if(parseInt(checkboxes[i].getAttribute("data-id")) === post.categories[j].id) {
                    checkboxes[i].checked = true;
                }
            }
        }
    }

    validateFields();

    const saveButton = document.querySelector("#savePost");
    saveButton.setAttribute("data-id", postId);
}

function fetchPostById(postId) {
    for (let i = 0; i < posts.length; i++) {
        if(posts[i].id === postId) {
            return posts[i];
        }

    }
    // didn't find it so return something falsy
    return false;
}






function setupDeleteHandlers() {
    // target all delete buttons
    const deleteButtons = document.querySelectorAll(".deletePost");
    // add click handler to all delete buttons
    for (let i = 0; i < deleteButtons.length; i++) {
        deleteButtons[i].addEventListener("click", function(event) {

            // get the post id of the delete button
            const postId = this.getAttribute("data-id");

            deletePost(postId);
        });
    }
}

function deletePost(postId) {
    const request = {
        method: "DELETE",
        headers: getHeaders(),
    }
    const url = POST_API_BASE_URL + `/${postId}`;
    fetch(url, request)
        .then(function(response) {
            if(response.status !== 200) {
                console.log("fetch returned bad status code: " + response.status);
                console.log(response.statusText);
                return;
            }
            CreateView("/posts");
        })
}







function setupSaveHandler() {
    const saveButton = document.querySelector("#savePost");
    if(saveButton) {
        saveButton.addEventListener("click", function (event) {
            const postId = parseInt(this.getAttribute("data-id"));
            savePost(postId);
        });
    }
}

function savePost(postId) {
    // get the title and content for the new/updated post
    const titleField = document.querySelector("#title");
    const contentField = document.querySelector("#content");

    // don't allow save if title or content are invalid
    if(!validateFields()) {
        return;
    }

    // don't need this anymore since I am now using bootstrap validation

    // if(titleField.value.trim().length < 1) {
    //     showNotification("Title cannot be blank!", "warning");
    //     return;
    // }
    // if(contentField.value.trim().length < 1) {
    //     showNotification("Content cannot be blank!", "warning");
    //     return;
    // }
    // make the new/updated post object
    const selectedCategories = getSelectedCategories();
    const post = {
        title: titleField.value,
        content: contentField.value,
        categories: selectedCategories
    }

    // make the request
    const request = {
        method: "POST",
        headers: getHeaders(),
        body: JSON.stringify(post)
    }
    let url = POST_API_BASE_URL;

    // if we are updating a post, change the request and the url
    if(postId > 0) {
        request.method = "PUT";
        url += `/${postId}`;
    }

    fetch(url, request)
        .then(function(response) {
            if(response.status !== 200) {
                console.log("fetch returned bad status code: " + response.status);
                console.log(response.statusText);
                return;
            }
            CreateView("/posts");
        })
}

function getSelectedCategories() {
    let cats = [];
    const checkboxes = document.querySelectorAll(".category-checkbox");

    for (let i = 0; i < checkboxes.length; i++) {
        const checkbox = checkboxes[i];
        if(checkbox.checked) {
            const id = checkbox.getAttribute("data-id");
            const cat = {
                id
            }
            cats.push(cat);
        }
    }
    return cats;
}