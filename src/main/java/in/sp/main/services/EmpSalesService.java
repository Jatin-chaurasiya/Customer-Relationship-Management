package in.sp.main.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sp.main.repositories.EmpSalesRepository;

@Service
public class EmpSalesService {

	@Autowired
	private EmpSalesRepository empSalesRepository;

	public String findTotalSalesByAllEmployee() {
		try {
			String totalSales = empSalesRepository.findTotalSalesByAllEmployees();
			return totalSales != null ? totalSales : "0";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	public List<Object[]> findTotalSalesByEachEmployee() {
		try {
			return empSalesRepository.findTotalSalesByEachEmployees();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}
}