import Home from "./views/Home.js";
import PostIndex, {postSetup} from "./views/PostIndex.js";
import About from "./views/About.js";
import Error404 from "./views/Error404.js";
import Loading from "./views/Loading.js";
import Login, {LoginEvent} from "./views/Login.js";
import Register from "./views/Register.js"
import {RegisterEvent} from "./views/Register.js";
import prepareUserHTML, {prepareUserJS} from "./views/User.js";
import DoLogin, {DoLoginEvents} from "./views/DoLogin.js";
import Logout, {LogoutEvent} from "./views/Logout.js";

/**
 * Returns the route object for a specific route based on the given URI
 * @param URI
 * @returns {*}
 */
export default function router(URI) {
    const routes = {
        '/': {
            returnView: Home,
            state: {},
            uri: '/',
            title: 'Home',
        },
        '/dologin': {
            returnView: DoLogin,
            state: {},
            uri: '/dologin',
            title: "DoLogin",
            viewEvent: DoLoginEvents
        },
        '/login': {
            returnView: Login,
            state: {},
            uri: '/login',
            title: "Login via Google",
            viewEvent: LoginEvent
        },
        '/logout': {
            returnView: Logout,
            state: {},
            uri: '/logout',
            title: "Logout",
            viewEvent: LogoutEvent
        },
        '/register': {
            returnView: Register,
            state: {},
            uri: '/register',
            title: 'Register',
            viewEvent: RegisterEvent
        },
        '/me': {
            returnView: prepareUserHTML,
            state: {
                me: '/api/users/me'
            },
            uri: '/me',
            title: 'User Info',
            viewEvent: prepareUserJS
        },
        '/posts': {
            returnView: PostIndex,
            state: {
                posts: '/api/posts',
                categories: '/api/categories'
            },
            uri: '/posts',
            title: 'All Posts',
            viewEvent: postSetup
        },
        '/about': {
            returnView: About,
            state: {},
            uri: '/about',
            title: 'About',
        },
        '/error': {
            returnView: Error404,
            state: {},
            uri: location.pathname,
            title: ' ERROR',
        },
        '/loading': {
            returnView: Loading,
            state: {},
            uri: location.pathname,
            title: 'Loading...',
        }
    };

    return routes[URI];
}

