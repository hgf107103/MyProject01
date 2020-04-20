package master;

import java.util.ArrayList;

public class Table { // 손님을 받을 테이블 객체
	private int tableNumber; // 테이블 넘버
	private String customers; // 고객이름
	private int costTotal; // 고객이 주문한 메뉴의 총합
	private int ownerCall;
	private ArrayList<Order> orderList = new ArrayList<Order>();
	
	public Table(int tableNumber) {
		super();
		this.tableNumber = tableNumber;
		this.customers = null;
		this.costTotal = 0;
		ownerCall = 0;
	}
	
	public Table(int tableNumber, String customers, int costTotal) {
		super();
		this.tableNumber = tableNumber;
		this.customers = customers;
		this.costTotal = costTotal;
		ownerCall = 0;
	}
	
	public void onCustomers(String customers) {
		this.customers = customers;
	}
	
	public void callOrder(int menuNumber, int callCount) {
		orderList.get(menuNumber).orderCountPlus(callCount);
	}
	
	public void reset() {
		this.customers = null;
		this.costTotal = 0;
		orderList.clear();
	}
	
	public void renewTotal() {
		costTotal = 0;
		for (Order order : orderList) {
			costTotal += order.getOrderTotal();
		}
	}

	public int getTableNumber() {
		return tableNumber;
	}

	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getCustomers() {
		return customers;
	}

	public void setCustomers(String customers) {
		this.customers = customers;
		System.out.println("고객이름 변경완료");
	}

	public int getCostTotal() {
		return costTotal;
	}

	public ArrayList<Order> getOrderList() {
		return orderList;
	}
	
	public void setOrderList(ArrayList<Order> orderList) {
		this.orderList = orderList;
	}
	
	public void setCostTotal(int total) {
		costTotal = total;
	}
	
	
}
