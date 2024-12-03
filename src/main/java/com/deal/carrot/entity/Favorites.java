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
@Table(name = "즐겨찾기")
public class Favorites {
    @Id
    @Column(name = "학번")
    private int studentNumber;

    @Column(name = "게시글번호")
    private int postId;
}
