package in.sp.main.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.sp.main.entities.Employee;
import in.sp.main.entities.FollowUps;
import in.sp.main.entities.Inquiry;
import in.sp.main.services.FollowUpsService;
import in.sp.main.services.InquiryService;
import jakarta.servlet.http.HttpSession;

@Controller
public class InquiryController {

	@Autowired
	private InquiryService inquiryService;

	@Autowired
	private FollowUpsService followUpsService;

	@GetMapping("/newInquiry")
	public String openNewInquiryPage(Model model, HttpSession session) {
		// Session check
		Employee employee = (Employee) session.getAttribute("sessionEmployee");
		if (employee == null) {
			return "redirect:/employeeLogin";
		}

		model.addAttribute("sessionEmployee", employee);
		model.addAttribute("inquiry", new Inquiry());
		return "new-inquiry";
	}

	@PostMapping("/submitForm")
	public String submitInquiryForm(@ModelAttribute("inquiry") Inquiry inquiry, HttpSession session, Model model,
			@RequestParam(name = "followUpDate", required = false) String followUpDate,
			@RequestParam(name = "sourcePage", required = false) String sourcePage) {

		Employee sessionEmployee = (Employee) session.getAttribute("sessionEmployee");
		if (sessionEmployee == null) {
			return "redirect:/employeeLogin";
		}

		// Sidebar ke liye
		model.addAttribute("sessionEmployee", sessionEmployee);

		try {
			inquiry.setEmpEmail(sessionEmployee.getEmailId());

			LocalDate ld = LocalDate.now();
			String date1 = ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

			LocalTime lt = LocalTime.now();
			String time1 = lt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

			inquiry.setDateofInquiry(date1);
			inquiry.setTimeofInquiry(time1);

			inquiryService.addNewInquiry(inquiry);

			String status = inquiry.getStatus();
			if ("Interested - (follow up)".equals(status) && followUpDate != null && !followUpDate.isEmpty()) {
				FollowUps followUps = new FollowUps();
				followUps.setPhoneno(inquiry.getPhoneno());
				followUps.setFollowUpDate(followUpDate);
				followUps.setEmpEmail(sessionEmployee.getEmailId());
				followUpsService.addFollowUps(followUps);
			} else {
				followUpsService.deleteByPhoneno(inquiry.getPhoneno());
			}

			model.addAttribute("successMsg", "Inquiry added successfully");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Failed to add inquiry. Please try again.");
		}

		if ("followUpsPage".equals(sourcePage)) {
			return "follow-ups";
		} else {
			return "inquiry-mangement";
		}
	}

	@PostMapping("/submitInquiryForm")
	public String submitInquiryFormModal(@ModelAttribute("inquiry") Inquiry inquiry, HttpSession session, Model model,
			@RequestParam(name = "followUpDate", required = false) String followUpDate) {

		return submitInquiryForm(inquiry, session, model, followUpDate, null);
	}
}