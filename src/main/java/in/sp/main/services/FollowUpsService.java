package in.sp.main.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.entities.FollowUps;
import in.sp.main.repositories.FollowUpsRepository;

@Service
public class FollowUpsService {

	@Autowired
	private FollowUpsRepository followUpsRepository;

	public void addFollowUps(FollowUps followUps) {
		// Validation
		if (followUps == null) {
			throw new IllegalArgumentException("Follow-up cannot be null");
		}

		if (followUps.getPhoneno() == null || followUps.getPhoneno().trim().isEmpty()) {
			throw new IllegalArgumentException("Phone number is required");
		}

		if (followUps.getFollowUpDate() == null || followUps.getFollowUpDate().trim().isEmpty()) {
			throw new IllegalArgumentException("Follow-up date is required");
		}

		if (followUps.getEmpEmail() == null || followUps.getEmpEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Employee email is required");
		}

		try {
			Optional<FollowUps> optional = followUpsRepository.findByPhoneno(followUps.getPhoneno().trim());

			if (optional.isPresent()) {
				FollowUps existing = optional.get();
				existing.setFollowUpDate(followUps.getFollowUpDate());
				existing.setEmpEmail(followUps.getEmpEmail());
				followUpsRepository.save(existing);
			} else {
				followUpsRepository.save(followUps);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to add follow-up: " + e.getMessage());
		}
	}

	public List<FollowUps> getMyFollowUps(String empEmail, String followUpDate) {
		// Validation
		if (empEmail == null || empEmail.trim().isEmpty()) {
			return List.of();
		}

		if (followUpDate == null || followUpDate.trim().isEmpty()) {
			return List.of();
		}

		try {
			return followUpsRepository.findByEmpEmailAndFollowUpDate(empEmail.trim(), followUpDate.trim());
		} catch (Exception e) {
			throw new RuntimeException("Failed to fetch follow-ups: " + e.getMessage());
		}
	}

	public void deleteByPhoneno(String phoneno) {
		if (phoneno == null || phoneno.trim().isEmpty()) {
			return;
		}

		try {
			Optional<FollowUps> optionalFollowUps = followUpsRepository.findByPhoneno(phoneno.trim());
			if (optionalFollowUps.isPresent()) {
				FollowUps followUps = optionalFollowUps.get();
				followUpsRepository.delete(followUps);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete follow-up: " + e.getMessage());
		}
	}
}