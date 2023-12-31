package ru.practicum.main.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class UserAdminController {
    private final UserService userService;

    @ResponseStatus(CREATED)
    @PostMapping("/users")
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(defaultValue = "10") Integer size,
                                  @RequestParam(defaultValue = "0") Integer from) {
        return userService.getUsers(ids, from, size);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
