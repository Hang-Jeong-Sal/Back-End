package field.platform.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

import field.platform.dto.login.LoginRequestDto;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member {

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
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="member_authority",
            joinColumns = {@JoinColumn(name="member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name="authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities = new HashSet<>();

    private Long kakaoId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Ground> grounds = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberGroundLikes> likes = new ArrayList<>();

    @Builder
    public Member(String email, String password, Set<Authority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @Builder
    public Member(Long kakaoId, String username, String email, String profile, Set<Authority> authorities) {
        this.kakaoId = kakaoId;
        this.username = username;
        this.email = email;
        this.profile = profile;
        this.authorities = authorities;
    }

}

