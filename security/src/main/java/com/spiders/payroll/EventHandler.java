
package com.spiders.payroll;

import static com.spiders.payroll.WebSocketConfiguration.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;



@Component
@RepositoryEventHandler(Student.class)
public class EventHandler {

	private final SimpMessagingTemplate websocket;

	private final EntityLinks entityLinks;

	@Autowired
	public EventHandler(SimpMessagingTemplate websocket, EntityLinks entityLinks) {
		this.websocket = websocket;
		this.entityLinks = entityLinks;
	}

	@HandleAfterCreate
	public void newStudent(Student student) {
		this.websocket.convertAndSend(
				MESSAGE_PREFIX + "/newStudent", getPath(student));
	}

	@HandleAfterDelete
	public void deleteStudent(Student student) {
		this.websocket.convertAndSend(
				MESSAGE_PREFIX + "/deleteStudent", getPath(student));
	}

	@HandleAfterSave
	public void updateStudent(Student student) {
		this.websocket.convertAndSend(
				MESSAGE_PREFIX + "/updateStudent", getPath(student));
	}

	/**
	 * Take an {@link Student} and get the URI using Spring Data REST's {@link EntityLinks}.
	 *
	 * @param student
	 */
	private String getPath(Student student) {
		return this.entityLinks.linkForItemResource(student.getClass(),
				student.getId()).toUri().getPath();
	}

}

