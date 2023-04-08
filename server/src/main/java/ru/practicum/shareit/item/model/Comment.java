package ru.practicum.shareit.item.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.shareit.user.model.User;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Exclude
    private Item item;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude
    private User author;

    @CreationTimestamp
    private LocalDateTime created;

}
