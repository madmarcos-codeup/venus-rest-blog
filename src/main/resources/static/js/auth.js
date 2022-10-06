
export function setLoggedInUserInfo() {
    const jwt = JSON.parse(localStorage.getItem("access_token"));
    setUserInfo(jwt);
}

export function checkForLoginTokens(url) {
    // console.log(url);
    // access_token is given back from spring after #
    let parts = url.split("#");
    if(parts.length < 2)
        return false;

    parts = parts[1].split("&");
    let tokens = [];
    for (let i = 0; i < parts.length; i++) {
        const pair = parts[i].split("=");
        if(pair.length > 1 && (pair[0] === "access_token" || pair[0] === "refresh_token"))
            tokens[pair[0]] = pair[1];
    }
    if(!tokens['access_token'])
        return false;

    setTokens(tokens);
    return true;
}

/**
 * Gets the Authorization header needed for making requests to protected endpoints
 * This function should be used only after the user is logged in
 * @returns {{Authorization: string, "Content-Type": string}|{"Content-Type": string}}
 */
export function getHeaders() {
    const token = localStorage.getItem("access_token");
    return token
        ? {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + `${token}`}
        : {'Content-Type': 'application/json'};
}

/**
 * Attempts to set the access and refresh tokens needs to authenticate and authorize the client and user
 * @param responseData
 */
export function setTokens(jwt) {
    if(jwt) {
        localStorage.setItem("access_token", jwt);
        setUserInfo(jwt);
        console.log("Access token and user info set");
    }
}

export function isLoggedIn() {
    if(localStorage.getItem('access_token')) {
        return true;
    } else {
        return false;
    }

}

//  returns an object with user_name and authority from the access_token
export function getUser() {
    const userString = localStorage.getItem("user");
    console.log(userString);
    if(userString)
        return JSON.parse(userString);
    return null;
}

// this only gets called when the user logs in (receives the JWT)
export async function setUserInfo(jwt) {
    if(!jwt) {
        return false;
    }
    const parts = jwt.split('.');
    const payload = parts[1];
    const decodedPayload = atob(payload);
    const payloadObject = JSON.parse(decodedPayload);
    // console.log(payloadObject);

    const request = {
        method: 'GET',
        headers: getHeaders()
    };
    return fetch(`${BACKEND_HOST_URL}/api/users/me`, request)
        .then((response) => {
            return response.json();
        }).then((data) => {
            const user = {
                userName: payloadObject.name,
                role: data.role,
                profilePic: payloadObject.picture
            };
            window.localStorage.setItem("user", JSON.stringify(user));
            // console.log(user);
            return user;
        }).catch(error => {
            console.log("FETCH ERROR: " + error);
        });
}

export async function removeStaleTokens() {
    console.log("Removing stale tokens...");

    // clear tokens from localStorage if backend tells us the tokens are invalid
    // make the request
    const request = {
        method: 'GET',
        headers: getHeaders()
    };
    await fetch(`/api/users/me`, request)
        .then((response) => {
            // if fetch error then you might be using stale tokens
            if (response.status === 401) {
                window.localStorage.clear();
            }
        }).catch(error => {
            console.log("FETCH ERROR: " + error);
        });
}