package com.shervin.store.users;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Iterable<UserDto> getAllUsers(String sortBy){

        if (!Set.of("name","email").contains(sortBy)){
            sortBy="name";
        }
        return userRepository.findAll(Sort.by(sortBy).ascending()).stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() ->  new UsernameNotFoundException("User not found"));
        return userMapper.toDto(user);
    }


    public UserDto register(UserRegisterDto data) {

        //validate user
        if (userRepository.existsByEmail(data.getEmail())){
            throw new DuplicatedUserException();
        }
        var user = userMapper.toEntity(data);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void deleteUser(Long id) {

        var user = userRepository.findById(id).orElseThrow(() ->  new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
        }

    public void changePassword(Long id, ChangePasswordReqDto request) {
        var user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found"));

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new DeniedAccessException();
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    public UserDto updateUser(Long id, UpdateUserRequestDto request) {

        var user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found"));
        userMapper.update(request, user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}



