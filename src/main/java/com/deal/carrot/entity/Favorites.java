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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "favoritesSequenceGenerator")
    @SequenceGenerator(name = "favoritesSequenceGenerator", sequenceName = "favoritesSequence", allocationSize = 1)
    @Column(name = "즐겨찾기번호")
    private int favoritesId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "학번")
    private Student studentNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "게시글번호")
    private Post postId;
}
