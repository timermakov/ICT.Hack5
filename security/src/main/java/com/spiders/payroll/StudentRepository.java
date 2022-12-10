
package com.spiders.payroll;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;



@PreAuthorize("hasRole('ROLE_MANAGER')") // <1>
public interface StudentRepository extends PagingAndSortingRepository<Student, Long> {

	@Override
	@PreAuthorize("#student?.manager == null or #student?.manager?.name == authentication?.name")
	Student save(@Param("student") Student student);

	@Override
	@PreAuthorize("@studentRepository.findById(#id)?.manager?.name == authentication?.name")
	void deleteById(@Param("id") Long id);

	@Override
	@PreAuthorize("#student?.manager?.name == authentication?.name")
	void delete(@Param("student") Student student);

}

