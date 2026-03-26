package org.ilaria.progetto.Controller;

import lombok.AllArgsConstructor;
import org.ilaria.progetto.Model.DTO.AssignmentRequestDTO;
import org.ilaria.progetto.Model.DTO.BookingDTO;
import org.ilaria.progetto.Model.DTO.TeacherDTO;
import org.ilaria.progetto.Model.Entity.Booking;
import org.ilaria.progetto.Model.Entity.User;
import org.ilaria.progetto.Service.ClassroomService;
import org.ilaria.progetto.Service.BookingService;
import org.ilaria.progetto.Service.UserService;
import org.ilaria.progetto.Service.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/FreeClassroom/home/teacher_and_admin")
@AllArgsConstructor
public class Admin_TeacherController {

    private final BookingService bookingService;
    private final ClassroomService classroomService;
    private final Utils utils;
    private final UserService userService;

    /* un docente ha la lista delle prenotazioni dei laboratori di cui è responsabile e che non
     ha ancora approvato, prima viene fatto un controllo per eliminare le prenotazioni scadute*/

    @PreAuthorize("hasRole('TEACHER') or  hasRole('ADMIN')")
    @GetMapping("/getList")
    public ResponseEntity<List<BookingDTO>> bookingList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User teacher = utils.findUser(email);
        return ResponseEntity.ok(bookingService.list(teacher.getId(),teacher.getRole()));
    }

    /* un docente accetta la prenotazione */

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping("/approveBooking")
    public ResponseEntity<String> bookingAccept(@RequestBody long bookingID) {
        Booking booking = bookingService.getBooking(bookingID);
        bookingService.approveBooking(booking);
        return ResponseEntity.ok("Booking approved successfully");
    }

    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @PostMapping("/getBookingClassroom")
    public ResponseEntity<LinkedList<BookingDTO>> bookingsClassroom(@RequestBody long classroomID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User teacher = utils.findUser(email);
        return (ResponseEntity.ok(classroomService.getBookings(classroomID,teacher.getId())));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/Admin/personInCharge")
    public ResponseEntity<String> personInCharge(@RequestBody AssignmentRequestDTO request) {
        User teacher = utils.getUser(request.getTeacherID());
        classroomService.personInCharge(request.getClassroomID(), teacher);
        return ResponseEntity.ok("Responsibility assigned successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/Admin/getTeacher")
    public ResponseEntity<List<TeacherDTO>> classroomList() {
        return ResponseEntity.ok(userService.getTeacher());
    }

}
