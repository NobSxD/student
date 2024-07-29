package com.scaleapps.students.service;

import com.scaleapps.students.Entitu.Group;
import com.scaleapps.students.Entitu.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

	Student save(Student student);

	List<Student> findAll();
	Optional<Student> findById(Long id);
	void deleteById(Long id);
	List<Student> findByTeam(Group group);
}
