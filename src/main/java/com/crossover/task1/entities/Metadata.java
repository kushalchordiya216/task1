package com.crossover.task1.entities;

import javax.persistence.*;

@Entity
/*
* Entity class for handling database operation on storing file metadata into a database
* Uses Hibernate annotation to define SQL data constraints, such unique and non nullable fields
* */
public class Metadata {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "objKey", unique = true, nullable = false)
    private String objKey;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "filetype")
    private String filetype;
    @Column(name = "size")
    private Long size;

    public Metadata(String objKey, String description, String filetype, Long size) {
        this.objKey = objKey;
        this.description = description;
        this.filetype = filetype;
        this.size = size;
    }

    public Metadata() {}
}

