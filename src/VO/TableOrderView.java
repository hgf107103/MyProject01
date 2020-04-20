package VO;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import DB.DataBese;
import master.*;

public class TableOrderView extends JFrame{
	
	private Table table;
	private DataBese admin;
	
	private JPanel allPanel;
	private JLabel title;
	private JLabel name;
	
	private ArrayList<JPanel> menuPanel = new ArrayList<JPanel>();
	private ArrayList<JLabel> menuName = new ArrayList<JLabel>();
	private ArrayList<JLabel> menuCount = new ArrayList<JLabel>();
	private ArrayList<JLabel> menuCost = new ArrayList<JLabel>();
	private int total = 0;
	
	private JLabel totalCost;
	private JButton payment; 
	
	public TableOrderView(RestaurantView rs, TableView tv, int index, DataBese admin) {
		try {
			table = admin.getTable(index);
			int panelY = 100;
			setTitle("주문조회");
			allPanel = new JPanel();
			allPanel.setBackground(new Color(255,255,255));
			allPanel.setLayout(null);
			
			title = new JLabel("상세보기");
			title.setBounds(190, 15, 120, 20);
			title.setFont(new Font(getName(), 1, 20));
			
			name = new JLabel("주문자 : " + table.getCustomers());
			name.setBounds(180, 45, 200, 20);
			name.setFont(new Font(getName(), 1, 20));
			
			for (Order order : table.getOrderList()) {
				menuPanel.add(new JPanel());
				int size = menuPanel.size();
				menuPanel.get(size - 1).setLayout(null);
				menuPanel.get(size - 1).setBounds(0, panelY, 500, 120);
				menuPanel.get(size - 1).setBackground(new Color(255,240,255));
				
				menuName.add(new JLabel("메뉴이름 : " + order.getMenuName()));
				menuName.get(size - 1).setBounds(190, 15, 200, 20);
				menuName.get(size - 1).setFont(new Font(getName(), 1, 18));
				
				menuCount.add(new JLabel("주문 수 : " + String.valueOf(order.getOrderCount())));
				menuCount.get(size - 1).setBounds(200, 50, 100, 20);
				menuCount.get(size - 1).setFont(new Font(getName(), 1, 18));
				
				menuCost.add(new JLabel("가격 : " + String.valueOf(order.getOrderTotal()) + "원"));
				menuCost.get(size - 1).setBounds(190, 85, 200, 20);
				menuCost.get(size - 1).setFont(new Font(getName(), 1, 18));
				
				
				menuPanel.get(size - 1).add(menuName.get(size - 1));
				menuPanel.get(size - 1).add(menuCount.get(size - 1));
				menuPanel.get(size - 1).add(menuCost.get(size - 1));	
				allPanel.add(menuPanel.get(size - 1));
				
				panelY += 125;
				
				total = total + order.getOrderTotal();
			}
			
			totalCost = new JLabel("총액 : " + total + "원");
			totalCost.setBounds(180, (panelY + 20), 200, 30);
			totalCost.setFont(new Font(getName(), 1, 20));
			
			payment = new JButton("계산");
			payment.setBounds(180, (panelY + 70), 110, 30);
			payment.setFont(new Font(getName(), 1, 20));
			payment.setBackground(Color.WHITE);
			
			payment.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = JOptionPane.showConfirmDialog(null, "계산하시겠습니까?\n도합 : " + table.getCostTotal(), "계산", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						admin.plusCustomersCount();
						receipt(table, admin);
						JOptionPane.showMessageDialog(null, "결재되었습니다.\n오늘 결재 고객 : " + admin.getCustomersCount(), "계산", JOptionPane.INFORMATION_MESSAGE);
						admin.tableReset(table);
						rs.tableSet();
						System.out.println("계산 완료");
						tv.dispose();
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, "결재가 취소되었습니다", "계산", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
			
			setBounds(720, 20, 500, 270 + (table.getOrderList().size() * 120));
			
			allPanel.add(title);
			allPanel.add(name);
			allPanel.add(totalCost);
			allPanel.add(payment);
			
			add(allPanel);
			
			setVisible(true);
			
			
		} catch (Exception e) {
			System.out.println("테이블 오더 뷰 오류발생");
		}
		
		
	}

	public void receipt(Table table, DataBese admin) {
		PrintWriter out = null;
		try {
			Date date = new Date();
			SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat call = new SimpleDateFormat("ddMMyyyy");
			String txtname = today.format(date) +"-"+ admin.getCustomersCount() + ".txt";
			out = new PrintWriter(new FileWriter("receipt/" + txtname));
			
			out.println("====== 영수증 ======");
			out.println("\n날짜 : " + today.format(date));
			out.println("\n주문번호 : " + call.format(date) + admin.getCustomersCount());
			out.println("고객 이름 : " + table.getCustomers());
			out.println("테이블 번호 : " + (table.getTableNumber()));
			out.println("\n====== 주문내역 ======\n");
			for (Order order : table.getOrderList()) {
				out.println("메뉴 이름 : " + order.getMenuName());
				out.println("메뉴 번호 : " + order.getOrderNumber());
				out.println("개수 : " + order.getOrderCount() + "개");
				out.println("가격 : " + order.getOrderTotal() + "원\n");
			}
			out.println("====================");
			out.println("\n이용해주셔서 감사합니다.");
			out.close();
			System.out.println("영수증 출력 성공");
		} catch (Exception e) {
			System.out.println("영수증 출력 실패");
		}
	}
}

