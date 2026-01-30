package com.shervin.store.users;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name="Users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestHeader(required = false, name = "Authorization") String authorization,
            @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy) {

            return userService.getAllUsers(sortBy);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {

        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @Valid  @RequestBody UserRegisterDto data,
            UriComponentsBuilder uriBuilder) {

        var userDto = userService.register(data);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public UserDto UpdateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDto request){
        return userService.updateUser(id, request);
    }

    @PostMapping("/{id}/change-password")
    public void changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordReqDto request
            ){
        userService.changePassword(id, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> UserNotFoundExceptionHandler(){
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<Map<String,String>> DuplicatedUserExceptionHandler() {
        return ResponseEntity.badRequest().body(
                Map.of("emaail", "email already exits")
        );
    }
    @ExceptionHandler(DeniedAccessException.class)
    public ResponseEntity<Void> DeniedAccessHandler(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
