package field.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Ground extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @Column(name = "ground_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

    private String image;
    private String title;

    @Column(nullable = false)
    private String address;
    private String address1DepthName;
    private String address2DepthName;

    @Column(nullable = false)
    private String address3DepthName;



    /**
     * 그 필터 기준으로 할 속성값은 나중에 기획으로 부터 듣고나서 추가
     */
    /*
    가격이나 면적은 ,format이용
     */
    @Column(nullable = false)
    private int price;
    @Enumerated(EnumType.STRING)
    private GroundStatus status;

    @OneToMany(mappedBy = "ground", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();


    private LocalDateTime startDate;
    private LocalDateTime finishDate;

    private int areaSize;

    private double latitude;
    private double longitude;

    @Builder
    public Ground(String image, String title, String address, int price, GroundStatus status,
                  LocalDateTime startDate, LocalDateTime finishDate, int areaSize, double latitude, double longitude, String address3DepthName) {
        this.image = image;
        this.title = title;
        this.address = address;
        this.price = price;
        this.status = status;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.areaSize = areaSize;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address3DepthName = address3DepthName;
    }

    @OneToMany(mappedBy = "ground", cascade = CascadeType.ALL)
    private List<GroundCategoryRelation> groundCategoryRelations = new ArrayList<>();

    public void setCategory(List<Category> categories) {
        for (Category category : categories) {
            new GroundCategoryRelation(this, category);
        }
    }



}
