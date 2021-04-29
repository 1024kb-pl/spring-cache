package kb.pl.springcache.service;

import kb.pl.springcache.repo.User;
import kb.pl.springcache.repo.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Optional;

@Service
@Log4j2
public class UserService {

    public final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "user")
    public User getUser(Long id){
        log.info("Find user method.");
        Assert.notNull(id, "Parameter 'id' can not be empty.");
        Optional<User> opt = repository.findById(id);
        return opt.isPresent() ? opt.get() : null;
    }

    @Cacheable(value = "users")
    public Collection<User> list(){
        log.info("Find all users list method.");
        return this.repository.findAll();
    }

    @Caching(
            evict = @CacheEvict(value = "users"),
            put = @CachePut(value = "user", key = "#user.id")
    )
    public User save(User user) {
        log.info("Save user method.");
        Assert.notNull(user, "Parameter 'user' can not be empty.");
        Assert.notNull(user.getFirstName(), "Parameter 'user.firstName' can not be empty.");
        Assert.notNull(user.getLastName(), "Parameter 'user.lastName' can not be empty.");
        Assert.notNull(user.getEmail(), "Parameter 'user.email' can not be empty.");
        return this.repository.save(user);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "user", key = "#user.id"),
                    @CacheEvict(value = "users")
            }
    )
    public void deleteUser(Long id) {
        log.info("Delete user method.");
        Assert.notNull(id, "Parameter 'id' can not be empty.");
        repository.deleteById(id);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "users"),
                    @CacheEvict(value = "user", key = "#user.id")
            }
    )
    public User modify(User user) {
        log.info("Modify user method.");
        Assert.notNull(user, "Parameter 'user' can not be empty.");
        Assert.notNull(user.getFirstName(), "Parameter 'user.firstName' can not be empty.");
        Assert.notNull(user.getLastName(), "Parameter 'user.lastName' can not be empty.");
        Assert.notNull(user.getEmail(), "Parameter 'user.email' can not be empty.");
        Optional<User> opt  = this.repository.findById(user.getId());
        if(!opt.isPresent()){
            throw new IllegalArgumentException("Specified user not exist.");
        }
        User dbUser = opt.get();
        dbUser.setLastName(user.getLastName());
        dbUser.setFirstName(user.getFirstName());
        dbUser.setEmail(user.getEmail());
        this.repository.save(dbUser);
        return dbUser;
    }

}