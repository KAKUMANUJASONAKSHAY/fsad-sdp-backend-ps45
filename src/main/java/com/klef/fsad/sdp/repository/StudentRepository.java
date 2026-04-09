package com.klef.fsad.sdp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.klef.fsad.sdp.entity.Student;
import jakarta.transaction.Transactional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer>
{
    Optional<Student> findByEmail(String email);

    Student findByUsername(String username);

    boolean existsByEmail(String email);

    List<Student> findByDepartment(String department);

    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Student getStudentByEmail(String email);

    @Query("SELECT s FROM Student s WHERE s.rollNumber = ?1")
    Student getStudentByRollNumber(String rollNumber);

    @Query("SELECT s FROM Student s WHERE s.name LIKE %?1%")
    List<Student> searchStudentByName(String keyword);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.department = ?1")
    long countByDepartment(String department);

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.password = ?2 WHERE s.email = ?1")
    int updatePasswordByEmail(String email, String password);
}