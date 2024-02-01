package sample.cafekiosktestcode.unit.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sample.cafekiosktestcode.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class Order {

	private final LocalDateTime orderDateTIme;

	private final List<Beverage> beverages;
}
