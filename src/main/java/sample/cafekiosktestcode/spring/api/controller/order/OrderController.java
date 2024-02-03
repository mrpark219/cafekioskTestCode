package sample.cafekiosktestcode.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosktestcode.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosktestcode.spring.api.service.order.OrderService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/api/vi/orders/new")
	public void createOrder(@RequestBody OrderCreateRequest request) {

		LocalDateTime registeredDateTime = LocalDateTime.now();
		orderService.createOrder(request, registeredDateTime);
	}
}
