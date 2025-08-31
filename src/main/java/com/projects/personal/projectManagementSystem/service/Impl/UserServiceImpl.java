package com.projects.personal.projectManagementSystem.service.Impl;

import com.projects.personal.projectManagementSystem.entity.User;
import com.projects.personal.projectManagementSystem.repository.UserRepository;
import com.projects.personal.projectManagementSystem.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
