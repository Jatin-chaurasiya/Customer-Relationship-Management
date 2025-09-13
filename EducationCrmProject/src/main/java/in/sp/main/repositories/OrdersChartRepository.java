package in.sp.main.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.Orders;

@Repository
public interface OrdersChartRepository extends JpaRepository<Orders, Long> {

	String SQL_QUERY1 = "SELECT TRIM(SUBSTRING_INDEX(dateof_purchase , ',', 1)) AS purchased_date, SUM(course_amount) AS total_sales_amount FROM orders GROUP BY purchased_date ORDER BY purchased_date";

	@Query(value = SQL_QUERY1, nativeQuery = true)
	List<Object[]> findCoursesAmountTotalSales();

	String SQL_QUERY2 = "SELECT course_name, COUNT(*) AS total_sold FROM orders GROUP BY course_name";

	@Query(value = SQL_QUERY2, nativeQuery = true)
	List<Object[]> findCoursesTotalSales();

	String SELECT_QUERY3 = "SELECT TRIM(SUBSTRING_INDEX(dateof_purchase,',',1)) as purchased_date, COUNT(*) as number_of_course_sold from orders group by purchased_date order by purchased_date";

	@Query(value = SELECT_QUERY3, nativeQuery = true)
	List<Object[]> findCoursesSoldPerDay();

}
