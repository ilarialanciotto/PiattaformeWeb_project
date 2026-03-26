package org.ilaria.progetto.Service;

import lombok.RequiredArgsConstructor;
import org.ilaria.progetto.Model.DTO.BookingDTO;
import org.ilaria.progetto.Model.Entity.Booking;
import org.ilaria.progetto.Model.Entity.User;
import org.ilaria.progetto.Repository.BookingRepository;
import org.ilaria.progetto.Role;
import org.ilaria.progetto.Service.Mapper.BookingMapper;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ClassroomService classroomService;
    private final BookingMapper bookingMapper;
    private final Utils utils;

    public List<BookingDTO> list(Long id, Role role) {
        List<BookingDTO> list = new LinkedList<>();
        List<Booking> bookinglist = new LinkedList<>();
        if(role== Role.ADMIN) {
            for(Booking booking : bookingRepository.findBookingAdmin(Role.ADMIN))
                if(booking.getClassroom().getPersonInCharge().getId().equals(id))
                    bookinglist.add(booking);
        }
        else bookinglist= bookingRepository.findBookingTeacher(id);
        for(Booking b : bookinglist){
            BookingDTO DTO = bookingMapper.toDto(b);
            DTO.setUserID(b.getUser().getId());
            if(Role.TEACHER == role || b.getUser().getRole().equals(Role.STUDENT))
                DTO.setLaboratorySeats(b.getClassroom().getTotalSeats());
            else DTO.setLaboratorySeats(1);
            DTO.setLaboratoryID(b.getClassroom().getId());
            list.add(DTO);
        }
        return list;
    }

    /* una prenotazione accetta ha accettata pari a true e nel momento in cui viene accetta viene fornito
     un codice per l'ingresso  */

    @Transactional
    public void approveBooking(Booking booking) {
        booking.setApproved(true);
        bookingRepository.accept(booking.getId(), booking.isApproved());
        booking.setCode(new SecureRandom().nextInt(900000) + 100000);
        bookingRepository.update(booking.getCode(), booking.getId());
        if(booking.getUser().getRole().equals(Role.TEACHER)) utils.listBookingClassroom(booking);

    }

    public boolean isBookable(BookingDTO bookingDTO) {
        return classroomService.IsFree(bookingDTO.getLaboratoryID())==0;
    }

    public void reservation(BookingDTO bookingDTO) {
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking.setUser(utils.getUser(bookingDTO.getUserID()));
        booking.setClassroom(classroomService.getClassroom(bookingDTO.getLaboratoryID()));
        if(booking.getBookingDate().minusDays(1).isBefore(LocalDateTime.now()))
            throw new RuntimeException("the classroom must be booked at least one day in advance");
        bookingRepository.save(booking);
        if(booking.getUser().getRole().equals(Role.ADMIN)) utils.listBookingClassroom(booking);
    }

    public Booking getBooking(long bookingID) { return bookingRepository.findById(bookingID); }

    public boolean checkCode(long classroomID, int code, long id) {
        return !bookingRepository.check(classroomID,code,id).isEmpty();
    }

    public boolean existBookingForDate(BookingDTO booking) {
        LocalDateTime start2 = booking.getBookingDate();
        LocalDateTime end2 = start2.plusHours(booking.getDuration().getHour());
        for (Booking b : utils.getBookingsUser(booking.getUserID())) {
            LocalDateTime start1 = b.getBookingDate();
            LocalDateTime end1 = start1.plusHours(b.getDuration().getHour());
            if (!(end1.isBefore(start2) || end1.equals(start2) || end2.isBefore(start1) || end2.equals(start1))) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void delete(long bookingID) {
        Booking b = bookingRepository.findById(bookingID);
        if (b != null) bookingRepository.delete(bookingID);
    }

    public LinkedList<BookingDTO> getBookings(User user) {
        LinkedList<BookingDTO> list = new LinkedList<>();
        for (Booking b : utils.getBookingsUser(user.getId())){
            BookingDTO DTO = bookingMapper.toDto(b);
            DTO.setUserID(b.getUser().getId());
            DTO.setLaboratoryID(b.getClassroom().getId());
            list.add(DTO);
        }
        return list;
    }
}
