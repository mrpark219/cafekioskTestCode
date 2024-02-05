package sample.cafekiosktestcode.spring.api.service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosktestcode.spring.IntegrationTestSupport;
import sample.cafekiosktestcode.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosktestcode.spring.domain.product.Product;
import sample.cafekiosktestcode.spring.domain.product.ProductRepository;
import sample.cafekiosktestcode.spring.domain.product.ProductSellingStatus;
import sample.cafekiosktestcode.spring.domain.product.ProductType;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosktestcode.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosktestcode.spring.domain.product.ProductType.HANDMADE;

@Transactional
class ProductNumberFactoryTest extends IntegrationTestSupport {

	@Autowired
	ProductNumberFactory productNumberFactory;

	@Autowired
	ProductRepository productRepository;

	@DisplayName("신규 상품을 등록한다. 상품 번호는 가장 최근 상품의 상품 번호에서 1 증가한 값이다.")
	@Test
	void createNextProductNumber() {

		// given
		Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);

		productRepository.save(product);

		ProductCreateServiceRequest request = ProductCreateServiceRequest.builder()
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("카푸치노")
			.price(5000)
			.build();

		// when
		String nextProductNumber = productNumberFactory.createNextProductNumber();

		// then
		assertThat(nextProductNumber).isEqualTo("002");
	}

	@DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품 번호는 001이다")
	@Test
	void createNextProductNumberWhenProductsIsEmpty() {

		// given
		// when
		String nextProductNumber = productNumberFactory.createNextProductNumber();

		// then
		assertThat(nextProductNumber).isEqualTo("001");
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