package com.scaleapps.students.Conttroler;

import com.scaleapps.students.Entitu.Group;
import com.scaleapps.students.Entitu.Student;
import com.scaleapps.students.service.GroupService;
import com.scaleapps.students.service.StudentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@Controller
@RequiredArgsConstructor
public class GroupController {
	private final GroupService groupService;
	private final StudentService studentService;

	@GetMapping("/groups")
	public String getGroups(Model model) {
		model.addAttribute("groups", groupService.findAll().stream()
				.sorted((g1, g2) -> g2.getDateAdded().compareTo(g1.getDateAdded()))
				.collect(Collectors.toList()));
		return "groups";
	}

	@GetMapping("/group")
	public String getGroup(@RequestParam("id") Long id, Model model) {
		Optional<Group> group = groupService.findById(id);
		if (group.isPresent()) {
			model.addAttribute("students", group.get().getStudents().stream()
					.sorted((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName()))
					.collect(Collectors.toList()));
			List<Student> byTeam = studentService.findByTeam(group.get());
			model.addAttribute("group", group.get());
			model.addAttribute("student", byTeam);
			return "group";
		} else {
			model.addAttribute("error", "Group not found");
			return "error";
		}
	}

	@PostMapping("/groups/add")
	public String addGroup(@RequestParam("number") String number) {
		Group group = new Group();
		group.setNumber(number);
		group.setDateAdded(LocalDate.now());
		groupService.save(group);
		return "redirect:/group?id=" + group.getId();
	}

	@PostMapping("/group/{id}/addStudent")
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
			return new ResponseEntity<>(response, CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/group/{groupId}/removeStudent/{studentId}")
	public ResponseEntity<?> deleteStudent(@PathVariable Long studentId, @PathVariable Long groupId) {
		Map<String, Object> response = new HashMap<>();
		Group group = groupService.findById(groupId)
				.orElseThrow(() -> new RuntimeException("Group not found"));

		Student student = studentService.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));

		group.getStudents().remove(student);
		studentService.deleteById(student.getId());

		response.put("success", true);
		response.put("message", "Student added successfully");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
