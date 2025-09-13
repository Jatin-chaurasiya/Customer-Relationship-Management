package in.sp.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.EmployeeOrder;

@Repository
public interface EmpSalesRepository extends JpaRepository<EmployeeOrder, Long> {
	String SQL_QUERY1 = "select sum(course_amount) as total_sum_of_course_amount from orders where order_id not like 'order_%'";

	@Query(value = SQL_QUERY1, nativeQuery = true)
	String findTotalSalesByAllEmployees();

	String SQL_QUERY2 = "select e.name AS employee_name,e.email_id AS employee_email,e.phone_no AS employee_phoneno,SUM(course_amount) total_sale from employee e JOIN employee_order eo ON e.email_id=eo.employee_email JOIN orders o ON eo.order_id=o.order_id GROUP BY e.name,e.email_id,e.phone_no";

	@Query(value = SQL_QUERY2, nativeQuery = true)
	List<Object[]> findTotalSalesByEachEmployees();

}
