package in.sp.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.sp.main.entities.Admin;
import in.sp.main.services.EmpSalesService;
import jakarta.servlet.http.HttpSession;

@Controller
public class EmpSalesInfoController {

	@Autowired
	private EmpSalesService empSalesService;

	@GetMapping("/sale")
	public String openSalesPage(Model model, HttpSession session) {

		Admin admin = (Admin) session.getAttribute("sessionAdmin");
		if (admin == null) {
			return "redirect:/adminLogin";
		}

		try {
			String totalSales = empSalesService.findTotalSalesByAllEmployee();
			model.addAttribute("totalSales", totalSales);

			List<Object[]> salesList = empSalesService.findTotalSalesByEachEmployee();
			model.addAttribute("salesList", salesList);

			return "sales";
		} catch (Exception e) {
			model.addAttribute("errorMsg", "Failed to load sales information");
			model.addAttribute("totalSales", "0");
			model.addAttribute("salesList", List.of());
			return "sales";
		}
	}
}