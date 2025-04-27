package com.codegym.airline_tickets.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "news")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @Column(name = "content")
    @NotBlank(message = "Nội dung không được để trống")
    private String content;

    @Column(name = "imageURL")
    private String imageURL;

}
