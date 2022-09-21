import CreateView from "../createView.js"
import {isLoggedIn} from "../auth.js";
import createView from "../createView.js";

export default function Register(props) {
    if(isLoggedIn()) {
        createView("/");
        return;
    }

    return `
    <!DOCTYPE html>
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Register</title>
            </head>
            <body>
                <h1>Register</h1>
                   
                   <a href="/login" data-link>Go to login</a>
                   
                <form id="register-form">
                    <label for="username">Username</label>
                    <input id="username" name="username" type="text"/>
                    <label for="email">Email</label>
                    <input id="email" name="email" type="email">
                    <label for="password">Password</label>
                    <input id="password" name="password" type="password"/>
                    <button id="register-btn" type="button">Register</button>
                </form>
                               
            </body>
        </html>
`;
}

export function RegisterEvent(){
    const registerButton = document.querySelector("#register-btn");
    registerButton.addEventListener("click", function(event) {

        const usernameField = document.querySelector("#username");
        const emailField = document.querySelector("#email");
        const passwordField = document.querySelector("#password");

        let newUser = {
            userName: usernameField.value,
            email: emailField.value,
            password: passwordField.value
        }

        // console.log(newUser);

        let request = {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(newUser)
        }

        fetch(USER_API_BASE_URL + "/create", request)
            .then(response => {
                console.log(response.status);
                CreateView("/");
            })

    })
}