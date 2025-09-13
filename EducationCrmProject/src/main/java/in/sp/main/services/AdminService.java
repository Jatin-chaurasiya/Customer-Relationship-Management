package in.sp.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Admin;
import in.sp.main.repositories.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	public boolean adminLoginService(String email, String password) {
		Admin admin = adminRepository.findByEmail(email);

		if (admin == null) {
			return false; // no such email
		}

		return password.equals(admin.getPassword());
	}
}
