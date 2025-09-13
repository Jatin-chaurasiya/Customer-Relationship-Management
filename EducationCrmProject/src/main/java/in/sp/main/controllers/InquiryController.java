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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import in.sp.main.entities.Employee;
import in.sp.main.entities.FollowUps;
import in.sp.main.entities.Inquiry;
import in.sp.main.services.FollowUpsService;
import in.sp.main.services.InquiryService;

@Controller
@SessionAttributes("sessionEmployee")
public class InquiryController {

	@Autowired
	private InquiryService inquiryService;

	@Autowired
	private FollowUpsService followUpsService;

	@GetMapping("/newInquiry")
	public String openNewInquiryPage(Model model) {
		model.addAttribute("inquiry", new Inquiry());
		return "new-inquiry";
	}

	@PostMapping("/submitForm")
	public String submitInquiryForm(@ModelAttribute("inquiry") Inquiry inquiry,
			@SessionAttribute("sessionEmployee") Employee sessionEmployee, Model model,
			@RequestParam(name = "followUpDate", required = false) String followUpDate,
			@RequestParam(name = "sourcePage", required = false) String sourcePage) {

		inquiry.setEmpEmail(sessionEmployee.getEmailId());

		// set date + time
		LocalDate ld = LocalDate.now();
		String date1 = ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		LocalTime lt = LocalTime.now();
		String time1 = lt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

		inquiry.setDateofInquiry(date1);
		inquiry.setTimeofInquiry(time1);

		try {
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
			model.addAttribute("successMsg", "Inquiry added successfully...");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "Some error occured, Please try again..!");
		}

		if ("followUpsPage".equals(sourcePage)) {
			model.addAttribute("successMsg", "Inquiry handled successfully...");
			return "follow-ups";
		} else {
			return "inquiry-mangement";
		}
	}
}
