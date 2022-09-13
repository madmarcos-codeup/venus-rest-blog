package docrob.venusrestblog.data;

import lombok.*;

import java.time.LocalDate;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private long id;
    private String userName;
    private String email;
    private String password;
    private LocalDate createdAt;
    private UserRole role;

    private Collection<Post> posts;

}
