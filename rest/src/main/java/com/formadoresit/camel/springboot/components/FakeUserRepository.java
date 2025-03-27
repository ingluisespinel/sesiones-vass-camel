package com.formadoresit.camel.springboot.components;

import com.formadoresit.camel.springboot.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Repository
public class FakeUserRepository {
    private List<User> users;
    private Random random;

    public FakeUserRepository(){
        users = new ArrayList<>();
        users.add(User.builder()
                        .id(1)
                        .name("John Doe")
                .build());
        random = new Random();
    }

    public List<User> getUsers(){
        return users;
    }

    public User getUserById(Integer id){
        log.info("Buscando by id {}", id);
        return users.stream()
                .filter(user -> id.equals(user.getId()))
                .findAny().orElse(null);
    }

    public User save(User user){
        user.setId(random.nextInt());
        users.add(user);
        return user;
    }
}
