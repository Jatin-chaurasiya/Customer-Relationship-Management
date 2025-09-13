package in.sp.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.sp.main.services.EmpSalesService;

@Controller
public class EmpSalesInfoController {

	@Autowired
	private EmpSalesService empSalesService;

	@GetMapping("/sale")
	public String opneSalesPage(Model model) {

		String totalSales = empSalesService.findTotalSalesByAllEmployee();
		model.addAttribute("totalSales", totalSales);

		List<Object[]> salesList = empSalesService.findTotalSalesByEachEmployee();
		model.addAttribute("salesList", salesList);

		return "sales";

	}

}
