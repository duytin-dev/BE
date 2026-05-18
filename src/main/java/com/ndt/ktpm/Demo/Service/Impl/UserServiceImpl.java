package com.ndt.ktpm.Demo.Service.Impl;

import com.ndt.ktpm.Demo.Domain.Response.UserResponse;
import com.ndt.ktpm.Demo.Domain.User;
import com.ndt.ktpm.Demo.Repository.UserRepository;
import com.ndt.ktpm.Demo.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User handleCreateUser(User user) {
        return this.userRepository.save(user);

    }

    @Override
    public User handleUpdateUser(long id ,User userUpd) {
        User userDb = this.getUserById(id);
        if(userDb != null){
            userDb.setName(userUpd.getName());
            userDb.setAddress(userUpd.getAddress());
            userDb.setEmail(userUpd.getEmail());
            return this.userRepository.save(userDb);
        }
        return null;
    }

    @Override
    public List<User> handleGetAllUser() {
     return this.userRepository.findAll();
    }

    @Override
    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);

    }

    @Override
    public User getUserById(long id) {
       Optional<User>opUser = this.userRepository.findById(id);
       return opUser.orElse(null);
    }

    @Override
    public UserResponse convertUser(User user) {
     UserResponse resUser = new UserResponse();
     resUser.setId(user.getId());
     resUser.setName(user.getName());
     resUser.setEmail(user.getEmail());
     resUser.setAddress(user.getAddress());
   return resUser;
    }
}
