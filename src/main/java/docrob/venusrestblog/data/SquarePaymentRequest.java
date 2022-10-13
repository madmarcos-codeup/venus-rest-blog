package docrob.venusrestblog.data;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SquarePaymentRequest {
    private String locationId;
    private String sourceId;
    private String idempotencyKey;
}
