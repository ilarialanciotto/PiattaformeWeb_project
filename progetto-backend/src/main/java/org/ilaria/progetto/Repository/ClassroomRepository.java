package org.ilaria.progetto.Repository;

import jakarta.persistence.LockModeType;
import org.ilaria.progetto.Model.Entity.Classroom;
import org.ilaria.progetto.Model.Entity.Content;
import org.ilaria.progetto.Model.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long>{

    @Query("SELECT C FROM Classroom C WHERE C.id=:id")
    Classroom findById(@Param("id") long id);

    @Query("SELECT C FROM Classroom C WHERE C.numberOfSeats>0 ORDER BY C.id ASC")
    LinkedList<Classroom> findFree();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Classroom c WHERE c.id = :id")
    Classroom findByIdWithLock(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Classroom c SET c.personInCharge=:user WHERE c.id = :id")
    void personInCharge(@Param("id") long id, @Param("user") User user);

    @Modifying
    @Query("UPDATE Classroom c SET c.numberOfSeats=:numberOfSeats WHERE c.id = :id")
    void update(@Param("id") long id, @Param("numberOfSeats") int numberOfSeats);

    Optional<Classroom> findByCube(String cube);

}
