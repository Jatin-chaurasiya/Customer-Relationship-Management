package in.sp.main.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Admin;
import in.sp.main.repositories.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;

	public boolean adminLoginService(String email, String password) {

		if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
			return false;
		}

		Admin admin = adminRepository.findByEmail(email.trim().toLowerCase()); // ✅ Direct call

		if (admin == null) {
			return false;
		}

		return password.equals(admin.getPassword());
	}

	public boolean isEmailExists(String email) {
		if (email == null || email.trim().isEmpty()) {
			return false;
		}
		return adminRepository.existsByEmail(email.trim().toLowerCase());
	}

	public void saveAdmin(Admin admin) {
		adminRepository.save(admin);
	}

	public List<Admin> getAllAdmins() {
		return adminRepository.findAll();
	}

	public void deleteAdmin(Long id) {
		adminRepository.deleteById(id);
	}

}