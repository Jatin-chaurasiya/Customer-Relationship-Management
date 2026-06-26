package in.sp.main.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "course_name")
	private String courseName;

	@Column(name = "course_amount")
	private String courseAmount;

	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "dateof_purchase")
	private String dateofPurchase;

	@Column(name = "payment_id")
	private String paymentId;

	@Column(name = "order_id")
	private String orderId;

	// Getters & Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCourseName() { // ✅ Correct getter
		return courseName;
	}

	public void setCourseName(String courseName) { // ✅ Correct setter
		this.courseName = courseName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getDateofPurchase() {
		return dateofPurchase;
	}

	public void setDateofPurchase(String dateofPurchase) {
		this.dateofPurchase = dateofPurchase;
	}

	public String getPaymentId() { // ✅ Correct getter
		return paymentId;
	}

	public void setPaymentId(String paymentId) { // ✅ Correct setter
		this.paymentId = paymentId;
	}

	public String getCourseAmount() {
		return courseAmount;
	}

	public void setCourseAmount(String courseAmount) {
		this.courseAmount = courseAmount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

}
