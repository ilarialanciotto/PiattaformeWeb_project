package org.ilaria.progetto.Repository;

import org.ilaria.progetto.Model.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface  FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO feedback (feedback) VALUES (:feedbackText)", nativeQuery = true)
    void save(@Param("feedbackText") String feedback);

}
