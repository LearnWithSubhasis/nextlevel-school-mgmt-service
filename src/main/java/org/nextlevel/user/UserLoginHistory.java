package org.nextlevel.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_login_history")
public class UserLoginHistory {
    @Id
    @Column(name = "login_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User loggedInUser;

    @Column(name = "last_login_datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
    private Date userLoginDateTime;

    @Column(name = "last_logout_datetime")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
    private Date userLogoutDateTime;

    @Column
    private Long sessionTimeSeconds;

    public User getUser() {
        return loggedInUser;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
