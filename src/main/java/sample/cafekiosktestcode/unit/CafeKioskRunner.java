package sample.cafekiosktestcode.unit;

import sample.cafekiosktestcode.unit.beverage.Americano;
import sample.cafekiosktestcode.unit.beverage.Latte;
import sample.cafekiosktestcode.unit.order.Order;

import java.time.LocalDateTime;

public class CafeKioskRunner {

	public static void main(String[] args) {

		CafeKiosk cafeKiosk = new CafeKiosk();

		cafeKiosk.add(new Americano());
		System.out.println(">>> 아메리카노 추가");

		cafeKiosk.add(new Latte());
		System.out.println(">>> 라떼 추가");

		int totalPrice = cafeKiosk.calculateTotalPrice();
		System.out.println("총 주문가격: " + totalPrice);

		Order order = cafeKiosk.creatOrder(LocalDateTime.now());
	}
}
