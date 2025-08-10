package com.nova.Nova_Link;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.spring

@Entity
@Getter
@Setter
public class User {
    private Long id;
    private String name;
    private String email;
    private String contactNumber;


    public User(){
        this.id = 0;
        this.name = "";
        this.email = "";
        this.contactNumber = "";
    }
    public User (Long id, String name, String email, String contactNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
    }


}