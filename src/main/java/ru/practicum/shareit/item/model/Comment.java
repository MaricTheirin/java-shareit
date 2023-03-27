package ru.practicum.shareit.item.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Length(max = 256)
    private String text;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "author_id")
    private Long authorId;

    @Column
    private LocalDateTime created;

}
