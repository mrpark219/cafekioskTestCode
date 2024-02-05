package sample.cafekiosktestcode.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosktestcode.spring.IntegrationTestSupport;
import sample.cafekiosktestcode.spring.domain.product.Product;
import sample.cafekiosktestcode.spring.domain.product.ProductRepository;
import sample.cafekiosktestcode.spring.domain.product.ProductSellingStatus;
import sample.cafekiosktestcode.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosktestcode.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosktestcode.spring.domain.product.ProductType.HANDMADE;


@Transactional
class OrderRepositoryTest extends IntegrationTestSupport {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@DisplayName("특정 날짜에 결제 완료가 된 주문 목록을 조회한다.")
	@Test
	void findOrdersByRegisteredDate() {

		// given
		Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
		productRepository.save(product);

		Order order1 = Order.create(List.of(product), LocalDateTime.of(2024, 2, 4, 23, 0, 0));
		LocalDateTime registeredDateTime = LocalDateTime.of(2024, 2, 5, 13, 0, 0);
		Order order2 = Order.builder()
			.products(List.of(product))
			.orderStatus(OrderStatus.PAYMENT_COMPLETED)
			.registeredDateTime(registeredDateTime)
			.build();

		orderRepository.saveAll(List.of(order1, order2));

		LocalDateTime targetStartDateTime = LocalDateTime.of(2024, 2, 5, 0, 0, 0);
		LocalDateTime targetEndDateTime = LocalDateTime.of(2024, 2, 6, 0, 0, 0);

		// when
		List<Order> orders = orderRepository.findOrdersBy(targetStartDateTime, targetEndDateTime, OrderStatus.PAYMENT_COMPLETED);

		// then
		assertThat(orders).hasSize(1)
			.extracting("orderStatus", "registeredDateTime")
			.contains(
				tuple(OrderStatus.PAYMENT_COMPLETED, registeredDateTime)
			);
	}

	private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
		return Product.builder()
			.productNumber(productNumber)
			.type(type)
			.sellingStatus(sellingStatus)
			.name(name)
			.price(price)
			.build();
	}
}