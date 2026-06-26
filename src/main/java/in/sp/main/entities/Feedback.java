package in.sp.main.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Feedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String userName;

	@Column(nullable = false, length = 100)
	private String userEmail;

	@Column(nullable = false, length = 3000)
	private String userFeedback;

	@Column(length = 20)
	private String dateOfFeedback;

	@Column(length = 20)
	private String timeOfFeedback;

	@Column(length = 20)
	private String readStatus; // "read" or "unread"

	public Feedback() {
	}

	// Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserFeedback() {
		return userFeedback;
	}

	public void setUserFeedback(String userFeedback) {
		this.userFeedback = userFeedback;
	}

	public String getDateOfFeedback() {
		return dateOfFeedback;
	}

	public void setDateOfFeedback(String dateOfFeedback) {
		this.dateOfFeedback = dateOfFeedback;
	}

	public String getTimeOfFeedback() {
		return timeOfFeedback;
	}

	public void setTimeOfFeedback(String timeOfFeedback) {
		this.timeOfFeedback = timeOfFeedback;
	}

	public String getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}
}