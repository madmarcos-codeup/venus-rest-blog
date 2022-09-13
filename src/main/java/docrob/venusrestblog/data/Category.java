package docrob.venusrestblog.data;

import lombok.*;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Category {
    private Long id;
    private String name;

    private Collection<Post> posts;
}
