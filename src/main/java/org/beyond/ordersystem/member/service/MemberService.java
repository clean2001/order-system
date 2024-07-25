package org.beyond.ordersystem.member.service;

import lombok.RequiredArgsConstructor;
import org.beyond.ordersystem.member.domain.Member;
import org.beyond.ordersystem.member.dto.CreateMemberRequest;
import org.beyond.ordersystem.member.dto.CreateMemberResponse;
import org.beyond.ordersystem.member.dto.MemberResponse;
import org.beyond.ordersystem.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Member member = CreateMemberRequest.toEntity(createMemberRequest, passwordEncoder);

        Member savedMember = memberRepository.save(member);

        return CreateMemberResponse.fromEntity(member);
    }
}
