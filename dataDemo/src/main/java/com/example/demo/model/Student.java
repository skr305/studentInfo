package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

//import org.springframework.context.annotation.Bean;
//
//@Bean
public class Student implements UserDetails {
    private String name;
    private String id;
    private String polit;
    private String unit;
    private String socia;
    @JsonIgnore
    private String password;




    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPolit() {
        return polit;
    }

    public String getSocia() {
        return socia;
    }

    public String getUnit() {
        return unit;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPolit(String polit) {
        this.polit = polit;
    }

    public void setSocia(String socia) {
        this.socia = socia;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}
