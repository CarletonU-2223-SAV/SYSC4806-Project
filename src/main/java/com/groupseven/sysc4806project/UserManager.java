package com.groupseven.sysc4806project;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.io.Serializable;
import java.util.*;

@Entity
public class UserManager implements Serializable {
    @OneToMany
    private List<User> users_list;

    public UserManager() {
        users_list = new ArrayList<>();
    }
    @Id
    @GeneratedValue
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
