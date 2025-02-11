package org.example.Controller;

import org.example.Dto.ResponseDto;
import org.example.Entity.UsersEntity;
import org.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create-update")
    public ResponseEntity<ResponseDto> createUser(@RequestBody UsersEntity user) {
        return userService.createUpdateUserRepo(user);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ResponseDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<ResponseDto> deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }
}
