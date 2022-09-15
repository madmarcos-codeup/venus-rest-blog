package docrob.venusrestblog.repository;

import docrob.venusrestblog.data.Category;
import docrob.venusrestblog.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
