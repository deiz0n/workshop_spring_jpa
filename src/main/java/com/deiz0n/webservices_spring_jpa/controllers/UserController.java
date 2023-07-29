package com.deiz0n.webservices_spring_jpa.controllers;

import com.deiz0n.webservices_spring_jpa.dtos.UserDTO;
import com.deiz0n.webservices_spring_jpa.models.User;
import com.deiz0n.webservices_spring_jpa.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserController {


    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userService.getAllResourcers();
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findUserById(@PathVariable UUID id) {
        var user = userService.getResource(id);
        return ResponseEntity.ok().body(user);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody @Valid UserDTO userDTO) {
        var user = new User();
        BeanUtils.copyProperties(userDTO, user);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(userService.createResource(user));
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> remUser(@PathVariable UUID id) {
        userService.removeResource(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody @Valid User user, UserDTO userDTO) {
        user = userService.updateResource(id, user);
        BeanUtils.copyProperties(user, userDTO);
        return ResponseEntity.ok().body(user);
    }

}
