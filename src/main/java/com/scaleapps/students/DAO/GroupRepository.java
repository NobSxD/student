package com.scaleapps.students.DAO;

import com.scaleapps.students.Entitu.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
