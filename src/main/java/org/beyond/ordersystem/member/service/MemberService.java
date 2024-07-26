package org.beyond.ordersystem.member.service;

import lombok.RequiredArgsConstructor;
import org.beyond.ordersystem.member.domain.Member;
import org.beyond.ordersystem.member.dto.CreateMemberRequest;
import org.beyond.ordersystem.member.dto.CreateMemberResponse;
import org.beyond.ordersystem.member.dto.MemberLoginDto;
import org.beyond.ordersystem.member.dto.MemberResponse;
import org.beyond.ordersystem.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<MemberResponse> memberList(Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(MemberResponse::fromEntity);
    }

    public CreateMemberResponse createMember(CreateMemberRequest createMemberRequest) {
        // 중복 검증
        Optional<Member> findMember = memberRepository.findByEmail(createMemberRequest.getEmail());
        if(findMember.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = CreateMemberRequest.toEntity(createMemberRequest, passwordEncoder);
        Member savedMember = memberRepository.save(member);

        return CreateMemberResponse.fromEntity(member);
    }

    public Member login(MemberLoginDto dto) {
        // 이메일 존재여부 체크
        Member member = memberRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

        // 패스워드 일치여부
        if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())) { // 반드시 matched 메서드를 사용해야 제대로 동작
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }
}
