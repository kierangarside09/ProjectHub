package com.projecthub.entities;

import com.projecthub.enums.Gender;
import com.projecthub.enums.LookingFor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Profile {

    @Id
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(length = 30)
    private String displayName;

    private String location;
    @Column(length = 1000)
    private String bio;

    //commenting this out for now as I have no idea how we implement this
    //private fileOfSomeKind avatar;

    //ill probably change this later so skill are actually comparable values
    @Column(length = 200)
    private String skills;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private LookingFor lookingFor;

}
