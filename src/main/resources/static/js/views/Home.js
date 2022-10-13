const APPLICATION_ID = "sandbox-sq0idb-LcJnieGEP4Kdi_gUM0bjjQ";
const LOCATION_ID = "LT12XCM5DP8MQ";

let card;

export default function Home(props) {
    return `
    <form id="payment-form">
   <div id="card-container"></div>
   <button id="card-button" type="button">Pay $1.00</button>
 </form>
 <div id="payment-status-container"></div>
`;
}

export async function HomeEvent() {
    const cardButton = document.getElementById(
        'card-button'
    );
    cardButton.addEventListener('click', async function (event) {
        await handlePaymentMethodSubmission(event, card, this);
    });

    if (!window.Square) {
        throw new Error('Square.js failed to load properly');
    }
    const payments = window.Square.payments(APPLICATION_ID, LOCATION_ID);
    try {
        card = await initializeCard(payments);
    } catch (e) {
        console.error('Initializing Card failed', e);
        return;
    }
}

async function initializeCard(payments) {
    const card = await payments.card();
    await card.attach('#card-container');
    return card;
}

// for credit card, paymentMethod is the card component
async function handlePaymentMethodSubmission(event, paymentMethod, cardButton) {
    event.preventDefault();

    try {
        // disable the submit button as we await tokenization and make a
        // payment request.
        cardButton.disabled = true;
        const token = await tokenize(paymentMethod);
        const paymentResults = await createPayment(token);
        displayPaymentResults('SUCCESS');

        console.debug('Payment Success', paymentResults);
        cardButton.disabled = false;
    } catch (e) {
        cardButton.disabled = false;
        displayPaymentResults('FAILURE');
        console.error(e.message);
    }
}

// This function tokenizes a payment method.
// The ‘error’ thrown from this async function denotes a failed tokenization,
// which is due to buyer error (such as an expired card). It is up to the
// developer to handle the error and provide the buyer the chance to fix
// their mistakes.
// for credit card, paymentMethod is the card component
async function tokenize(paymentMethod) {
    const tokenResult = await paymentMethod.tokenize();
    if (tokenResult.status === 'OK') {
        return tokenResult.token;
    } else {
        let errorMessage = `Tokenization failed-status: ${tokenResult.status}`;
        if (tokenResult.errors) {
            errorMessage += ` and errors: ${JSON.stringify(
                tokenResult.errors
            )}`;
        }
        throw new Error(errorMessage);
    }
}

// Call this function to send a payment token, buyer name, and other details
// to the project server code so that a payment can be created with
// Payments API
async function createPayment(token) {
    const body = JSON.stringify({
        locationId: LOCATION_ID,
        sourceId: token,
        idempotencyKey: uuidv4()
    });
    const paymentResponse = await fetch('/api/payment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body,
    });
    if (paymentResponse.ok) {
        return paymentResponse.json();
    }
    const errorBody = await paymentResponse.text();
    throw new Error(errorBody);
}

function uuidv4() {
    return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
    );
}

// Helper method for displaying the Payment Status on the screen.
// status is either SUCCESS or FAILURE;
function displayPaymentResults(status) {
    const statusContainer = document.getElementById(
        'payment-status-container'
    );
    if (status === 'SUCCESS') {
        statusContainer.classList.remove('is-failure');
        statusContainer.classList.add('is-success');
        statusContainer.innerHTML = "<p>SUCCESS</p>";
    } else {
        statusContainer.classList.remove('is-success');
        statusContainer.classList.add('is-failure');
        statusContainer.innerHTML = "<p>FAILURE</p>";
    }

    statusContainer.style.visibility = 'visible';
}