package com.scaleapps.students.Conttroler;

import com.scaleapps.students.Entitu.Group;
import com.scaleapps.students.Entitu.Student;
import com.scaleapps.students.service.GroupService;
import com.scaleapps.students.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupRestController {
	private final GroupService groupService;
	private final StudentService studentService;

	@GetMapping("/groups")
	public ResponseEntity<List<Group>> getGroups() {
		List<Group> groups = groupService.findAll().stream()
				.sorted((g1, g2) -> g2.getDateAdded().compareTo(g1.getDateAdded()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(groups);
	}

	@GetMapping("/groups/{id}")
	public ResponseEntity<?> getGroup(@PathVariable("id") Long id) {
		Optional<Group> group = groupService.findById(id);
		if (group.isPresent()) {
			Map<String, Object> response = new HashMap<>();
			List<Student> students = group.get().getStudents().stream()
					.sorted((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName()))
					.collect(Collectors.toList());
			response.put("group", group.get());
			response.put("students", students);
			response.put("studentByTeam", studentService.findByTeam(group.get()));
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
		}
	}

	@PostMapping("/groups")
	public ResponseEntity<Group> addGroup(@RequestBody Map<String, String> requestBody) {
		Group group = new Group();
		group.setNumber(requestBody.get("number"));
		group.setDateAdded(LocalDate.now());
		groupService.save(group);
		return ResponseEntity.status(HttpStatus.CREATED).body(group);
	}

	@PostMapping("/groups/{id}/students")
	public ResponseEntity<?> addStudent(@PathVariable("id") Long groupId, @RequestBody Map<String, String> name) {
		Map<String, Object> response = new HashMap<>();
		Student student = new Student();
		student.setFullName(name.get("name"));
		student.setCreateDate(LocalDate.now());
		try {
			groupService.addStudent(groupId, student);
			response.put("success", true);
			response.put("message", "Student added successfully");
			response.put("data", student);
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding student");
		}
	}

	@DeleteMapping("/groups/{groupId}/students/{studentId}")
	public ResponseEntity<?> deleteStudent(@PathVariable Long groupId, @PathVariable Long studentId) {
		Map<String, Object> response = new HashMap<>();
		Group group = groupService.findById(groupId)
				.orElseThrow(() -> new RuntimeException("Group not found"));

		Student student = studentService.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));

		group.getStudents().remove(student);
		studentService.deleteById(student.getId());

		response.put("success", true);
		response.put("message", "Student deleted successfully");
		return ResponseEntity.ok(response);
	}
}
