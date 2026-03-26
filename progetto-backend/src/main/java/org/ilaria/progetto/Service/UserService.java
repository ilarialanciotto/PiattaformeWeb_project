package org.ilaria.progetto.Service;

import lombok.RequiredArgsConstructor;
import org.ilaria.progetto.Model.DTO.ModifyDTO;
import org.ilaria.progetto.Model.DTO.TeacherDTO;
import org.ilaria.progetto.Model.Entity.User;
import org.ilaria.progetto.Repository.BookingRepository;
import org.ilaria.progetto.Repository.CodeRepository;
import org.ilaria.progetto.Role;
import org.ilaria.progetto.Security.JwtResponse;
import org.ilaria.progetto.Model.DTO.UserDTO;
import org.ilaria.progetto.Repository.UserRepository;
import org.ilaria.progetto.Service.Mapper.UserMapper;
import org.ilaria.progetto.Security.JwtUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CodeRepository codeRepository;
    private final Utils utils;
    private final CacheManager cacheManager;

    public void register(UserDTO dto) {
        User user = userMapper.toEntity(dto);
        if (utils.findUserRegister(dto.getEmail())!=null) { throw new RuntimeException("Email already registered"); }
        if(codeRepository.findByCode(dto.getTeacherCode())!=null) user.setRole(Role.TEACHER);
        else if (dto.getTeacherCode()==null || dto.getTeacherCode().equals(""))user.setRole(Role.STUDENT);
        else throw new RuntimeException("teacher code does not exist");
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if(!StringUtils.hasText(dto.getPassword()) || !dto.getEmail().matches(regex))
            throw new RuntimeException("Password or email format incorrect");
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        if(user.getRole()==Role.TEACHER) {
            utils.teacherCache(user);
        }
    }

    public ResponseEntity<?> login(UserDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userPrincipal);
        String ruolo = userPrincipal.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("NO_ROLE");
        User user = utils.findUser(dto.getEmail());
        long enterClassroomID = user.getClassroomIDin();
        utils.bookingCheck();
        return ResponseEntity.ok(new JwtResponse(jwt,userPrincipal.getUsername(), ruolo, enterClassroomID));
    }

    @CacheEvict(value = "users", key = "#user.email")
    @Transactional
    public void update(Long id, ModifyDTO userDTO, User user) {
        if (!StringUtils.hasText(userDTO.getEmail())) userDTO.setEmail(user.getEmail());
        if (!StringUtils.hasText(userDTO.getPassword())) userDTO.setPassword(user.getPassword());
        else userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if(!userDTO.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) throw new RuntimeException("Email already exists");
        userRepository.updateDates(id,userDTO.getEmail(),userDTO.getPassword());
    }


    @Cacheable(value = "teacher")
    public List<TeacherDTO> getTeacher() {
        LinkedList<TeacherDTO> teacherDTOList = new LinkedList<>();
        for(User teacher : userRepository.findTeacher(Role.TEACHER)) {
            TeacherDTO teacherDTO = new TeacherDTO(teacher.getId(),teacher.getName(),teacher.getEmail());
            teacherDTOList.add(teacherDTO);
        }
        return teacherDTOList;
    }
}

