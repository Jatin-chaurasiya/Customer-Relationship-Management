package in.sp.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.sp.main.entities.User;
import in.sp.main.repositories.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public Page<User> getAllUserDetailsByPagination(Pageable pageable) {
		return customerRepository.findAll(pageable);
	}

	public User getCustomerDetails(String userEmail) {
		if (userEmail == null || userEmail.trim().isEmpty()) {
			return null;
		}
		return customerRepository.findByEmail(userEmail.trim());
	}

	public void updateUserBanStatus(User user) {
		if (user == null || user.getId() == null) {
			throw new IllegalArgumentException("Invalid user data");
		}
		customerRepository.save(user);
	}
}