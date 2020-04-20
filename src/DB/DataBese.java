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
			System.out.println("mysql : 드라이브 적재됨");
			
			conn = DriverManager.getConnection(url, uid, upass);
			System.out.println("mysql : 드라이브에 DB 연동됨");
			
			sql = "select * from mytable";
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			
			
			
			
			while (rs.next()) {
				int tableNumber = rs.getInt("tableNumber");
				String customers = rs.getString("customers");
				int costTotal = rs.getInt("costTotal");
				tableList.add(new Table(tableNumber, customers, costTotal));
			}
			System.out.println("mysql : 테이블 리스트 정리완료");
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
			System.out.println("mysql : 테이블 리스트에 오더리스트 정리완료");
			rs.close();
			
			sql = "select * from menu";
			rs = st.executeQuery(sql);
			while (rs.next()) {
				menuList.add(new Menu(rs.getInt("menuNumber"), rs.getString("menuName"), rs.getInt("menuCost")));
			}
			System.out.println("mysql : 메뉴 리스트 정리완료");
			rs.close();
			st.close();
			conn.close();
			
			System.out.println("mysql : 모든 객체 클로즈 됨");
			
			
			ownerCallThread oct = new ownerCallThread();
			oct.start();
			
			
			System.out.println("mysql : DB객체 생성완료\n");
			
		} catch (Exception e) {
			
			System.out.println("mysql : DB연동 실패\n");
		}
	}
	
	public void	renewal() {
		
		try {
			
			System.out.println("mysql : 데이터베이스 재갱신 시작");
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
			System.out.println("mysql : 테이블 데이터베이스 재갱신 성공");
			
			ResultSet rs2 = st.executeQuery(sql2);
			
			while (rs2.next()) {
				menuList.add(new Menu(rs2.getInt("menuNumber"), rs2.getString("menuName"), rs2.getInt("menuCost")));
			}
			System.out.println("mysql : 메뉴 데이터베이스 재갱신 성공");
			
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
			System.out.println("mysql : 오더 데이터베이스 재갱신 성공\n");
			removeConnect();
			
			rs1.close();
			rs2.close();
			
		} catch (Exception e) {
			System.out.println("mysql : 오더 데이터베이스 재갱신 오류 발생\n");
		}
		
	}
	
	public void DbRenewal() {
		try {
			System.out.println("mysql : 데이터베이스 오토 인크리먼트 재갱신 시작");
			
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
			System.out.println("mysql : 메뉴 데이터베이스 오토 인크리먼트 재갱신 완료");
			
			
			
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
			System.out.println("mysql : 테이블 데이터베이스 오토 인크리먼트 재갱신 완료");
			
			
			
			sql1 = "ALTER TABLE tableorder AUTO_INCREMENT=1";
			sql2 = "SET @COUNT = 0";
			sql3 = "UPDATE tableorder SET orderNumber = @COUNT:=@COUNT+1";
			pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql2);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql3);
			pstmt.executeUpdate();
			System.out.println("mysql : 오더 데이터베이스 오토 인크리먼트 재갱신 완료");
			
			
			sql1 = "ALTER TABLE rsmaster AUTO_INCREMENT=1";
			sql2 = "SET @COUNT = 0";
			sql3 = "UPDATE rsmaster SET idx = @COUNT:=@COUNT+1";
			pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql2);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sql3);
			pstmt.executeUpdate();
			System.out.println("mysql : 카운터 데이터베이스 오토 인크리먼트 재갱신 완료\n");
			
		} catch (Exception e) {
			System.out.println("mysql : 데이터베이스 오토 인크리먼트 재갱신 오류발생\n");
		}
		
	}
	
	public ArrayList<Table> getTableList() {
		System.out.println("mysql : 겟 테이블 리스트 작동");
		return tableList;
	}
	
	public Table getTable(int index) {
		return tableList.get(index);
	}
	
	public ArrayList<Menu> getMenuList() {
		System.out.println("mysql : 겟 메뉴 리스트 작동");
		return menuList;
	}
	
	public Menu getMenu(int index) {
		return menuList.get(index);
	}
	
	public void reConnect() {
		try {
			
			System.out.println("mysql : mysql 재연결 메소드");
			conn = DriverManager.getConnection(url, uid, upass);
			st = conn.createStatement();
			sql = "";
			System.out.println("mysql : 재연결 성공\n");
			
		} catch (Exception e) {
			System.out.println("mysql : 재연결 실패\n");
		}
	}
	
	public void removeConnect() {
		try {
			System.out.println("mysql : 커넥트 삭제");
			conn.close();
			st.close();
			System.out.println("mysql : 커넥트 삭제 성공\n");
		} catch (Exception e) {
			System.out.println("mysql : 커넥트 삭제 실패\n");
		}
		
	}
	
	public void tableAdd() {
		try {
			
			System.out.println("mysql : 테이블 추가 시작");
			if (tableList.size() < 6) {
				int result = JOptionPane.showConfirmDialog(null, "테이블을 추가하시겠습니까?", "테이블 추가", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					reConnect();
					String sql1 = "insert into mytable() values()";
					PreparedStatement pstmt = conn.prepareStatement(sql1);
					pstmt.executeUpdate();
					System.out.println("mysql : 테이블 추가 성공\n");
					renewal();
					pstmt.close();
					JOptionPane.showMessageDialog(null, "테이블이 추가되었습니다\n현재 테이블 : " + tableList.size() + "개", "테이블 추가", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "테이블 추가가 취소되었습니다", "테이블 추가", JOptionPane.INFORMATION_MESSAGE);
					renewal();
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "테이블이 꽉 차있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
				renewal();
			}
			
		} catch (Exception e) {
			System.out.println("mysql : 테이블 추가 오류발생\n");
			renewal();
		}
	}
	
	public void tableDel() {
		try {
			System.out.println("mysql : 테이블 삭제 시작");
			
			if (tableList.size() > 1) {
				int result = JOptionPane.showConfirmDialog(null, "테이블을 삭제하시겠습니까?", "테이블 삭제", JOptionPane.YES_NO_OPTION);
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
						System.out.println("mysql : 테이블 삭제 성공\n");
						JOptionPane.showMessageDialog(null, "테이블이 삭제되었습니다\n현재 테이블 : " + tableList.size() + "개", "테이블 삭제", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(null, "결제되지 않은 내역이 있습니다.", "테이블 삭제", JOptionPane.INFORMATION_MESSAGE);
						renewal();
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "테이블 삭제가 취소되었습니다", "테이블 삭제", JOptionPane.INFORMATION_MESSAGE);
					renewal();
				}
			}
			
			else {
				JOptionPane.showMessageDialog(null, "테이블이 하나 이상 있어야 합니다.", "오류", JOptionPane.ERROR_MESSAGE);
				renewal();
			}
			
		} catch (Exception e) {
			System.out.println("mysql : 테이블 삭제 오류발생\n");
		}
	}
	
	public void tableReset(Table table) {
		try {
			
			System.out.println("mysql : 테이블" + table.getTableNumber() + "번 리셋 시작");

			renewal();
			reConnect();
			String sql1 = "UPDATE mytable SET customers = NULL, costTotal = 0, ownerCall = 0 WHERE tableNumber = " + table.getTableNumber();
			PreparedStatement pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			
			
			String sql2 = "DELETE FROM tableorder WHERE tableNumber = " + table.getTableNumber();
			pstmt = conn.prepareStatement(sql2);
			pstmt.executeUpdate();
			
			
			renewal();
			System.out.println("mysql : 테이블 리셋 성공\n");
			
		} catch (Exception e) {
			System.out.println("mysql : 테이블 리셋 실패\n");
		}
	}
	
	public void tableRenewal(Table table) {
		try {
			
			System.out.println("mysql : 테이블" + table.getTableNumber() + "번 갱신 시작");
			
			renewal();
			reConnect();
			String sql1 = "UPDATE mytable SET customers = '" + table.getCustomers() + "', costTotal = " + table.getCostTotal() + " WHERE tableNumber = " + table.getTableNumber();
			PreparedStatement pstmt = conn.prepareStatement(sql1);
			pstmt.executeUpdate();
			
			System.out.println("mysql : 테이블 갱신 성공\n");
			
		} catch (Exception e) {
			System.out.println("mysql : 테이블 갱신 실패\n");
		}
		
	}
	
	public void plusOrder(int tableNumber, String menuName, int count) {
		try {
			
			System.out.println("mysql : 오더 추가 시작");
			
			reConnect();
			
			String orderName = null;
			int orderCount = 0;
			int orderCost = 0;
			
			//테이블 넘버와 메뉴 이름에 맞는 오더를 가져온다. 이미 오더가 있다면 그 오더에 이프문이 반응하도록 한다.
			String sql1 = "select * from tableorder where tableNumber = " + tableNumber + " AND orderName = '" + menuName + "'";
			System.out.println("mysql : " + sql1);
			ResultSet rs = st.executeQuery(sql1);
			
			while (rs.next()) {
				orderName = rs.getString("orderName");
				orderCount = rs.getInt("orderCount");
				orderCost = rs.getInt("orderCost");
			}
			
			System.out.println("mysql : 오더 셀렉트 완료");
			
			if (menuName.equals(orderName) && orderCount > 0) {
				
				System.out.println("mysql : 이미 내역이 있음");
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
						System.out.println("이미 생성된 주문에 하나 추가됨");
						System.out.println("메뉴명 : " + order.getMenuName());
						System.out.println("총 주문개수 : " + order.getOrderCount());
						System.out.println("총 가격 : " + order.getOrderTotal());
						tableList.get(tableNumber - 1).renewTotal();
						JOptionPane.showMessageDialog(null, "주문이 성공하셨습니다.\n메뉴명 : "+ order.getMenuName() + "\n총 주문 개수 : "+ order.getOrderCount() +"개\n총 가격 : " + order.getOrderTotal()+"원", "주문", JOptionPane.INFORMATION_MESSAGE);
						
					}
				}
				
			}
			else {
				
				System.out.println("mysql : 내역이 없으니 추가함");
				
				
				for (Menu menu : menuList) {
					if (menu.getMenuName().equals(menuName)) {
						tableList.get(tableNumber - 1).getOrderList().add(new Order(tableList.get(tableNumber - 1).getOrderList().size(), tableNumber, menuName, menu.getMenuCost(), count, menu.getMenuCost() * count));
						tableList.get(tableNumber - 1).renewTotal();
						String sql2 = "INSERT INTO tableorder (tableNumber, orderName, orderCost, orderCount, orderTotal) values (" + tableNumber + ", '" + menu.getMenuName() + "', " + menu.getMenuCost() + ", " + count + ", " + menu.getMenuCost() + ")";
						PreparedStatement pstmt = conn.prepareStatement(sql2);
						pstmt.executeUpdate();
						
						JOptionPane.showMessageDialog(null, "주문이 성공하셨습니다.\n메뉴명 : "+ menu.getMenuName() + "\n총 주문 개수 : " + count + "개\n총 가격 : " + menu.getMenuCost() * count +"원", "주문", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
			
			
			reConnect();
			
			String sql2 = "UPDATE mytable SET costTotal = " + tableList.get(tableNumber - 1).getCostTotal() + " WHERE tableNumber = " + tableNumber;
			PreparedStatement pstmt = conn.prepareStatement(sql2);
			pstmt.executeUpdate();
			renewal();
			
			System.out.println("mysql : 오더 추가 성공\n");
			
		} catch (Exception e) {
			renewal();
			System.out.println("mysql : 오더 추가 실패\n");
		}
	}
	
	//메뉴 부분
	public void menuAdd(String menuName, int menuCost) {
		try {
			
			System.out.println("mysql : 메뉴 추가 시작");
			reConnect();
			String name = null;
			String sql = "select * from menu where menuName = '" + menuName + "'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				name = rs.getString("menuName");
			}
			
			if (menuName.equals(name)) {
				System.out.println("이미 같은 이름의 메뉴가 있습니다.\n");
				removeConnect();
				rs.close();
				JOptionPane.showMessageDialog(null, "이미 같은 이름의 메뉴가 존재합니다." ,"오류", JOptionPane.ERROR_MESSAGE);
			}
			
			else {
				if (menuCost >= 1000 && menuCost % 50 == 0) {
					String sql2 = "insert into menu (menuName, menuCost) values ('" + menuName +"', " + menuCost + ")";
					System.out.println("메뉴추가 쿼리 : " + sql2);
					PreparedStatement pstmt = conn.prepareStatement(sql2);
					pstmt.executeUpdate();
					renewal();
					System.out.println("mysql : 메뉴 추가 성공\n");
					removeConnect();
					pstmt.close();
					rs.close();
					JOptionPane.showMessageDialog(null,"메뉴가 추가되었습니다.\n메뉴명 : " + menuName + "\n가격 : "  + menuCost + "원" ,"메뉴 추가 완료", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					System.out.println("가격에 문제가 있습니다.\n");
					removeConnect();
					rs.close();
					JOptionPane.showMessageDialog(null, "가격에 문제가 있습니다." ,"오류", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			
		} catch (Exception e) {
			System.out.println("mysql : 메뉴 추가 실패\n");
			JOptionPane.showMessageDialog(null, "오류발생" ,"오류", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	public void menuDel(String menuName) {
		try {
			
			System.out.println("mysql : 메뉴 삭제 시작");
			reConnect();
			String name = null;
			sql = "select * from menu where menuName = '" + menuName + "'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				name = rs.getString("menuName");
			}
			
			if (menuName.equals(name)) {
				int result = JOptionPane.showConfirmDialog(null, menuName + "메뉴를 삭제하시겠습니까?", "메뉴 삭제", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					sql = "delete from menu where menuName = '" + menuName + "'";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();
					
					
					String sql2 = "DELETE FROM tableorder WHERE orderName = '" + menuName +"'";
					pstmt = conn.prepareStatement(sql2);
					pstmt.executeUpdate();
					
					renewal();
					System.out.println("mysql : 메뉴 삭제 성공\n");
					
					removeConnect();
					pstmt.close();
					rs.close();
					renewal();
					JOptionPane.showMessageDialog(null, "메뉴가 삭제되었습니다." ,"메뉴 삭제 완료", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					removeConnect();
					rs.close();
					renewal();
					JOptionPane.showMessageDialog(null, "메뉴 삭제가 취소되었습니다." ,"메뉴 삭제 취소", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			
			else {
				System.out.println("그런 메뉴는 없습니다.\n");
				removeConnect();
				rs.close();
				renewal();
				JOptionPane.showMessageDialog(null, "그런 메뉴는 존재하지 않음" ,"오류", JOptionPane.ERROR_MESSAGE);
			}
			
		} catch (Exception e) {
			System.out.println("mysql : 메뉴 삭제 실패\n");
			renewal();
			JOptionPane.showMessageDialog(null, "오류발생" ,"오류", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void plusCustomersCount() {
		try {
			System.out.println("mysql : 고객 카운트 추가 시작");
			
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
				System.out.println("mysql : 고객 카운트 추가 성공\n");
				removeConnect();
				pstmt.close();
				rs.close();
			}
			else {
				String sql3 = "INSERT INTO rsmaster (today, customerscnt) VALUES ('" + mydate.format(today) + "', 1)";
				PreparedStatement pstmt = conn.prepareStatement(sql3);
				pstmt.executeUpdate();
				System.out.println("mysql : 고객 카운트 추가 성공\n");
				removeConnect();
				pstmt.close();
				rs.close();
			}
			
		} catch (Exception e) {
			System.out.println("mysql : 고객 카운트 추가 실패\n");
		}
	}
	
	public int getCustomersCount() {
		try {
			
			System.out.println("mysql : 고객 카운트 불러오기 시작");
			reConnect();
			int Count = 0;
			Date today = new Date();
			SimpleDateFormat mydate = new SimpleDateFormat("yyyy-MM-dd");
			String sql1 = "SELECT * FROM rsmaster WHERE today = '" + mydate.format(today) + "'";
			ResultSet rs = st.executeQuery(sql1);
			while (rs.next()) {
				Count = rs.getInt("customerscnt");
			}
			System.out.println("mysql : 고객 카운트 불러오기 성공\n");
			return Count;
			
		} catch (Exception e) {
			System.out.println("mysql : 고객 카운트 불러오기 실패\n");
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
				System.out.println("mysql : 쓰레드에 드라이브 적재됨");
				
				conn = DriverManager.getConnection(url, uid, upass);
				System.out.println("mysql : 쓰레드에 드라이브에 DB 연동됨");
			} catch (Exception e) {
				System.out.println("mysql : 쓰레드에 DB 연동 오류 발생");
			}
		}
		
		@Override
		public void run() {
			System.out.println("mysql : 종업원 콜 쓰레드 작동 시작");
			try {
				
				do {
					
					st = conn.createStatement();
					String sql1 ="SELECT * FROM mytable";
					ResultSet rs = st.executeQuery(sql1);
					while (rs.next()) {
						int tableNumber = rs.getInt("tableNumber");
						int ownerCall = rs.getInt("ownerCall");
						
						if (ownerCall == 1) {
							int result = JOptionPane.showConfirmDialog(null, tableNumber + "번 테이블에서 호출입니다.", "호출", JOptionPane.YES_NO_OPTION);
							if (result == JOptionPane.YES_OPTION) {
								
								String sql2 = "UPDATE mytable SET ownerCall = 0 WHERE tableNumber = " + tableNumber;
								PreparedStatement pstmt = conn.prepareStatement(sql2);
								pstmt.executeUpdate();
								
								JOptionPane.showMessageDialog(null, "호출 확인되었습니다.");
							}
							else {
								JOptionPane.showMessageDialog(null, "호출 확인을 취소하셨습니다.\n잠시 후에 재호출 합니다.");
							}
						}
					}
					Thread.sleep(2000);
					
				} while (true);
				
			} catch (Exception e) {
				
				System.out.println("mysql : 종업원 콜 쓰레드 오류발생");
			}	
		}

	}
}
