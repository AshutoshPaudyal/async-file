package com.async.async.Demo.repo;

import com.async.async.Demo.model.Random;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomRepo extends JpaRepository<Random,Integer> {
}
