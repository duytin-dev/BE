package com.ndt.ktpm.Demo.Repository;

import com.ndt.ktpm.Demo.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
