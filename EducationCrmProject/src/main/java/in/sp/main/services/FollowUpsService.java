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
		if (followUps.getPhoneno() == null) {
			throw new IllegalArgumentException("Phone number cannot be null");
		}

		Optional<FollowUps> optional = followUpsRepository.findByPhoneno(followUps.getPhoneno());

		if (optional.isPresent()) {
			FollowUps existing = optional.get();
			existing.setFollowUpDate(followUps.getFollowUpDate());
			existing.setEmpEmail(followUps.getEmpEmail());
			followUpsRepository.save(existing);
		} else {
			followUpsRepository.save(followUps);
		}
	}

	public List<FollowUps> getMyFollowUps(String empEmail, String followUpDate) {

		return followUpsRepository.findByEmpEmailAndFollowUpDate(empEmail, followUpDate);

	}

	public void deleteByPhoneno(String phoneno) {
		Optional<FollowUps> optionalFollowUps = followUpsRepository.findByPhoneno(phoneno);
		if (optionalFollowUps.isPresent()) {
			FollowUps followUps = optionalFollowUps.get();
			followUpsRepository.delete(followUps);
		}
	}

}
