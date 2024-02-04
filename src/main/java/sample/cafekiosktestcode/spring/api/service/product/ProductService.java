package sample.cafekiosktestcode.spring.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosktestcode.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosktestcode.spring.api.service.product.response.ProductResponse;
import sample.cafekiosktestcode.spring.domain.product.Product;
import sample.cafekiosktestcode.spring.domain.product.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

import static sample.cafekiosktestcode.spring.domain.product.ProductSellingStatus.forDisplay;

/**
 * readOnly = true: 읽기 전용
 * CRUD에서 CUD 동작 X / only Read
 * JPA: CUD 스냅샷 저장, 변경 감지 X (성능 향상)
 *
 * CQRS - Command / Query 분리
 *
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	// 동시성 이슈
	// UUID
	@Transactional
	public ProductResponse createProduct(ProductCreateServiceRequest request) {

		String nextProductNumber = createNextProductNumber();

		Product product = request.toEntity(nextProductNumber);
		Product savedProduct = productRepository.save(product);

		return ProductResponse.of(savedProduct);
	}

	public List<ProductResponse> getSellingProducts() {

		List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());

		return products.stream()
				.map(ProductResponse::of)
				.collect(Collectors.toList());
	}

	private String createNextProductNumber() {

		String latestProductNumber = productRepository.findLatestProductNumber();
		if(latestProductNumber == null) {
			return "001";
		}

		int latestProductNumberInt = Integer.parseInt(latestProductNumber);
		int nextProductNumberInt = latestProductNumberInt + 1;

		return String.format("%03d", nextProductNumberInt);
	}
}
