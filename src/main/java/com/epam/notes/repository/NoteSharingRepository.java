package com.epam.notes.repository;

import com.epam.notes.entity.NoteSharing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteSharingRepository extends JpaRepository<NoteSharing, Long> {
}
