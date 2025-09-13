package in.sp.main.dto;

public class EmployeeProfileDTO {
	private String name;
	private String emailId;
	private String phoneNo;
	private String city;

	public EmployeeProfileDTO(String name, String emailId, String phoneNo, String city) {
		this.name = name;
		this.emailId = emailId;
		this.phoneNo = phoneNo;
		this.city = city;
	}

	// Getters
	public String getName() {
		return name;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public String getCity() {
		return city;
	}
}
