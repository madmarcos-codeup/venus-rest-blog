/**
 * Given an object containing all the required data for a given page, fetch all the needed data and return it as properties to pass to a view.
 * @param state
 * @param request
 * @returns {Promise<{}>}
 */
import createView from "./createView.js";

export default function fetchData(state, request) {
    const promises = [];
    //TODO: this needs to be moved to a prop file or env variable
    const baseUri = API_HOST;

    console.log("got to fetch data");
    console.log(request);
    for (let pieceOfState of Object.keys(state)) {
        console.log(baseUri + state[pieceOfState]);
        promises.push(
            fetch(baseUri + state[pieceOfState], request)
                .then(function (res) {
                    if(res.status === 400 && res.url.includes("oauth/token")) {
                        // console.log("FAILED LOGIN");
                        // console.log(res);
                        createView("/failedlogin");
                        return;
                    }

                    return res.json();
                }));
    }
    return Promise.all(promises).then(propsData => {
        const props = {};
        Object.keys(state).forEach((key, index) => {
            props[key] = propsData[index];
        });
        return props;
    });
}
