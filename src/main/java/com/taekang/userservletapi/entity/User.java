package com.taekang.userservletapi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "roleType", nullable = false)
  private RoleType roleType;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "is_block", nullable = false)
  private boolean isBlock;

  @CreatedDate
  @Column(name = "insert_date_time", nullable = false, updatable = false)
  private LocalDateTime insertDateTime;

  @CreatedBy
  @Column(name = "insert_id", nullable = false, updatable = false)
  private String insertId;

  @LastModifiedDate
  @Column(name = "update_date_time")
  private LocalDateTime updateDateTime;

  @LastModifiedBy
  @Column(name = "update_id")
  private String updateId;

  @Column(name = "delete_date_time")
  private LocalDateTime deleteDateTime;

  @Column(name = "delete_id")
  private String deleteId;
}
