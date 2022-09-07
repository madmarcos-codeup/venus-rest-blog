package docrob.venusrestblog.data;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Post {
    private Long id;
    private String title;
    private String content;
}

