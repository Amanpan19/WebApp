package com.Cart_Service.cartService;

import com.Cart_Service.cartService.filter.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class CartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartServiceApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> interUrl(){
		FilterRegistrationBean<AuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new AuthFilter());
		filterRegistrationBean.addUrlPatterns(
				"/v1/api/cart/addProduct",
				"/v1/api/cart/getCart/details/*",
				"/v1/api/cart/getNoOfProducts",
				"/v1/api/cart/remove/product",
				"/v1/api/cart/decrease/productQty",
				"/v1/api/cart/check-exist",
				"/api/v1/fav/addProduct",
				"/api/v1/fav/getFav/details/data",
				"/api/v1/fav/remove/product",
				"/api/v1/fav/getNoOfProducts",
				"/api/v1/fav/check-exist"
		);
		return filterRegistrationBean;
	}
}
