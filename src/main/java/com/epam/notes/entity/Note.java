package com.epam.notes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "note"/*, uniqueConstraints = @UniqueConstraint(columnNames = {"id", "dateEdited"})*/)
public class Note implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private boolean deleted;

    @Column(length = 5000)
    private String text;

    @Column
    private LocalDateTime dateCreated;

    @Column
    private LocalDateTime dateEdited;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @JsonIgnore
    @OneToMany(mappedBy = "note")
    private Set<NoteSharing> noteSharing = new HashSet<>();
}
