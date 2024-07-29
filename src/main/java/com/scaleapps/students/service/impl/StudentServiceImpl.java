package com.scaleapps.students.service.impl;

import com.scaleapps.students.DAO.StudentRepository;
import com.scaleapps.students.Entitu.Group;
import com.scaleapps.students.Entitu.Student;
import com.scaleapps.students.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

	private final StudentRepository studentRepository;

	public Student save(Student student) {
		return studentRepository.save(student);
	}

	public List<Student> findAll() {
		return studentRepository.findAll();
	}

	public Optional<Student> findById(Long id) {
		return studentRepository.findById(id);
	}

	public void deleteById(Long id) {
		studentRepository.deleteById(id);
	}

	@Override
	public List<Student> findByTeam(Group group) {
		return studentRepository.findByTeam(group);
	}
}
