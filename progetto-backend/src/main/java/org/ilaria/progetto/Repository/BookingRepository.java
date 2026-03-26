package org.ilaria.progetto.Repository;

import org.ilaria.progetto.Model.Entity.Booking;
import org.ilaria.progetto.Model.Entity.Classroom;
import org.ilaria.progetto.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Modifying
    @Query("DELETE Booking b WHERE b.classroom.id=:classroomID and b.user.id=:userID")
    void delete(@Param("classroomID") Long classroomID,@Param("userID") Long userID);

    @Query("SELECT B FROM Booking B WHERE B.id=:id")
    Booking findById(@Param("id") long id);

    @Query("SELECT B FROM Booking B WHERE B.classroom.personInCharge.id=:id and B.approved=false ORDER BY B.id ASC")
    LinkedList<Booking> findBookingTeacher(@Param("id") Long id);

    @Query("SELECT B FROM Booking B WHERE B.approved=false and B.user.role!=:role ORDER BY B.id ASC")
    LinkedList<Booking> findBookingAdmin( @Param("role") Role role);

    @Modifying
    @Query("UPDATE Booking b SET b.approved = :approved  WHERE b.id = :id")
    void accept(@Param("id") Long id, @Param("approved") boolean approved);

    @Modifying
    @Query("UPDATE Booking b SET b.code=:code WHERE b.id = :id")
    void update(@Param("code") int code,@Param("id") long id);

    @Query("SELECT p FROM Booking p WHERE p.code=:code and p.classroom.id=:classroomID and p.user.id=:id ORDER BY p.id ASC")
    LinkedList<Booking> check(@Param("classroomID")long classroomID, @Param("code")int code, @Param("id")Long id);

    @Modifying
    @Query("DELETE Booking b WHERE b.id=:bookingID")
    void delete(@Param("bookingID")long bookingID);

    @Query("SELECT b FROM Booking b WHERE b.user.id=:id  ORDER BY b.id ASC")
    List<Booking> findByUser(@Param("id")long id);

    @Query("SELECT b FROM Booking b WHERE b.classroom.id=:id  ORDER BY b.id ASC")
    LinkedList<Booking> findByClassroom(@Param("id")long id);

    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM bookings 
    WHERE booking_date + duration< :now
""", nativeQuery = true)
    void deleteExpired(@Param("now") LocalDateTime now);

}
