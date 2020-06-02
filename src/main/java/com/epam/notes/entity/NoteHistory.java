package com.epam.notes.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "history")
public class NoteHistory {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long idNote;

    @Column
    private String title;

    @Column(length = 5000)
    private String text;

    @Column
    private LocalDateTime date;
}
