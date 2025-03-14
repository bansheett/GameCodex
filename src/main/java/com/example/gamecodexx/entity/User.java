package com.example.gamecodexx.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "utente")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String email;
    private String password;
    private int attempts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public User(Long id, String nickname, String email, String password, int attempts) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.attempts = attempts;
    }
    public User() {}
}
