package com.spiders.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner {

	private final StudentRepository students;
	private final ManagerRepository managers;

	@Autowired
	public DatabaseLoader(StudentRepository studentRepository,
						  ManagerRepository managerRepository) {

		this.students = studentRepository;
		this.managers = managerRepository;
	}

	@Override
	public void run(String... strings) throws Exception {

		Manager admin = this.managers.save(new Manager("admin", "password_of_admin",
							"ROLE_MANAGER"));
		Manager company = this.managers.save(new Manager("manager", "manager",
							"ROLE_MANAGER"));

		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken("admin", "doesn't matter",
				AuthorityUtils.createAuthorityList("ROLE_MANAGER")));

		this.students.save(new Student("Frodo", "Baggins", "ring bearer", admin));
		this.students.save(new Student("Bilbo", "Baggins", "burglar", admin));
		this.students.save(new Student("Gandalf", "the Grey", "wizard", admin));

		SecurityContextHolder.getContext().setAuthentication(
			new UsernamePasswordAuthenticationToken("company", "doesn't matter",
				AuthorityUtils.createAuthorityList("ROLE_MANAGER")));

		this.students.save(new Student("Samwise", "Gamgee", "gardener", company));
		this.students.save(new Student("Merry", "Brandybuck", "pony rider", company));
		this.students.save(new Student("Peregrin", "Took", "pipe smoker", company));

		SecurityContextHolder.clearContext();
	}
}
