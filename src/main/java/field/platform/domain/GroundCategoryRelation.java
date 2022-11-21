package field.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GroundCategoryRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @Column(name = "ground_category_relation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ground_id")
    private Ground ground;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    public GroundCategoryRelation(Ground ground, Category category) {
        this.ground = ground;
        this.category = category;
    }
}
