package com.scaleapps.students.service.impl;

import com.scaleapps.students.DAO.GroupRepository;
import com.scaleapps.students.Entitu.Group;
import com.scaleapps.students.Entitu.Student;
import com.scaleapps.students.service.GroupService;
import com.scaleapps.students.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
	private final GroupRepository groupRepository;
	private final StudentService studentService;
	@Override
	public List<Group> findAll() {
		return groupRepository.findAll();
	}

	@Override
	public void save(Group group) {
		groupRepository.save(group);
	}

	@Override
	public Optional<Group> findById(Long id) {
		return groupRepository.findById(id);
	}

	public Student addStudent(Long groupId, Student student) {
		Optional<Group> groupOpt = groupRepository.findById(groupId);
		if (groupOpt.isPresent()) {
			Group group = groupOpt.get();
			group.getStudents().add(student);

			student.setTeam(group);
			studentService.save(student);
			groupRepository.save(group);
			return student;
		} else {
			throw new RuntimeException("Group not found");
		}
	}
}
