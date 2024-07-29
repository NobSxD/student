package com.scaleapps.students.DAO;

import com.scaleapps.students.Entitu.Group;
import com.scaleapps.students.Entitu.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
	List<Student> findByTeam(Group group);
}
