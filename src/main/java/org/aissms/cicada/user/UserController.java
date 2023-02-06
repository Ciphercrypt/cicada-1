package org.aissms.cicada.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired UserRepository repository;
    
    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return repository.findByEmail(email);
    }
    @GetMapping("/")
    public String getNull(Authentication auth) {
        return auth.getName();
    }
}