package com.epam.notes.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
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
