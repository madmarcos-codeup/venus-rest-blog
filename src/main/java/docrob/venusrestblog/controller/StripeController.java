package docrob.venusrestblog.controller;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/stripe", produces = "application/json")
public class StripeController {

    @GetMapping("/create-payment-intent")
    public String createPaymentIntent() {
        Gson gson = new Gson();
        Stripe.apiKey = "sk_test_51LpjqUJQWiixUeCyHBp0nKfTKungwRO0TuPL0rTdZZWq2KnrBlkJvug9xkSFfiAX6bSARvVn7vMliJGyddgrWGS500Z2ckOh7G";

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .setCurrency("EUR")
                .setAmount(1999L)
                .build();

        try {
            // Create a PaymentIntent with the order amount and currency
            PaymentIntent intent = PaymentIntent.create(params);

            // Send PaymentIntent details to client
            return gson.toJson(new StripePaymentResponse(intent.getClientSecret()));
        } catch(StripeException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
