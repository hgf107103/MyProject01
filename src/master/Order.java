package master;

public class Order { //�ֹ�����
	
	private int tableNumber; // �ֹ��� ���̺��� �ѹ�
	private int orderNumber; // �ֹ��� �޴��� �޴��ѹ�
	private String orderName; // �ֹ��� �޴��� �̸�
	private int orderCost; //�ֹ��� �޴��� ����
	private int orderCount; // �ֹ��� �޴��� ����
	private int orderTotal; // �ֹ��� �޴��� �� ����
	
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
		//�޴��� �߸� �ֹ������� ����� �� �ִ�.
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
