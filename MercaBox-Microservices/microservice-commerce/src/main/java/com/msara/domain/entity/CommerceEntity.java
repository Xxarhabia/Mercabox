package com.msara.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "commerce")
@Builder
public class CommerceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;

    @Column(name = "contact_list")
    private List<String> contactList;

    @Column(name = "type_of_establishment")
    private String typeOfEstablishment;

    @Column(name = "owner_id")
    private Long ownerId;

}
