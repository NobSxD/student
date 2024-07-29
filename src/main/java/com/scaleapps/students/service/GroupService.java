package com.scaleapps.students.service;

import com.scaleapps.students.Entitu.Group;
import com.scaleapps.students.Entitu.Student;

import java.util.List;
import java.util.Optional;

public interface GroupService {
	List<Group> findAll();

	void save(Group group);

	Optional<Group> findById(Long id);
	Student addStudent(Long groupId, Student student);
}
