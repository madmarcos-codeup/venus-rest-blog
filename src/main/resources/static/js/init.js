import createView from './createView.js';
import {isLoggedIn} from "./auth";


export default function init() {
    loadViewOnPageRequest();
    addListenerToNavLinks();
}
/**
 * When the DOM loads, build the view given the current endpoint.
 */
function loadViewOnPageRequest() {
    window.addEventListener('DOMContentLoaded', function() {
        //TODO: Switched to location.pathname so the route would be accurate to current view
        // if user is new then force them to go to /register
        createView(location.pathname);
    });
}

/**
 * Add a listener that will change the view if a nav link is clicked.
 */
function addListenerToNavLinks() {
    document.addEventListener('click', event => {
        if(event.target.classList.contains('my-bypass')) {
            return;
        }
        if(event.target.type && event.target.type === "checkbox") {
            return;
        }
        if(event.target.matches('label')) {
            return;
        }
        if(event.target.type && event.target.type === "file") {
            return;
        }
        if(event.target.type && event.target.type === "submit") {
            return;
        }
        // console.log(event);
        if(event.target.dataset['passthru'] !== undefined) {
            event.target = event.target.parentElement;
            return;
        }
        event.preventDefault();
        if (event.target.dataset['link'] !== undefined) {
            const URI = event.target.href.substring(location.origin.length);
            createView(URI);
        }
    });
}

