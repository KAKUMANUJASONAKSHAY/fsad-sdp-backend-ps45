package com.klef.fsad.sdp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.klef.fsad.sdp.entity.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer>
{
    Optional<Faculty> findByEmail(String email);

    Faculty findByUsername(String username);

    boolean existsByEmail(String email);
}
