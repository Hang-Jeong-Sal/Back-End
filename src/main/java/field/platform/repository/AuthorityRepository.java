package field.platform.repository;

import field.platform.domain.Authority;
import field.platform.domain.UserRole;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthorityRepository {
    private final EntityManager em;

    //    public void saveAuthority(Authority authority){
//        em.persist(authority);
//    }
    public Optional<Authority> findByAuthorityName(UserRole userRole){
        Authority authority = em.
                createQuery("select a from Authority a where a.authorityName = :authorityName", Authority.class)
                .setParameter("authorityName", userRole)
                .getSingleResult();
        System.out.println("authority = " + authority);
        return Optional.of(authority);
    }
}
