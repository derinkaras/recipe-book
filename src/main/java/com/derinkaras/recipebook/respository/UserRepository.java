package com.derinkaras.recipebook.respository;

import com.derinkaras.recipebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
