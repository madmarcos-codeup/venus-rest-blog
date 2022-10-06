export default function Home(props) {
    return `
        <header>
            <h1>Checkout</h1>
        </header>
        <main>
            <h3>Your Order</h3>
            <table class="table">
                <tr>
                    <th>Item Description</th>
                    <th>Amount</th>
                </tr>
                <tr>
                    <td>Fern</td>
                    <td>$19.99</td>
                </tr>
                <tr>
                    <td><b>Total</b></td>
                    <td><b>$19.99</b></td>
                </tr>            
            </table>
            
            <h3>Pay for yo stuff here</h3>
            <form id="payment-form">
                <div id="payment-element">
                    <!-- Elements will create form elements here -->
                </div>
                <button id="submit">Pay now</button>
                <div id="error-message">
                    <!-- Display error message to your customers here -->
                </div>
            </form>  
        </main>
    `;
}

export async function HomeEvent() {
    const YOUR_STRIPE_KEY = 'pk_test_51LpjqUJQWiixUeCyeIjrO7axb0zcCGFblGnFN1vPZcuXj5LlXUDahBH9F4hb8haNmzm1UDiqqssqA7di8C7i4Dbc00zM2C6oQf';
    let stripe = Stripe(YOUR_STRIPE_KEY, {
        apiVersion: '2020-08-27',
    });
    // const publishableKey = "YOUR STRIPE KEY";

    // On page load, we create a PaymentIntent on the server so that we have its clientSecret to
    // initialize the instance of Elements below. The PaymentIntent settings configure which payment
    // method types to display in the PaymentElement.
    const {
        error: backendError,
        clientSecret
    } = await fetch('/api/stripe/create-payment-intent').then(r => r.json());
    if (backendError) {
        console.log(backendError.message);
    }
    console.log(`Client secret returned.`);

    // Initialize Stripe Elements with the PaymentIntent's clientSecret,
    // then mount the payment element.
    // const clientSecret = "TEST";
    const elements = stripe.elements({ clientSecret });
    const paymentElement = elements.create('payment');
    paymentElement.mount('#payment-element');

    // When the form is submitted...
    const form = document.getElementById('payment-form');
    let submitted = false;
    form.addEventListener('submit', async (e) => {
        console.log("submitting payment!");

        e.preventDefault();

        // Disable double submission of the form
        if(submitted) { return; }
        submitted = true;
        form.querySelector('button').disabled = true;

        const nameInput = document.querySelector('#name');

        // Confirm the card payment given the clientSecret
        // from the payment intent that was just created on
        // the server.
        const {error: stripeError} = await stripe.confirmPayment({
            elements,
            confirmParams: {
                return_url: `${window.location.origin}/paymentOk`,
            }
        });

        if (stripeError) {
            console.log(stripeError.message);

            // reenable the form.
            submitted = false;
            form.querySelector('button').disabled = false;
            return;
        }
    });
}