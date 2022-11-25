package field.platform.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    public Member(String email, String username, String password, String profile, UserRole role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.profile = profile;
        this.role = role;
    }

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    //추가
    private String email;
    private String username;
    private String password;
    private String profile;

    //add for login
    @Enumerated(value = EnumType.STRING)
    private UserRole role;
    private Long kakaoId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Ground> grounds = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberGroundLikes> likes = new ArrayList<>();

    public Member(String email, String username, String password, String profile) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.profile = profile;
    }
}

