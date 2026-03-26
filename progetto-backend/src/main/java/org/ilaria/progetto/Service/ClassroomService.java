package org.ilaria.progetto.Service;

import lombok.RequiredArgsConstructor;
import org.ilaria.progetto.Model.DTO.BookingDTO;
import org.ilaria.progetto.Model.DTO.ClassroomDTO;
import org.ilaria.progetto.Model.DTO.ContentDTO;
import org.ilaria.progetto.Model.DTO.ClassroomBookingDTO;
import org.ilaria.progetto.Model.Entity.Classroom;
import org.ilaria.progetto.Model.Entity.Booking;
import org.ilaria.progetto.Model.Entity.User;
import org.ilaria.progetto.Repository.ClassroomRepository;
import org.ilaria.progetto.Repository.BookingRepository;
import org.ilaria.progetto.Repository.UserRepository;
import org.ilaria.progetto.Role;
import org.ilaria.progetto.Service.Mapper.BookingMapper;
import org.ilaria.progetto.Service.Mapper.ClassroomMapper;
import org.ilaria.progetto.Service.Mapper.ContentMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final ClassroomMapper classroomMapper;
    private final BookingRepository bookingRepository;
    private final ContentMapper contentMapper;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;
    private final Utils utils;
    private final CaffeineCacheManager cacheManager;

    /* un aula è libera se ha posti al momento della richiesta di lista, se non è stata prenotata per quel tempo da un
     professore. */

    private LinkedList<Classroom> free(){
        LocalDateTime now = LocalDateTime.now();
        LinkedList<Classroom> listClassroom = classroomRepository.findFree();
        LinkedList<Classroom> listClassroomCopy = (LinkedList<Classroom>) listClassroom.clone();
        for (Classroom classroom : listClassroom) {
            List<Booking> bookingList = utils.getBookings(classroom.getId());
            for (Booking b : bookingList) {
                LocalDateTime start = b.getBookingDate();
                LocalDateTime end = start.plusHours(b.getDuration().getHour());
                if (now.toLocalDate().isEqual(start.toLocalDate()))
                    if (now.isAfter(start) && now.isBefore(end)) {
                        if(b.isApproved() && b.getUser().getRole().equals(Role.TEACHER)
                                || b.getUser().getRole().equals(Role.ADMIN)
                        || b.getClassroom().getPersonInCharge().getRole().equals(Role.TEACHER) )
                            listClassroomCopy.remove(classroom);
                    }
            }
        }
        return listClassroomCopy;
    }

    public Page<ClassroomDTO> list(int page, int size) {
        List<Classroom> allFreeClassrooms = free();
        int start = page * size;
        int end = Math.min(start + size, allFreeClassrooms.size());
        if (start >= allFreeClassrooms.size()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), allFreeClassrooms.size());
        }
        List<Classroom> paginatedClassrooms = allFreeClassrooms.subList(start, end);
        List<ClassroomDTO> dtoList = paginatedClassrooms.stream().map(c -> {
            ClassroomDTO dto = classroomMapper.toDto(c);
            if (c.isLaboratory()) {
                dto.setPersonInCharge(c.getPersonInCharge().getEmail());
            }
            List<ContentDTO> contents = utils.getContents(c).stream()
                    .map(contentMapper::toDto)
                    .collect(Collectors.toList());
            LinkedList<ContentDTO> linkedContents = new LinkedList<>(contents);
            dto.setContents(linkedContents);
            return dto;
        }).collect(Collectors.toList());
        return new PageImpl<>(dtoList, PageRequest.of(page, size), allFreeClassrooms.size());
    }

    public int IsFree(long classroomID) {
        Classroom classroom = classroomRepository.findById(classroomID);
        if(!free().contains(classroom)) return -1;
        return 0;
    }

    /* se uno studente entra in un aula decrementa il numero di posti disponibili */

   @CacheEvict(value = "users", key = "#user.email")
    @Transactional
    public void enter(User user, ClassroomDTO classroom) {
        Classroom c = classroomRepository.findByIdWithLock(classroom.getId());
        if(c.isLaboratory()) {
            for(Booking b :utils.getBookings(c.getId())) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime start = b.getBookingDate();
                if(b.getUser().getId().equals(user.getId()))
                    if(now.isBefore(start)) throw new RuntimeException("it's not the booking time");
            }
        }
        userRepository.update(user.getId(),c.getId());
        classroomRepository.update(c.getId(),c.getNumberOfSeats() - 1);
    }

    /* se uno studente esce da un aula aumenta il numero di posti disponibili */

    @CacheEvict(value = "users", key = "#user.email")
    @Transactional
    public void exit(User user, long classroomID) {
        ClassroomDTO classroom = getClassroomDTO(classroomID);
        Classroom c = classroomMapper.toEntity(classroom);
        if(c.getId()!= user.getClassroomIDin()) throw new RuntimeException("you can't leave a different classroom");
        if(c.isLaboratory()) {
            bookingRepository.delete(classroom.getId(), user.getId());
        }
        userRepository.update(user.getId(),-1);
        classroomRepository.update(c.getId(),classroom.getNumberOfSeats() + 1);
    }

    public Classroom getClassroom(long classroomID) {
        return classroomRepository.findById(classroomID);
    }

    public ClassroomDTO getClassroomDTO(long classroomID) {
        return classroomMapper.toDto(classroomRepository.findById(classroomID));
    }

    @Cacheable(value = "booking", key = "#classroomID")
    @Transactional
    public LinkedList<ClassroomBookingDTO> classroomsBooking(long classroomID) {
        LinkedList<ClassroomBookingDTO> list = new LinkedList<>();
        for (Booking b : utils.getBookings(classroomID)){
            ClassroomBookingDTO classroomsBooking = new ClassroomBookingDTO(b.getId() , b.getBookingDate().toLocalDate(),
                    b.getBookingDate().toLocalTime(), b.getDuration());
            if(b.getUser().getRole().equals(Role.TEACHER) && b.isApproved())  list.add(classroomsBooking);
            if(b.getUser().getRole().equals(Role.ADMIN))  list.add(classroomsBooking);
            if(b.getUser().getRole().equals(Role.TEACHER) &&
                    b.getClassroom().getPersonInCharge().getRole().equals(Role.TEACHER))
                list.add(classroomsBooking);
        }
        return list;
    }

    @Transactional
    public void personInCharge(long idAula, User teacher) {
        classroomRepository.personInCharge(idAula, teacher);
    }

    public LinkedList<BookingDTO> getBookings(long classroomID,long userID) {
        LinkedList<BookingDTO> listBooking = new LinkedList<>();
        Classroom c = classroomRepository.findById(classroomID);
        if(c.getPersonInCharge().getId().equals(userID))
            for(Booking b : utils.getBookings(c.getId())) {
                BookingDTO bookingDTO = bookingMapper.toDto(b);
                bookingDTO.setLaboratoryID(b.getClassroom().getId());
                if(b.getUser().getRole().equals(Role.STUDENT)) bookingDTO.setLaboratorySeats(b.getClassroom().getTotalSeats());
                if(b.isApproved()) listBooking.add(bookingDTO);
            }
        return listBooking;
    }
}
