package in.sp.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.sp.main.entities.EmployeeOrder;

@Repository
public interface EmployeeOrderRepository extends JpaRepository<EmployeeOrder, Long> {

}
