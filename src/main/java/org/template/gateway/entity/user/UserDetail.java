package org.template.gateway.entity.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

@Data
@ToString
@Builder
@Table(name= "user_detail")
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userCode;

    private String userId;

    private String userPw;

    private String userEmail;

    private String userName;

    private String roleGroup;
}
