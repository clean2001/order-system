package org.beyond.ordersystem.member.dto;

import lombok.*;
import org.beyond.ordersystem.member.domain.Member;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String street;
    private String zipcode;

    public static MemberResponse fromEntity(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .city(member.getAddress().getCity())
                .street(member.getAddress().getStreet())
                .zipcode(member.getAddress().getZipcode())
                .build();
    }
}
