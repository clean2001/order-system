package org.beyond.ordersystem.member.controller;

import lombok.RequiredArgsConstructor;
import org.beyond.ordersystem.common.dto.SuccessResponse;
import org.beyond.ordersystem.member.dto.CreateMemberRequest;
import org.beyond.ordersystem.member.dto.CreateMemberResponse;
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

@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

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
}