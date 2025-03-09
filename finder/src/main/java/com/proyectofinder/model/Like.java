package com.proyectofinder.model;

import jakarta.persistence.*;

/**
 * Entidad que representa la acción de like o dislike de un usuario hacia otro.
 */
@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_from_id")
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "user_to_id")
    private User userTo;

    private boolean liked;

    public Like() {}

    public Like(User userFrom, User userTo, boolean liked) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.liked = liked;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUserFrom() { return userFrom; }
    public void setUserFrom(User userFrom) { this.userFrom = userFrom; }

    public User getUserTo() { return userTo; }
    public void setUserTo(User userTo) { this.userTo = userTo; }

    public boolean isLiked() { return liked; }
    public void setLiked(boolean liked) { this.liked = liked; }
}
