package kb.pl.springcache.controller;

import kb.pl.springcache.repo.User;
import kb.pl.springcache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/new")
    public @ResponseBody User save(@RequestBody User user){
        return this.userService.save(user);
    }

    @GetMapping("/list")
    public @ResponseBody Collection<User> list(){
        return this.userService.list();
    }

    @GetMapping("/details")
    public @ResponseBody User findById(@RequestParam("id") Long id){
        return this.userService.getUser(id);
    }

    @DeleteMapping("/delete")
    public @ResponseBody void delete(@RequestParam("id") Long id){
        this.userService.deleteUser(id);
    }
}
