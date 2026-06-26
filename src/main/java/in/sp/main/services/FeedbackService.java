package in.sp.main.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.sp.main.entities.Feedback;
import in.sp.main.repositories.FeedbackRepository;

@Service
public class FeedbackService {
	@Autowired
	private FeedbackRepository feedbackRepository;

	public void sendFeedback(Feedback feedback) {
		if (feedback == null) {
			throw new IllegalArgumentException("Feedback cannot be null");
		}

		if (feedback.getUserName() == null || feedback.getUserName().trim().isEmpty()) {
			throw new IllegalArgumentException("Name is required");
		}

		if (feedback.getUserEmail() == null || feedback.getUserEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Email is required");
		}

		if (feedback.getUserFeedback() == null || feedback.getUserFeedback().trim().isEmpty()) {
			throw new IllegalArgumentException("Feedback message is required");
		}
		feedbackRepository.save(feedback);
	}

	public Page<Feedback> getAllFeedbacksByPagination(Pageable pageable) {
		return feedbackRepository.findAll(pageable);
	}

	public boolean updateFeedbackStatus(Long id, String status) {
		if (id == null || status == null || status.trim().isEmpty()) {
			return false;
		}
		Feedback feedback = feedbackRepository.findById(id).orElse(null);
		if (feedback != null) {
			feedback.setReadStatus(status.trim().toLowerCase());
			feedbackRepository.save(feedback);
			return true;
		}
		return false;
	}
}