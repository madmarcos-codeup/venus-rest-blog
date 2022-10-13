package docrob.venusrestblog.controller;

import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.api.PaymentsApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.Money;
import docrob.venusrestblog.data.SquarePaymentRequest;
import docrob.venusrestblog.data.SquareResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
public class SquareController {

    @Value("${square.apikey}")
    private String SQUARE_API_KEY;

    @PostMapping("")
    public SquareResponse createPayment(@RequestBody SquarePaymentRequest request) {
        String message = "";
        System.out.println(request);

        SquareClient client = new SquareClient.Builder()
                .environment(Environment.SANDBOX)
                .accessToken(SQUARE_API_KEY)
                .build();

        PaymentsApi api = client.getPaymentsApi();
        Money money = new Money(50L, "USD");
        CreatePaymentRequest payment = new CreatePaymentRequest.Builder(request.getSourceId(), request.getIdempotencyKey(), money).build();
        try {
            api.createPayment(payment);
            message = "success";
        } catch (ApiException | IOException e) {
            System.out.println(e.getMessage());
            message = "error";
        }

        SquareClient.shutdown();

        return new SquareResponse(message);
    }
}
