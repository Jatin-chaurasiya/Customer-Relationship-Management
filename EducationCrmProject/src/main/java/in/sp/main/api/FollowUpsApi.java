package in.sp.main.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.sp.main.entities.Employee;
import in.sp.main.entities.FollowUps;
import in.sp.main.services.FollowUpsService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class FollowUpsApi {

	@Autowired
	private FollowUpsService followUpsService;

	@GetMapping("/myFollowUps")
	public ResponseEntity<?> getFollowUpCustomer(@RequestParam("empEmail") String empEmail,
			@RequestParam("followUpDate") String followUpDate, HttpSession session) {

		Employee employee = (Employee) session.getAttribute("sessionEmployee");
		if (employee == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please login first");
		}

		try {
			if (empEmail == null || empEmail.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Employee email is required");
			}

			if (followUpDate == null || followUpDate.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("Follow-up date is required");
			}

			if (!employee.getEmailId().equals(empEmail.trim())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
			}

			List<FollowUps> followUpsList = followUpsService.getMyFollowUps(empEmail.trim(), followUpDate.trim());
			return ResponseEntity.ok(followUpsList);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch follow-ups");
		}
	}
}