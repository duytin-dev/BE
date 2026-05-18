package com.ndt.ktpm.Demo.Controller;

import com.ndt.ktpm.Demo.Domain.Response.UserResponse;
import com.ndt.ktpm.Demo.Domain.User;
import com.ndt.ktpm.Demo.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String heelo(){
        return "String helllo tin ";
    }
    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody User user) {
        log.info("Create user successfully");
        UserResponse resUser = this.userService.convertUser(this.userService.handleCreateUser(user));
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(resUser);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponse> updateUser( @PathVariable("id") long id ,@RequestBody User user) {
        log.info("Update user successfully");
        UserResponse resUser = this.userService.convertUser(this.userService.handleUpdateUser(id,user));
        return ResponseEntity.status(HttpStatus.ACCEPTED.value()).body(resUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUser() {
        log.info("Get all user successfully");
        List<UserResponse> lstUser = this.userService.handleGetAllUser()
                .stream()
                .map(user -> this.userService.convertUser(user))
                .toList();

        return ResponseEntity.status(HttpStatus.OK.value()).body(lstUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        log.info("Delete user successfully");
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK.value()).body("Delete user successfully");
    }

    @PostMapping("/")
    public String test() {
        return "Hello World";
    }

}
