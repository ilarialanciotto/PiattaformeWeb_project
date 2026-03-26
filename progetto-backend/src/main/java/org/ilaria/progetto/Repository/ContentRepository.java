package org.ilaria.progetto.Repository;

import org.ilaria.progetto.Model.Entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    @Query("SELECT c FROM Content c WHERE c.classroom.id=:id ORDER BY c.id ASC")
    Collection<Content> findContent(Long id);
}
