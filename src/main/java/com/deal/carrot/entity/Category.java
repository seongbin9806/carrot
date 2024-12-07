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
@Table(name = "카테고리")
public class Category {
    @Id
    @Column(name = "카테고리명")
    private String categoryName;

    @Transient
    private boolean isSelected = false;
}