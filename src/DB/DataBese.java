package DB;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

import VO.RestaurantView;
import VO.TableView;
import master.*;

public class DataBese {
	
	
	private String url="jdbc:mysql://117.17.113.248:3306/restaurant";
	private String uid="guest";
	private String upass="123456";
	private String sql;
	private ArrayList<Table> tableList = new ArrayList<Table>();
	private ArrayList<Menu> menuList = new ArrayList<Menu>();
	private Connection conn = null;
	private Statement st = null;
	
	public DataBese() {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("mysql : ����̺� �����");
			
			conn = DriverManager.getConnection(url, uid, upass);
			System.out.println("mysql : ����̺꿡 DB ������");
			
			sql = "select * from mytable";
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			
			
			
			while (rs.next()) {
				int tableNumber = rs.getInt("tableNumber");
				String customers = rs.getString("customers");
				int costTotal = rs.getInt("costTotal");
				tableList.add(new Table(tableNumber, customers, costTotal));
			}
			System.out.println("mysql : ���̺� ����Ʈ �����Ϸ�");
			rs.close();
			
			
			
			
			for (Table table : tableList) {
				sql = "select * from tableorder where tableNumber=" + table.getTableNumber();
				rs = st.executeQuery(sql);
				ArrayList<Order> orderList = new ArrayList<Order>();
				int total = 0;
				while (rs.next()) {
					total += rs.getInt("orderTotal");
					orderList.add(new Order(rs.getInt("orderNumber"), rs.getInt("tableNumber"), rs.getString("orderName"), rs.getInt("orderCost"), rs.getInt("orderCount"), rs.getInt("orderTotal")));
				}
				
				/*String sql2 = "UPDATE mytable SET costTotal = " + total + " WHERE tableNumber = " + table.getTableNumber();
				PreparedStatement pstmt = conn.prepareStatement(sql2);
				pstmt.executeUpdate();*/
				
				table.setCostTotal(total);
				table.setOrderList(orderList);
				
			}
			System.out.println("mysql : ���̺� ����Ʈ�� ��������Ʈ �����Ϸ�");
			rs.close();
			
			sql = "select * from menu";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				menuList.add(new Menu(rs.getInt("menuNumber"), rs.getString("menuName"), rs.getInt("menuCost")));
			}
			System.out.println("mysql : �޴� ����Ʈ �����Ϸ�");
			rs.close();
			st.close();
			conn.close();
			
