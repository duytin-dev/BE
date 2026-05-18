package com.ndt.ktpm.Demo.Service;

import com.ndt.ktpm.Demo.Domain.Response.UserResponse;
import com.ndt.ktpm.Demo.Domain.User;

import java.util.List;

public interface UserService {
    User handleCreateUser(User user);

    User handleUpdateUser(long id ,User userUpd);

    List<User> handleGetAllUser() ;

    void handleDeleteUser(Long id);

    User getUserById(long id);

    UserResponse convertUser(User user);

}
