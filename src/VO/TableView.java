package VO;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DB.DataBese;
import master.*;
import VO.*;

public class TableView extends JFrame implements ActionListener{
	private RestaurantView rs;
	private TableView tv;
	private ArrayList<String> orderCombo = new ArrayList<String>(); // 콤보박스에 저장할 스트링 리스트
	
	private DataBese admin;
	private Table table;
	private JPanel allPanel;
	
	private JButton refreshBtn;
	private JButton callBtn;
	private JLabel numberLabel;
	private JLabel myCustomerLabel;
	
	private JPanel customerPanel;
	private JLabel customerLabel;
	private JTextField customerText;
	private JButton customerBtn;
	
	private JPanel orderPanel;
	private JLabel orderLabel;
	private JLabel orderCostLabel;
	
	private JLabel orderCountLabel;
	private JButton orderCountPlus;
	private JButton orderCountMinus;
	private JButton orderCountClear;
	private int orderCount;
	
	private JLabel totalCostLabel;
	private JComboBox orderBox;
	private JButton orderBtn;
	
	private JButton viewOrderBtn;
	
	public TableView(RestaurantView rs, int tableNumber, DataBese admin) {
		
		try {
			this.rs = rs;
			this.tv = this;
			this.admin = admin;
			this.orderCount = 1;
			this.table = admin.getTable(tableNumber);
			System.out.println(table.getTableNumber() + "번 테이블 생성");
			
			for (Menu menu : admin.getMenuList()) {
				orderCombo.add(menu.getMenuName());
			}
			System.out.println("모든 변수에 값 저장 완료");
			
			setTitle("테이블");
			setBounds(100, 20, 600, 700);
			setBackground(new Color(255,255,255));
			System.out.println("프레임 세팅 완료");
			
			allPanel = new JPanel();
			allPanel.setLayout(null);
			allPanel.setBackground(new Color(255,255,255));
			System.out.println("올 패널 생성완료");
			
			numberLabel = new JLabel("테이블 번호 : " + (table.getTableNumber()));
			myCustomerLabel = new JLabel("고객 이름 : " + table.getCustomers());
			
			numberLabel.setBounds(235, 10, 130, 30);
			myCustomerLabel.setBounds(200, 40, 200, 30);
			
			numberLabel.setFont(new Font(getName(), 1, 18));
			myCustomerLabel.setFont(new Font(getName(), 1, 18));
			System.out.println("라벨 객체 생성");
			
			
			
			//새로고침
			ImageIcon refreshIcon = new ImageIcon("refresh.png");
			refreshBtn = new JButton(refreshIcon);
			refreshBtn.setBackground(new Color(255,255,255));
			refreshBtn.setBorder(null);
			refreshBtn.setBounds(10, 10, 50, 50);
			refreshBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					table = admin.getTable(tableNumber);
					myCustomerLabel.setText("고객 이름 : " + table.getCustomers());
					totalCostLabel.setText("총 가격 : " + table.getCostTotal());
					revalidate();
					repaint();
					
					System.out.println("새로고침 완료");
					
				}
			});
			
			
			//콜버튼
			ImageIcon callIcon = new ImageIcon("call.png");
			callBtn = new JButton(callIcon);
			callBtn.setBackground(new Color(255,255,255));
			callBtn.setBorder(null);
			callBtn.setBounds(520, 10, 50, 50);
			callBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					rs.ownerCall(table);
				}
			});
			
			
			
			
			
			// 고객 이름 변경하는 부분
			customerPanel = new JPanel();
			customerLabel = new JLabel("<고객이름 변경>");
			customerText = new JTextField();
			customerBtn = new JButton("이름등록");
			System.out.println("고객등록 생성됨");
			
			customerPanel.setBounds(0, 80, 600, 200);
			customerPanel.setLayout(null);
			customerPanel.setBackground(new Color(255,240,255));
			
			customerLabel.setBounds(230, 5, 150, 50);
			customerLabel.setFont(new Font(getName(), 1, 18));
			
			customerText.setBounds(200, 57, 200, 50);
			customerText.setFont(new Font(getName(), 1, 18));
			
			customerBtn.setBounds(240, 120, 120, 40);
			customerBtn.setFont(new Font(getName(), 1, 18));
			customerBtn.setBackground(new Color(255,255,255));
			customerBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						
						System.out.println("변경전 이름 : " + table.getCustomers());
						String setname = customerText.getText();
						table.setCustomers(setname);
						myCustomerLabel.setText("고객 이름 : " + table.getCustomers());
						customerText.setText("");
						System.out.println("저장완료");
						admin.tableRenewal(table);
						System.out.println("바뀐 이름 : " + table.getCustomers());
						rs.tableRenamed(table);
						JOptionPane.showMessageDialog(null, "이름이 변경되었습니다.\n변경된 이름 : " + table.getCustomers(), "이름 변경 완료", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e2) {
						System.out.println("알수없는 이유로 실패함");
					}
					
					
				}
			});
			System.out.println("고객등록 객체 설정 완료");
			
			customerPanel.add(customerLabel);
			customerPanel.add(customerText);
			customerPanel.add(customerBtn);
			System.out.println("고객등록 에드됨");
			
			
			
			
			
			
			
			
			// 새 주문하는 부분
			orderPanel = new JPanel();
			orderBox = new JComboBox(orderCombo.toArray(new String[orderCombo.size()]));
			
			//콤보박스에 어레이리스트를 등록하여 사용하기 위함에 있다.
			orderLabel = new JLabel("<새 주문>");
			if (orderCombo.isEmpty() == false) {
				orderCostLabel = new JLabel("가격 : " + admin.getMenuList().get(orderBox.getSelectedIndex()).getMenuCost() + "원");
			}
			else {
				orderCostLabel = new JLabel("가격 : 0원");
			}
			
			orderCountPlus = new JButton("+");
			orderCountMinus = new JButton("-");
			orderCountClear = new JButton("C");
			orderCountLabel = new JLabel("주문 수량 : " + orderCount + "개");
			
			orderBtn = new JButton("주문하기");
			totalCostLabel = new JLabel("총 가격 : " + table.getCostTotal() + "원");

			System.out.println("메뉴추가 생성됨");
			
			orderPanel.setLayout(null);
			orderPanel.setBounds(0, 280, 600, 300);
			orderPanel.setBackground(new Color(255,255,230));
			
			orderLabel.setBounds(260, 20, 90, 30);
			orderLabel.setFont(new Font(getName(), 1, 18));
			
			orderBox.setBounds(200, 65, 200, 30);
			orderBox.setBackground(new Color(255,255,255));
			orderBox.setFont(new Font(getName(), 1, 18));
			orderBox.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						System.out.println("선택된 인덱스 : " + orderBox.getSelectedIndex());
						orderCostLabel.setText("가격 : " + admin.getMenuList().get(orderBox.getSelectedIndex()).getMenuCost() + "원");
					} catch (Exception e2) {
						orderCostLabel.setText("오류발생");
					}
				}
			});
			
			orderCostLabel.setBounds(200, 110, 200, 20);
			orderCostLabel.setFont(new Font(getName(), 1, 18));
			
			
			orderCountLabel.setBounds(200, 140, 200, 20);
			orderCountLabel.setFont(new Font(getName(), 1, 18));
			
			orderCountPlus.setBounds(225, 165, 45, 45);
			orderCountPlus.setFont(new Font(getName(), 1, 16));
			orderCountPlus.setBackground(Color.WHITE);
			
			orderCountMinus.setBounds(280, 165, 45, 45);
			orderCountMinus.setFont(new Font(getName(), 1, 16));
			orderCountMinus.setBackground(Color.WHITE);
			
			orderCountClear.setBounds(335, 165, 45, 45);
			orderCountClear.setFont(new Font(getName(), 1, 15));
			orderCountClear.setBackground(Color.WHITE);
			
			
			ActionListener countbtn = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if (e.getActionCommand().equals("+")) {
							System.out.println("플러스버튼 누름");
							orderCount++;
						}
						else if (e.getActionCommand().equals("-")) {
							System.out.println("마이너스버튼 누름");
							
							if (orderCount <= 1) {
								orderCount = 1;
								JOptionPane.showMessageDialog(null, "주문 수량은 1이하로 내려갈 수 없습니다.", "카운터 오류", JOptionPane.ERROR_MESSAGE);
							}
							else {
								orderCount--;
								
							}
							
						}
						else if (e.getActionCommand().equals("C")) {
							System.out.println("클리어버튼 누름");
							orderCount = 1;
							JOptionPane.showMessageDialog(null, "주문 수량을 1로 초기화했습니다.", "카운터 초기화", JOptionPane.INFORMATION_MESSAGE);
							
						}
						orderCostLabel.setText("가격 : " + admin.getMenuList().get(orderBox.getSelectedIndex()).getMenuCost() * orderCount + "원");
						orderCountLabel.setText("주문 수량 : " + orderCount + "개");
						
					} catch (Exception e2) {
						System.out.println("카운터 버튼 액션리스너 오류 발생");
					}
				}
			};
			
			orderCountPlus.addActionListener(countbtn);
			orderCountMinus.addActionListener(countbtn);
			orderCountClear.addActionListener(countbtn);
			
			
			orderBtn.setBounds(240, 220, 120, 40);
			orderBtn.setFont(new Font(getName(), 1, 18));
			orderBtn.setBackground(new Color(255,255,255));
			orderBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if (table.getCustomers() != null) {
							int result = JOptionPane.showConfirmDialog(null, "주문하시겠습니까?\n메뉴이름 : " + orderBox.getSelectedItem() +"\n"+ orderCostLabel.getText(), "주문", JOptionPane.YES_NO_OPTION);
							
							if (result == JOptionPane.YES_OPTION) {
									admin.plusOrder(table.getTableNumber(), (String)orderBox.getSelectedItem(), orderCount);;
									table = admin.getTable(tableNumber);
									totalCostLabel.setText("총 가격 : " + table.getCostTotal());
									orderCount = 1;
									orderCountLabel.setText("주문 수량 : " + orderCount + "개");
									System.out.println("메뉴 주문 메소드 작동 성공\n");
							}
							else {
								JOptionPane.showMessageDialog(null, "주문이 취소되었습니다", "취소", JOptionPane.INFORMATION_MESSAGE);
							}
						}		
						else {
							JOptionPane.showMessageDialog(null, "주문자 이름이 없습니다.", "오류", JOptionPane.INFORMATION_MESSAGE);
						}
						
					} catch (Exception e2) {
						System.out.println("메뉴 주문 메소드 오류발생\n");
						JOptionPane.showMessageDialog(null, "오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
					}
					
				}
			});
			
			totalCostLabel.setBounds(200, 260, 200, 30);
			totalCostLabel.setFont(new Font(getName(), 1, 18));
			
			
			System.out.println("메뉴추가 객체 생성 완료됨");
			
			
			viewOrderBtn = new JButton("주문상세");
			viewOrderBtn.setBounds(200, 605, 200, 35);
			viewOrderBtn.setBackground(new Color(255,255,255));
			viewOrderBtn.setFont(new Font(getName(), 1, 18));
			viewOrderBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					TableOrderView TOV = new TableOrderView(rs, tv, table.getTableNumber() - 1, admin);
				}
			});
			
			
			orderPanel.add(orderLabel);
			orderPanel.add(orderBox);
			orderPanel.add(orderCostLabel);
			orderPanel.add(orderCountLabel);
			orderPanel.add(orderCountPlus);
			orderPanel.add(orderCountMinus);
			orderPanel.add(orderCountClear);
			orderPanel.add(orderBtn);
			orderPanel.add(totalCostLabel);
			
			
			allPanel.add(refreshBtn);
			allPanel.add(callBtn);
			allPanel.add(numberLabel);
			allPanel.add(myCustomerLabel);
			allPanel.add(customerPanel);
			allPanel.add(orderPanel);
			allPanel.add(viewOrderBtn);
			
			add(allPanel);
			System.out.println("올패널 그래픽 생성됨");
			
			setVisible(true);
			System.out.println("테이블 뷰 생성완료\n");
			
			this.addWindowListener(new WindowListener() {
				
				@Override
				public void windowOpened(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowIconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowDeiconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowDeactivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowClosing(WindowEvent e) {
					tv.dispose();
					rs.setVisible(true);
					
				}
				
				@Override
				public void windowClosed(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowActivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
		} catch (Exception e) {
			System.out.println("알수없는 이유로 생성되지 않음");
		}
		
	}
	
	public void setCustomers(String name, int index) {
		admin.getTable(index).setCustomers(name);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("잘못된 접근");
	}

	public static class restartThread implements Runnable{
		private RestaurantView rs;
		private TableView tv;
		
		public restartThread(RestaurantView rs, TableView tv) {
			this.tv = tv;
		}
		@Override
		public void run() {
			System.out.println("새로고침 쓰레드 작동");
			try {
				tv.revalidate();
				tv.repaint();
			} catch (Exception e) {
				System.out.println("오류발생");
			}	
		}

	}
}
