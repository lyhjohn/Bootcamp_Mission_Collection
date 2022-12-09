package com.zerobase.cms.order_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.cms.order_api.domain.model.Product;
import com.zerobase.cms.order_api.domain.product.AddProductForm;
import com.zerobase.cms.order_api.domain.product.AddProductItemForm;
import com.zerobase.cms.order_api.domain.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;

	@Test
	void ADD_PRODUCT_TEST() {
		//given
		Long sellerId = 1L;
		AddProductForm addProductForm = makeProductForm("나이키", "신발입니다", 3);
		Product product = productService.addProduct(sellerId, addProductForm);
		//when
		Product result = productRepository.findById(product.getId()).get();
		//then
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo("나이키");
		assertThat(result.getDescription()).isEqualTo("신발입니다");
		assertThat(result.getProductItemList().size()).isEqualTo(3);

	}

	private static AddProductForm makeProductForm(String name, String description, int itemCount) {
		List<AddProductItemForm> itemForms = new ArrayList<>();
		for (int i = 0; i < itemCount; i++) {
			itemForms.add(makeProductItemForm(name + i));
		}
		return AddProductForm.builder()
			.name(name)
			.description(description)
			.addProductItemFormList(itemForms)
			.build();
	}

	private static AddProductItemForm makeProductItemForm(String name) {
		return AddProductItemForm.builder()
			.name(name)
			.price(1000)
			.count(1)
			.build();
	}


}