package shop.jtoon.payment.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import shop.jtoon.entity.CookieItem;
import shop.jtoon.entity.Member;
import shop.jtoon.entity.MemberCookie;
import shop.jtoon.exception.NotFoundException;
import shop.jtoon.repository.MemberCookieRepository;
import shop.jtoon.type.ErrorStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberCookieService {

	private final MemberCookieRepository memberCookieRepository;

	@Transactional
	public void createMemberCookie(String cookieItem, Member member) {
		CookieItem item = CookieItem.from(cookieItem);
		MemberCookie memberCookie = MemberCookie.create(item.getCount(), member);
		memberCookieRepository.save(memberCookie);
	}

	@Transactional
	public int useCookie(int cookieCount, Long memberId) {
		MemberCookie memberCookie = getByMemberIdWithPessimisticLock(memberId);
		memberCookie.decreaseCookieCount(cookieCount);

		return memberCookie.getCookieCount();
	}

	public int getMemberCookieCount(Member member) {
		MemberCookie memberCookie = getByMember(member);

		return memberCookie.getCookieCount();
	}

	private MemberCookie getByMemberIdWithPessimisticLock(Long memberId) {
		return memberCookieRepository.findByMemberIdWithPessimisticLock(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_COOKIE_NOT_FOUND));
	}

	private MemberCookie getByMember(Member member) {
		return memberCookieRepository.findByMember(member)
			.orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_COOKIE_NOT_FOUND));
	}
}
