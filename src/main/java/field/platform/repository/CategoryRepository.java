package field.platform.repository;

import field.platform.domain.Category;
import field.platform.domain.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(CategoryName categoryName);
}
