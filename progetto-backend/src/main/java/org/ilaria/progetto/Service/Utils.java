package org.ilaria.progetto.Service;

import lombok.AllArgsConstructor;
import org.ilaria.progetto.Model.DTO.ClassroomBookingDTO;
import org.ilaria.progetto.Model.DTO.TeacherDTO;
import org.ilaria.progetto.Model.Entity.Booking;
import org.ilaria.progetto.Model.Entity.Classroom;
import org.ilaria.progetto.Model.Entity.Content;
import org.ilaria.progetto.Model.Entity.User;
import org.ilaria.progetto.Repository.BookingRepository;
import org.ilaria.progetto.Repository.ContentRepository;
import org.ilaria.progetto.Repository.UserRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class Utils {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ContentRepository contentRepository;
    private final CacheManager cacheManager;

    @Cacheable(value = "users" , key="#email")
    public User findUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void bookingCheck() {
        LocalDateTime now = LocalDateTime.now();
        bookingRepository.deleteExpired(now);
    }

    public User getUser(long userID) {
        return userRepository.findById(userID); }

    public List<Booking> getBookingsUser(long id) {
        return bookingRepository.findByUser(id);
    }

    public List<Booking> getBookings(long id) {
        return bookingRepository.findByClassroom(id);
    }

    @Cacheable(value = "contents" , key="#c.id")
    public Collection<Content> getContents(Classroom c) {
        return contentRepository.findContent(c.getId());
    }

    public User findUserRegister(String email) {
        return userRepository.findByEmail(email);
    }

    public void teacherCache(User user) {
        List<TeacherDTO> teacherList = cacheManager.getCache("teacher").get("teacher", List.class);
        if(teacherList==null) teacherList = new LinkedList<>();
        teacherList.add(new TeacherDTO(user.getId(), user.getName(), user.getEmail()));
        cacheManager.getCache("teacher").put("teacher", teacherList);
    }

    public void listBookingClassroom(Booking booking) {
        List<ClassroomBookingDTO> bookingDTOList = cacheManager.getCache("booking").get(booking.getClassroom().getId(), List.class);
        if(bookingDTOList==null) bookingDTOList = new LinkedList<>();
        bookingDTOList.add(new ClassroomBookingDTO(booking.getId(),booking.getBookingDate().toLocalDate(),booking.getBookingDate().toLocalTime(),booking.getDuration()));
        cacheManager.getCache("booking").put(booking.getClassroom().getId(),bookingDTOList);
    }
}
