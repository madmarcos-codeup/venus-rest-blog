import CreateView from "../createView.js"

let me;
export default function prepareUserHTML(props) {
    me = props.me;

    const userPostHTML = createPostHTML(me);

    // make the user's original pw available somewhere in here
    return `
        <h1>User Info</h1>
        <h2>${props.me.userName}</h2>
        <h2>${props.me.email}</h2>
        
        <form>
            <label for="oldpassword">Please enter your current password</label>
            <input type="password" id="oldpassword" name="oldpassword">
            <br>

            <label for="newpassword">New password</label>
            <input type="password" id="newpassword" name="newpassword">
            <br>
            <label for="confirmpassword">Confirm new password</label>
            <input type="password" id="confirmpassword" name="confirmpassword">
            
            <br>
            <button id="toggleShowPassword" name="toggleShowPassword">Show Password?</button>
            <button id="updatePassword" name="updatePassword">Save New Password</button>
        </form>
        
        <hr>
        ${userPostHTML}
        
    `;
}

function createPostHTML(user) {
    let html = `
        <table class="table">
        <thead>
        <tr>
            <th scope="col">Title</th>
            <th scope="col">Content</th>
        </tr>
        </thead>
        <tbody>
    `;

    // add a row to the table for each user post
    for (let i = 0; i < user.posts.length; i++) {
        const post = user.posts[i];
        html += `<tr>
            <td>${post.title}</td>
            <td>${post.content}</td>
            </tr>`;
    }

    // finish the table
    html += `
        </tbody>
        </table>`;

    return html;
}

export function prepareUserJS() {
    doTogglePasswordHandler();
    doSavePasswordHandler();
}

function doSavePasswordHandler() {
    const button = document.querySelector("#updatePassword");
    button.addEventListener("click", function(event) {
        // grab the 3 password field values
        const oldPasswordField = document.querySelector('#oldpassword');
        const newPasswordField = document.querySelector('#newpassword');
        const confirmPasswordField = document.querySelector('#confirmpassword');

        const oldPassword = oldPasswordField.value;
        const newPassword = newPasswordField.value;
        const confirmPassword = confirmPasswordField.value;

        const request = {
            method: "PUT",
        }
        const url = `${USER_API_BASE_URL}/${me.id}/updatePassword?oldPassword=${oldPassword}&newPassword=${newPassword}`

        fetch(url, request)
            .then(function(response) {
                CreateView("/");
            });
    });
}

function doTogglePasswordHandler() {
    const button = document.querySelector("#toggleShowPassword");
    button.addEventListener("click", function(event) {
        // grab a reference to confirmpassword
        const oldPassword = document.querySelector("#oldpassword");
        const newPassword = document.querySelector("#newpassword");
        const confirmPassword = document.querySelector("#confirmpassword");
        if(confirmPassword.getAttribute("type") === "password") {
            confirmPassword.setAttribute("type", "text");
            oldPassword.setAttribute("type", "text");
            newPassword.setAttribute("type", "text");
        } else {
            confirmPassword.setAttribute("type", "password");
            oldPassword.setAttribute("type", "password");
            newPassword.setAttribute("type", "password");
        }
    });
}