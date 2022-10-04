import createView from './createView.js';
import {checkForLoginTokens, setLoggedInUserInfo} from './auth.js';


export default function init() {
    if(checkForLoginTokens(window.location.href)) {
        setLoggedInUserInfo();
        createView("/");
        return;
    }
    loadViewOnPageRequest();
    addListenerToNavLinks();
}
/**
 * When the DOM loads, build the view given the current endpoint.
 */
function loadViewOnPageRequest() {
    window.addEventListener('DOMContentLoaded', function() {
        //TODO: Switched to location.pathname so the route would be accurate to current view
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
        if(e.target.type && e.target.type === "checkbox") {
            return;
        }
        if(e.target.matches('label')) {
            return;
        }
        event.preventDefault();
        if (event.target.dataset['link'] !== undefined) {
            const URI = event.target.href.substring(location.origin.length);
            createView(URI);
        }
    });
}

