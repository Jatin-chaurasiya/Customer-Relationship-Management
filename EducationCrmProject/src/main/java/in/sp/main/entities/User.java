package in.sp.main.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z ]{5,25}$", message = "Invalid Name Format")
	private String name;
	@Column
	@NotEmpty
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid emailId Format")
	private String email;
	@Column
	@NotEmpty
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid Password Format")
	private String password;
	@Column
	@NotEmpty
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid PhoneNo. Format")
	private String phoneno;
	@Column
	@NotEmpty
	@Pattern(regexp = "^[A-Za-z ]+$", message = "Invalid City Format")
	private String city;

	@Column(name = "ban_status", nullable = false)
	private boolean banStatus = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean isBanStatus() {
		return banStatus;
	}

	public void setBanStatus(boolean banStatus) {
		this.banStatus = banStatus;
	}

}
