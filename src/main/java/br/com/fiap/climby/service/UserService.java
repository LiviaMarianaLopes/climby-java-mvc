package br.com.fiap.climby.service;

import br.com.fiap.climby.dto.UserRequest;
import br.com.fiap.climby.model.User;
import br.com.fiap.climby.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User salvarUser(UserRequest userRequest) {
        User user = requestToUser(userRequest);
        return userRepository.save(user);
    }

    public void atualizarUser(Long id, UserRequest userRequest) {
        User user = buscarUser(id);
        if (user != null) {
            BeanUtils.copyProperties(userRequest, user);
            userRepository.save(user);
        }
    }

    public void deletarUser(Long id) {
        userRepository.deleteById(id);
    }

    public User requestToUser(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setCountry(userRequest.getCountry());
        user.setCity(userRequest.getCity());
        return user;
    }

    public UserRequest userToRequest(User user) {
        UserRequest userRequest = new UserRequest();
        BeanUtils.copyProperties(user, userRequest);
        return userRequest;
    }

    public List<User> buscarUsers() {
        return userRepository.findAll();
    }

    public User buscarUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

}