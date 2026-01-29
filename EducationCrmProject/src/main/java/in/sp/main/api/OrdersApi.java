package in.sp.main.api;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import in.sp.main.entities.Orders;
import in.sp.main.services.OrdersService;

@RestController
@RequestMapping("/api")
public class OrdersApi {

	@Autowired
	private OrdersService ordersService;

	@PostMapping("/storeOrderDetails")
	public ResponseEntity<String> storeUserOrders(@RequestBody Orders order) throws RazorpayException {

		RazorpayClient razorpay = new RazorpayClient("rzp_test_R8jbIJWbxXJTNi", "E3AGxLAJl6UnMLOdLOxiF3T0");

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", order.getCourseAmount());
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", "rcpt_id_" + System.currentTimeMillis());

		Order razorpayOrder = razorpay.orders.create(orderRequest);

		System.out.println(razorpayOrder);

		String orderId = razorpayOrder.get("id");
		order.setOrderId(orderId);

		ordersService.storeUserOrders(order);

		return ResponseEntity.ok("Order details stored successfully");
	}

}
