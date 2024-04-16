package shop.jtoon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;

@Repository
public interface MemberCookieRepository extends JpaRepository<MemberCookie, Long> {

	Optional<MemberCookie> findByMember(Member member);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select mc from MemberCookie mc where mc.member.id = :memberId")
	Optional<MemberCookie> findByMemberIdWithPessimisticLock(Long memberId);
}
