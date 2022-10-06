package docrob.venusrestblog.controller;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StripePaymentResponse {
    private String clientSecret;
}
