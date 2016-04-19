package com.restaurante.persistence;


import javax.persistence.*;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by alex on 29.03.16.
 */
@Entity
@Table(name="users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "userType", discriminatorType = DiscriminatorType.STRING)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="mail")
    private String mail;

    @Column(name="login")
    private String username;

    @Column(name="password")
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<UserRole> roles;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return username;
    }

    public void setLogin(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }
}
