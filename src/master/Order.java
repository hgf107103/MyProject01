package master;

public class Order { //주문내역
	
	private int tableNumber; // 주문한 테이블의 넘버
	private int orderNumber; // 주문한 메뉴의 메뉴넘버
	private String orderName; // 주문한 메뉴의 이름
	private int orderCost; //주문한 메뉴의 가격
	private int orderCount; // 주문한 메뉴의 개수
	private int orderTotal; // 주문한 메뉴의 총 가격
	
	public Order(Menu menu, int tableNumber, int orderNumber) {
		this.tableNumber = tableNumber;
		this.orderNumber = orderNumber;
		this.orderName = menu.getMenuName();
		this.orderCost = menu.getMenuCost();
		this.orderCount = 1;
		this.orderTotal = orderCount * menu.getMenuCost();
	}
	
	public Order(int orderNumber, int tableNumber, String orderName, int orderCost, int orderCount, int orderTotal) {
		this.tableNumber = tableNumber;
		this.orderNumber = orderNumber;
		this.orderName = orderName;
		this.orderCost = orderCost;
		this.orderCount = orderCount;
		this.orderTotal = orderTotal;
	}
	
	public void renewOrderTotal() {
		orderTotal = orderCount * orderCost;
	}
	
	public int getOrderNumber() {
		return orderNumber;
	}

	public String getMenuName() {
		return orderName;
	}
	
	public int getOrderCost() {
		return orderCost;
	}

	public void setOrderMenu(Menu orderMenu) {
		this.orderName = orderMenu.getMenuName();
		this.orderCost = orderMenu.getMenuCost();
		this.orderCount = 1;
		this.orderTotal = orderCount * orderMenu.getMenuCost();
		//메뉴를 잘못 주문했을때 사용할 수 있다.
	}

	public int getOrderCount() {
		return orderCount;
	}
	
	public void orderCountPlus(int plusCount) {
		this.orderCount += plusCount;
		this.orderTotal = orderCount * orderCost;
	}

	public int getOrderTotal() {
		renewOrderTotal();
		return orderTotal;
	}
	
}
