package org.beyond.ordersystem.member.controller;

import lombok.RequiredArgsConstructor;
import org.beyond.ordersystem.common.auth.JwtTokenProvider;
import org.beyond.ordersystem.common.dto.SuccessResponse;
import org.beyond.ordersystem.member.domain.Member;
import org.beyond.ordersystem.member.dto.CreateMemberRequest;
import org.beyond.ordersystem.member.dto.CreateMemberResponse;
import org.beyond.ordersystem.member.dto.MemberLoginDto;
import org.beyond.ordersystem.member.dto.MemberResponse;
import org.beyond.ordersystem.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/member/list")
    public ResponseEntity<SuccessResponse> memberList(@PageableDefault(size = 10) Pageable pageable) {
        Page<MemberResponse> memberList = memberService.memberList(pageable);
        SuccessResponse response = SuccessResponse.builder()
                .statusMessage("멤버 리스트입니다.")
                .httpStatus(HttpStatus.OK)
                .result(memberList)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/member/create")
    public ResponseEntity<SuccessResponse> createMember(@Valid @RequestBody CreateMemberRequest createMemberRequest) {
        CreateMemberResponse member = memberService.createMember(createMemberRequest);
        SuccessResponse response = SuccessResponse.builder()
                .statusMessage("멤버 생성 완료.")
                .httpStatus(HttpStatus.CREATED)
                .result(member)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<SuccessResponse> doLogin(@RequestBody MemberLoginDto dto) {
        // email, password 일치하는지 검증
        Member member = memberService.login(dto);

        // 일치할 경우 accesssToken 생성
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
        // 생성된 토큰을 CommonResDto에 담아 사용자에게 리턴

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);
        SuccessResponse successResponse = new SuccessResponse(HttpStatus.OK, "login is successful", loginInfo);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
    }
}