package sample.cafekiosktestcode.spring.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosktestcode.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosktestcode.spring.api.service.order.OrderService;
import sample.cafekiosktestcode.spring.api.service.order.Response.OrderResponse;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/api/vi/orders/new")
	public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {

		LocalDateTime registeredDateTime = LocalDateTime.now();
		return orderService.createOrder(request, registeredDateTime);
	}
}
