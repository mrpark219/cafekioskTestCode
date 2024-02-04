package sample.cafekiosktestcode.spring.api.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosktestcode.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosktestcode.spring.api.service.order.Response.OrderResponse;
import sample.cafekiosktestcode.spring.domain.order.Order;
import sample.cafekiosktestcode.spring.domain.order.OrderRepository;
import sample.cafekiosktestcode.spring.domain.product.Product;
import sample.cafekiosktestcode.spring.domain.product.ProductRepository;
import sample.cafekiosktestcode.spring.domain.product.ProductType;
import sample.cafekiosktestcode.spring.domain.stock.Stock;
import sample.cafekiosktestcode.spring.domain.stock.StockRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

	private final ProductRepository productRepository;

	private final OrderRepository orderRepository;

	private final StockRepository stockRepository;

	/**
	 * 재고 감소 -> 동시성 고민
	 * optimistic lock / pessimistic lock / ...
	 */
	public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime registeredDateTime) {

		List<String> productNumbers = request.getProductNumbers();
		List<Product> products = findProductsBy(productNumbers);

		deductStockQuantities(products);

		Order order = Order.create(products, registeredDateTime);
		Order savedOrder = orderRepository.save(order);

		return OrderResponse.of(savedOrder);
	}

	private void deductStockQuantities(List<Product> products) {

		List<String> stockProductNumbers = extractStockProductNumbers(products);

		Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);

		Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

		for(String stockProductNumber : new HashSet<>(stockProductNumbers)) {
			Stock stock = stockMap.get(stockProductNumber);
			int quantity = productCountingMap.get(stockProductNumber).intValue();

			if(stock.isQuantityLessThan(quantity)) {
				throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
			}

			stock.deductQuantity(quantity);
		}
	}

	private List<Product> findProductsBy(List<String> productNumbers) {
		List<Product> products = productRepository.findAllByProductNumberIn(productNumbers);

		Map<String, Product> productMap = products.stream()
				.collect(Collectors.toMap(Product::getProductNumber, p -> p));

		return productNumbers.stream()
				.map(productMap::get)
				.collect(Collectors.toList());
	}

	private static List<String> extractStockProductNumbers(List<Product> products) {
		return products.stream()
				.filter(product -> ProductType.containsStockType(product.getType()))
				.map(Product::getProductNumber)
				.collect(Collectors.toList());
	}

	private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
		List<Stock> stocks = stockRepository.findAllByProductNumberIn(stockProductNumbers);
		return stocks.stream()
				.collect(Collectors.toMap(Stock::getProductNumber, s -> s));
	}

	private static Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
		return stockProductNumbers.stream()
				.collect(Collectors.groupingBy(p -> p, Collectors.counting()));
	}
}
