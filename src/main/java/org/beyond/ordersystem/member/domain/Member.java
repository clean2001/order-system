package org.beyond.ordersystem.member.domain;


import lombok.*;
import org.beyond.ordersystem.common.domain.Address;
import org.beyond.ordersystem.common.domain.BaseEntity;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Embedded
    private Address address;

}
