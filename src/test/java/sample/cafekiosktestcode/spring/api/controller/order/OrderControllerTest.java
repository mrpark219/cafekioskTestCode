package sample.cafekiosktestcode.spring.api.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosktestcode.spring.ControllerTestSupport;
import sample.cafekiosktestcode.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosktestcode.spring.api.service.order.OrderService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends ControllerTestSupport {

	@DisplayName("신규 주문을 등록한다.")
	@Test
	void createOrder() throws Exception {

		// given
		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of("001"))
			.build();

		// when
		// then
		mockMvc.perform(
				post("/api/vi/orders/new")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("200"))
			.andExpect(jsonPath("$.status").value("OK"))
			.andExpect(jsonPath("$.message").value("OK"));
	}

	@DisplayName("신규 주문을 등록할 때 상품 번호는 1개 이상이여야 한다.")
	@Test
	void createOrderWithEmptyProductNumbers() throws Exception {

		// given
		OrderCreateRequest request = OrderCreateRequest.builder()
			.productNumbers(List.of())
			.build();

		// when
		// then
		mockMvc.perform(
				post("/api/vi/orders/new")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("400"))
			.andExpect(jsonPath("$.status").value("BAD_REQUEST"))
			.andExpect(jsonPath("$.message").value("상품 번호 리스트는 필수입니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}
}