			System.out.println("mysql : ��� ��ü Ŭ���� ��");
			
			
			ownerCallThread oct = new ownerCallThread();
			oct.start();
			
			
			System.out.println("mysql : DB��ü �����Ϸ�\n");
			
		} catch (Exception e) {
			
			System.out.println("mysql : DB���� ����\n");
		}
	}
	
	public void	renewal() {
		
		try {
			
			System.out.println("mysql : �����ͺ��̽� �簻�� ����");
			reConnect();
			DbRenewal();
			
			String sql1 = "select * from mytable";
			String sql2 = "select * from menu";
			String sql3 = null;
			
			tableList.clear();
			menuList.clear();
			
			ResultSet rs1 = st.executeQuery(sql1);
			
			while (rs1.next()) {
				int tableNumber = rs1.getInt("tableNumber");
				String customers = rs1.getString("customers");
				int costTotal = rs1.getInt("costTotal");
				tableList.add(new Table(tableNumber, customers, costTotal));
			}
			System.out.println("mysql : ���̺� �����ͺ��̽� �簻�� ����");
			
			ResultSet rs2 = st.executeQuery(sql2);
			
			while (rs2.next()) {
				menuList.add(new Menu(rs2.getInt("menuNumber"), rs2.getString("menuName"), rs2.getInt("menuCost")));
			}
			System.out.println("mysql : �޴� �����ͺ��̽� �簻�� ����");
			
			for (Table table : tableList) {
				sql3 = "select * from tableorder where tableNumber=" + table.getTableNumber();
				ResultSet rs3 = st.executeQuery(sql3);
				ArrayList<Order> orderList = new ArrayList<Order>();
				int total = 0;
				while (rs3.next()) {
					orderList.add(new Order(rs3.getInt("orderNumber"), rs3.getInt("tableNumber"), rs3.getString("orderName"), rs3.getInt("orderCost"), rs3.getInt("orderCount"), rs3.getInt("orderTotal")));
					total += rs3.getInt("orderTotal");
				}
				
				/*String sql4 = "UPDATE mytable SET costTotal = " + total + " WHERE tableNumber = " + table.getTableNumber();
				PreparedStatement pstmt = conn.prepareStatement(sql4);
				pstmt.executeUpdate();*/
				
				table.setOrderList(orderList);
			}
			System.out.println("mysql : ���� �����ͺ��̽� �簻�� ����\n");
			removeConnect();
			
			rs1.close();
			rs2.close();
			
		} catch (Exception e) {
			System.out.println("mysql : ���� �����ͺ��̽� �簻�� ���� �߻�\n");
		}
		
	}
	
	public void DbRenewal() {
		try {
			System.out.println("mysql : �����ͺ��̽� ���� ��ũ����Ʈ �簻�� ����");
			
			String sql1 = "ALTER TABLE menu AUTO_INCREMENT=1";
			String sql2 = "SET @COUNT = 0";
			String sql3 = "UPDATE menu SET menuNumber = @COUNT:=@COUNT+1";
			String sql4 = "ALTER TABLE menu AUTO_INCREMENT=@COUNT:=@COUNT+1";
			PreparedStatement pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql2);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql3);
			pstmt.executeUpdate();
			System.out.println("mysql : �޴� �����ͺ��̽� ���� ��ũ����Ʈ �簻�� �Ϸ�");
			
			
			
			sql1 = "ALTER TABLE mytable AUTO_INCREMENT=1";
			sql2 = "SET @COUNT = 0";
			sql3 = "UPDATE mytable SET tableNumber = @COUNT:=@COUNT+1";
			sql4 = "ALTER TABLE mytable AUTO_INCREMENT=7";
			pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql2);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql3);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql4);
			pstmt.executeUpdate();
			System.out.println("mysql : ���̺� �����ͺ��̽� ���� ��ũ����Ʈ �簻�� �Ϸ�");
			
			
			
			sql1 = "ALTER TABLE tableorder AUTO_INCREMENT=1";
			sql2 = "SET @COUNT = 0";
			sql3 = "UPDATE tableorder SET orderNumber = @COUNT:=@COUNT+1";
			pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql2);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql3);
			pstmt.executeUpdate();
			System.out.println("mysql : ���� �����ͺ��̽� ���� ��ũ����Ʈ �簻�� �Ϸ�");
			
			
			sql1 = "ALTER TABLE rsmaster AUTO_INCREMENT=1";
			sql2 = "SET @COUNT = 0";
			sql3 = "UPDATE rsmaster SET idx = @COUNT:=@COUNT+1";
			pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql2);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql3);
			pstmt.executeUpdate();
			System.out.println("mysql : ī���� �����ͺ��̽� ���� ��ũ����Ʈ �簻�� �Ϸ�\n");
			
		} catch (Exception e) {
			System.out.println("mysql : �����ͺ��̽� ���� ��ũ����Ʈ �簻�� �����߻�\n");
		}
		
	}
	
	public ArrayList<Table> getTableList() {
		System.out.println("mysql : �� ���̺� ����Ʈ �۵�");
		return tableList;
	}
	
	public Table getTable(int index) {
		return tableList.get(index);
	}
	
	public ArrayList<Menu> getMenuList() {
		System.out.println("mysql : �� �޴� ����Ʈ �۵�");
		return menuList;
	}
	
	public Menu getMenu(int index) {
		return menuList.get(index);
	}
	
	public void reConnect() {
		try {
			
			System.out.println("mysql : mysql �翬�� �޼ҵ�");
			conn = DriverManager.getConnection(url, uid, upass);
			st = conn.createStatement();
			sql = "";
			System.out.println("mysql : �翬�� ����\n");
			
		} catch (Exception e) {
			System.out.println("mysql : �翬�� ����\n");
		}
	}
	
	public void removeConnect() {
		try {
			System.out.println("mysql : Ŀ��Ʈ ����");
			conn.close();
			st.close();
			System.out.println("mysql : Ŀ��Ʈ ���� ����\n");
		} catch (Exception e) {
			System.out.println("mysql : Ŀ��Ʈ ���� ����\n");
		}
		
	}
	
	public void tableAdd() {
		try {
			
			System.out.println("mysql : ���̺� �߰� ����");
			if (tableList.size() < 6) {
				int result = JOptionPane.showConfirmDialog(null, "���̺��� �߰��Ͻðڽ��ϱ�?", "���̺� �߰�", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					reConnect();
					String sql1 = "insert into mytable() values()";
					PreparedStatement pstmt = conn.prepareStatement(sql1);
					pstmt.executeUpdate();
					System.out.println("mysql : ���̺� �߰� ����\n");
					renewal();
					pstmt.close();
					JOptionPane.showMessageDialog(null, "���̺��� �߰��Ǿ����ϴ�\n���� ���̺� : " + tableList.size() + "��", "���̺� �߰�", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "���̺� �߰��� ��ҵǾ����ϴ�", "���̺� �߰�", JOptionPane.INFORMATION_MESSAGE);
					renewal();
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "���̺��� �� ���ֽ��ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
				renewal();
			}
			
		} catch (Exception e) {
			System.out.println("mysql : ���̺� �߰� �����߻�\n");
			renewal();
		}
	}
	
	public void tableDel() {
		try {
			System.out.println("mysql : ���̺� ���� ����");
			
			if (tableList.size() > 1) {
				int result = JOptionPane.showConfirmDialog(null, "���̺��� �����Ͻðڽ��ϱ�?", "���̺� ����", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					reConnect();
					String sql1 = "select * from mytable";
					ResultSet rs = st.executeQuery(sql1);
					int tableNumber = 0;
					while (rs.next()) {
						tableNumber = rs.getInt("tableNumber");
					}
					
					String sql2 = "SELECT * FROM tableorder WHERE tableNumber = " + tableNumber;
					rs = st.executeQuery(sql2);
					int orderTotal = 0;
					while (rs.next()) {
						orderTotal = rs.getInt("orderTotal");
					}
					
					if (orderTotal == 0) {
						String sql3 = "DELETE FROM mytable WHERE tableNumber = " + tableNumber;
						PreparedStatement pstmt = conn.prepareStatement(sql3);
						pstmt.executeUpdate();
						renewal();
						rs.close();
						pstmt.close();
						System.out.println("mysql : ���̺� ���� ����\n");
						JOptionPane.showMessageDialog(null, "���̺��� �����Ǿ����ϴ�\n���� ���̺� : " + tableList.size() + "��", "���̺� ����", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "�������� ���� ������ �ֽ��ϴ�.", "���̺� ����", JOptionPane.INFORMATION_MESSAGE);
						renewal();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "���̺� ������ ��ҵǾ����ϴ�", "���̺� ����", JOptionPane.INFORMATION_MESSAGE);
					renewal();
				}
			}
			
			else {
				JOptionPane.showMessageDialog(null, "���̺��� �ϳ� �̻� �־�� �մϴ�.", "����", JOptionPane.ERROR_MESSAGE);
				renewal();
			}
			
		} catch (Exception e) {
			System.out.println("mysql : ���̺� ���� �����߻�\n");
		}
	}
	
	public void tableReset(Table table) {
		try {
			
			System.out.println("mysql : ���̺�" + table.getTableNumber() + "�� ���� ����");

			renewal();
			reConnect();
			String sql1 = "UPDATE mytable SET customers = NULL, costTotal = 0, ownerCall = 0 WHERE tableNumber = " + table.getTableNumber();
			PreparedStatement pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			
			
			String sql2 = "DELETE FROM tableorder WHERE tableNumber = " + table.getTableNumber();
			pstmt = conn.prepareStatement(sql2);
			pstmt.executeUpdate();
			
			
			renewal();
			System.out.println("mysql : ���̺� ���� ����\n");
			
		} catch (Exception e) {
			System.out.println("mysql : ���̺� ���� ����\n");
		}
	}
	
	public void tableRenewal(Table table) {
		try {
			
			System.out.println("mysql : ���̺�" + table.getTableNumber() + "�� ���� ����");
			
			renewal();
			reConnect();
			String sql1 = "UPDATE mytable SET customers = '" + table.getCustomers() + "', costTotal = " + table.getCostTotal() + " WHERE tableNumber = " + table.getTableNumber();
			PreparedStatement pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			
			System.out.println("mysql : ���̺� ���� ����\n");
			
		} catch (Exception e) {
			System.out.println("mysql : ���̺� ���� ����\n");
		}
		
	}
	
	public void plusOrder(int tableNumber, String menuName, int count) {
		try {
			
			System.out.println("mysql : ���� �߰� ����");
			
			reConnect();
			
			String orderName = null;
			int orderCount = 0;
			int orderCost = 0;
			
			//���̺� �ѹ��� �޴� �̸��� �´� ������ �����´�. �̹� ������ �ִٸ� �� ������ �������� �����ϵ��� �Ѵ�.
			String sql1 = "select * from tableorder where tableNumber = " + tableNumber + " AND orderName = '" + menuName + "'";
			System.out.println("mysql : " + sql1);
			ResultSet rs = st.executeQuery(sql1);
			
			while (rs.next()) {
				orderName = rs.getString("orderName");
				orderCount = rs.getInt("orderCount");
				orderCost = rs.getInt("orderCost");
			}
			
			System.out.println("mysql : ���� ����Ʈ �Ϸ�");
			
			if (menuName.equals(orderName) && orderCount > 0) {
				
				System.out.println("mysql : �̹� ������ ����");
				orderCount = orderCount + count;
				int total = orderCount * orderCost;
				String sql2 = "UPDATE tableorder SET orderCount = " + orderCount + ", orderTotal = " + total + " WHERE orderName = '" + menuName + "' AND tableNumber = " + tableNumber;
				System.out.println("mysql : " + sql2);
				PreparedStatement pstmt = conn.prepareStatement(sql2);
				pstmt.executeUpdate();
				
				for (Order order : tableList.get(tableNumber - 1).getOrderList()) {
					if (order.getMenuName().equals(menuName)) {
						order.orderCountPlus(1);
						order.renewOrderTotal();
						System.out.println("�̹� ������ �ֹ��� �ϳ� �߰���");
						System.out.println("�޴��� : " + order.getMenuName());
						System.out.println("�� �ֹ����� : " + order.getOrderCount());
						System.out.println("�� ���� : " + order.getOrderTotal());
						tableList.get(tableNumber - 1).renewTotal();
						JOptionPane.showMessageDialog(null, "�ֹ��� �����ϼ̽��ϴ�.\n�޴��� : "+ order.getMenuName() + "\n�� �ֹ� ���� : "+ order.getOrderCount() +"��\n�� ���� : " + order.getOrderTotal()+"��", "�ֹ�", JOptionPane.INFORMATION_MESSAGE);
						
					}
				}
				
			}
			else {
				
				System.out.println("mysql : ������ ������ �߰���");
				
				
				for (Menu menu : menuList) {
					if (menu.getMenuName().equals(menuName)) {
						tableList.get(tableNumber - 1).getOrderList().add(new Order(tableList.get(tableNumber - 1).getOrderList().size(), tableNumber, menuName, menu.getMenuCost(), count, menu.getMenuCost() * count));
						tableList.get(tableNumber - 1).renewTotal();
						String sql2 = "INSERT INTO tableorder (tableNumber, orderName, orderCost, orderCount, orderTotal) values (" + tableNumber + ", '" + menu.getMenuName() + "', " + menu.getMenuCost() + ", " + count + ", " + menu.getMenuCost() + ")";
						PreparedStatement pstmt = conn.prepareStatement(sql2);
						pstmt.executeUpdate();
						
						JOptionPane.showMessageDialog(null, "�ֹ��� �����ϼ̽��ϴ�.\n�޴��� : "+ menu.getMenuName() + "\n�� �ֹ� ���� : " + count + "��\n�� ���� : " + menu.getMenuCost() * count +"��", "�ֹ�", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			
			
			reConnect();
			
			String sql2 = "UPDATE mytable SET costTotal = " + tableList.get(tableNumber - 1).getCostTotal() + " WHERE tableNumber = " + tableNumber;
			PreparedStatement pstmt = conn.prepareStatement(sql2);
			pstmt.executeUpdate();
			renewal();
			
			System.out.println("mysql : ���� �߰� ����\n");
			
		} catch (Exception e) {
			renewal();
			System.out.println("mysql : ���� �߰� ����\n");
		}
	}
	
	//�޴� �κ�
	public void menuAdd(String menuName, int menuCost) {
		try {
			
			System.out.println("mysql : �޴� �߰� ����");
			reConnect();
			String name = null;
			String sql = "select * from menu where menuName = '" + menuName + "'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				name = rs.getString("menuName");
			}
			
			if (menuName.equals(name)) {
				System.out.println("�̹� ���� �̸��� �޴��� �ֽ��ϴ�.\n");
				removeConnect();
				rs.close();
				JOptionPane.showMessageDialog(null, "�̹� ���� �̸��� �޴��� �����մϴ�." ,"����", JOptionPane.ERROR_MESSAGE);
			}
			
			else {
				if (menuCost >= 1000 && menuCost % 50 == 0) {
					String sql2 = "insert into menu (menuName, menuCost) values ('" + menuName +"', " + menuCost + ")";
					System.out.println("�޴��߰� ���� : " + sql2);
					PreparedStatement pstmt = conn.prepareStatement(sql2);
					pstmt.executeUpdate();
					renewal();
					System.out.println("mysql : �޴� �߰� ����\n");
					removeConnect();
					pstmt.close();
					rs.close();
					JOptionPane.showMessageDialog(null,"�޴��� �߰��Ǿ����ϴ�.\n�޴��� : " + menuName + "\n���� : "  + menuCost + "��" ,"�޴� �߰� �Ϸ�", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					System.out.println("���ݿ� ������ �ֽ��ϴ�.\n");
					removeConnect();
					rs.close();
					JOptionPane.showMessageDialog(null, "���ݿ� ������ �ֽ��ϴ�." ,"����", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			
		} catch (Exception e) {
			System.out.println("mysql : �޴� �߰� ����\n");
			JOptionPane.showMessageDialog(null, "�����߻�" ,"����", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public void menuDel(String menuName) {
		try {
			
			System.out.println("mysql : �޴� ���� ����");
			reConnect();
			String name = null;
			sql = "select * from menu where menuName = '" + menuName + "'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				name = rs.getString("menuName");
			}
			
			if (menuName.equals(name)) {
				int result = JOptionPane.showConfirmDialog(null, menuName + "�޴��� �����Ͻðڽ��ϱ�?", "�޴� ����", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					sql = "delete from menu where menuName = '" + menuName + "'";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();
					
					
					String sql2 = "DELETE FROM tableorder WHERE orderName = '" + menuName +"'";
					pstmt = conn.prepareStatement(sql2);
					pstmt.executeUpdate();
					
					renewal();
					System.out.println("mysql : �޴� ���� ����\n");
					
					removeConnect();
					pstmt.close();
					rs.close();
					renewal();
					JOptionPane.showMessageDialog(null, "�޴��� �����Ǿ����ϴ�." ,"�޴� ���� �Ϸ�", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					removeConnect();
					rs.close();
					renewal();
					JOptionPane.showMessageDialog(null, "�޴� ������ ��ҵǾ����ϴ�." ,"�޴� ���� ���", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			
			else {
				System.out.println("�׷� �޴��� �����ϴ�.\n");
				removeConnect();
				rs.close();
				renewal();
				JOptionPane.showMessageDialog(null, "�׷� �޴��� �������� ����" ,"����", JOptionPane.ERROR_MESSAGE);
			}
			
		} catch (Exception e) {
			System.out.println("mysql : �޴� ���� ����\n");
			renewal();
			JOptionPane.showMessageDialog(null, "�����߻�" ,"����", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void plusCustomersCount() {
		try {
			System.out.println("mysql : �� ī��Ʈ �߰� ����");
			
			reConnect();
			String dbDate = null;
			int dbCnt = 0;
			Date today = new Date();
			SimpleDateFormat mydate = new SimpleDateFormat("yyyy-MM-dd");
			String sql1 = "select * from rsmaster where today = '" + mydate.format(today) + "'"; 
			ResultSet rs = st.executeQuery(sql1);
			while (rs.next()) {
				dbDate = rs.getString("today");
				dbCnt = rs.getInt("customerscnt");
			}
			
			if (mydate.format(today).equals(dbDate)) {
				dbCnt++;
				String sql2 = "UPDATE rsmaster SET customerscnt = " + dbCnt + " WHERE today = '" + mydate.format(today) + "'";
				PreparedStatement pstmt = conn.prepareStatement(sql2);
				pstmt.executeUpdate();
				System.out.println("mysql : �� ī��Ʈ �߰� ����\n");
				removeConnect();
				pstmt.close();
				rs.close();
			}
			else {
				String sql3 = "INSERT INTO rsmaster (today, customerscnt) VALUES ('" + mydate.format(today) + "', 1)";
				PreparedStatement pstmt = conn.prepareStatement(sql3);
				pstmt.executeUpdate();
				System.out.println("mysql : �� ī��Ʈ �߰� ����\n");
				removeConnect();
				pstmt.close();
				rs.close();
			}
			
		} catch (Exception e) {
			System.out.println("mysql : �� ī��Ʈ �߰� ����\n");
		}
	}
	
	public int getCustomersCount() {
		try {
			
			System.out.println("mysql : �� ī��Ʈ �ҷ����� ����");
			reConnect();
			int Count = 0;
			Date today = new Date();
			SimpleDateFormat mydate = new SimpleDateFormat("yyyy-MM-dd");
			String sql1 = "SELECT * FROM rsmaster WHERE today = '" + mydate.format(today) + "'";
			ResultSet rs = st.executeQuery(sql1);
			while (rs.next()) {
				Count = rs.getInt("customerscnt");
			}
			System.out.println("mysql : �� ī��Ʈ �ҷ����� ����\n");
			return Count;
			
		} catch (Exception e) {
			System.out.println("mysql : �� ī��Ʈ �ҷ����� ����\n");
			return 0;
		}
	}
	
	public static class ownerCallThread extends Thread{
		
		private String url="jdbc:mysql://117.17.113.248:3306/restaurant";
		private String uid="guest";
		private String upass="123456";
		private Connection conn = null;
		private Statement st = null;
		
		public ownerCallThread() {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("mysql : �����忡 ����̺� �����");
				
				conn = DriverManager.getConnection(url, uid, upass);
				System.out.println("mysql : �����忡 ����̺꿡 DB ������");
			} catch (Exception e) {
				System.out.println("mysql : �����忡 DB ���� ���� �߻�");
			}
		}
		
		@Override
		public void run() {
			System.out.println("mysql : ������ �� ������ �۵� ����");
			try {
				
				do {
					
					st = conn.createStatement();
					String sql1 ="SELECT * FROM mytable";
					ResultSet rs = st.executeQuery(sql1);
					while (rs.next()) {
						int tableNumber = rs.getInt("tableNumber");
						int ownerCall = rs.getInt("ownerCall");
						
						if (ownerCall == 1) {
							int result = JOptionPane.showConfirmDialog(null, tableNumber + "�� ���̺��� ȣ���Դϴ�.", "ȣ��", JOptionPane.YES_NO_OPTION);
							if (result == JOptionPane.YES_OPTION) {
								
								String sql2 = "UPDATE mytable SET ownerCall = 0 WHERE tableNumber = " + tableNumber;
								PreparedStatement pstmt = conn.prepareStatement(sql2);
								pstmt.executeUpdate();
								
								JOptionPane.showMessageDialog(null, "ȣ�� Ȯ�εǾ����ϴ�.");
							}
							else {
								JOptionPane.showMessageDialog(null, "ȣ�� Ȯ���� ����ϼ̽��ϴ�.\n��� �Ŀ� ��ȣ�� �մϴ�.");
							}
						}
					}
					Thread.sleep(2000);
					
				} while (true);
				
			} catch (Exception e) {
				
				System.out.println("mysql : ������ �� ������ �����߻�");
			}	
		}

	}
}
