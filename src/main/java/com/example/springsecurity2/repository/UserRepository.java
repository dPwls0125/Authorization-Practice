package com.example.springsecurity2.repository;

import com.example.springsecurity2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


//<타입,id 타입>
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName); // userName이 있으면 optional 안에 값이 들어옴
    // Optional<T>는 null이 올 수 있는 값을 감싸는 wrapper 클래스로, 참조하더라도 NPE가 발생하지 않도록 도와준다. 클래스이기 때문에 각종 메서드를 제공
}
