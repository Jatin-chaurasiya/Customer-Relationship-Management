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
		return empSalesRepository.findTotalSalesByAllEmployees();
	}

	public List<Object[]> findTotalSalesByEachEmployee() {
		return empSalesRepository.findTotalSalesByEachEmployees();
	}

}
