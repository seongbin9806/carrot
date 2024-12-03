package com.deal.carrot.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "학과")
public class Department {
    @Id
    @Column(name = "학과명")
    private String departmentName;
}