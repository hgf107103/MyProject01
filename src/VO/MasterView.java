package VO;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DB.DataBese;
import master.Menu;

public class MasterView extends JFrame implements ActionListener{
	
	private JPanel allPanel;
	private JPanel createTPanel;
	private JPanel createMPanel;
	private JPanel deleteMPanel;
	
	private JButton createTable;
	private JButton deleteTable;
	
	private JButton createMenu;
	private JLabel menuNameLabel;
	private JTextField menuNameText;
	private JLabel menuCostLabel;
	private JTextField menuCostText;
	
	private JButton deleteMenu;
	private JComboBox<String> menuBox;
	private ArrayList<String> menuCombo = new ArrayList<String>();
	
	private JPanel restartPanel;
	private JLabel restart;
	private JButton restartPower;
	
	private boolean restartCheak;
	
	public MasterView(RestaurantView rs, DataBese admin) {
		setTitle("마스터");
		setBounds(20, 20, 328, 735);
		setResizable(false);
		
		allPanel = new JPanel();
		
		
		
		//테이블 패널
		createTPanel = new JPanel();
		createTable = new JButton("테이블 생성");
		deleteTable = new JButton("테이블 삭제");
		
		allPanel.setLayout(null);
		allPanel.setBounds(0, 0, 312, 725);
		allPanel.setBackground(new Color(255,255,255));
		
		createTPanel.setLayout(null);
		createTPanel.setBounds(1, 1, 310, 170);
		createTPanel.setBackground(new Color(255,255,220));
		
		createTable.setBounds(80, 30, 150, 50);
		createTable.setFont(new Font(getName(), 1, 18));
		createTable.setBackground(new Color(255,255,255));
		
		deleteTable.setBounds(80, 90, 150, 50);
		deleteTable.setFont(new Font(getName(), 1, 18));
		deleteTable.setBackground(new Color(255,255,255));
		
		createTable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					admin.tableAdd();
					rs.tableSet();
					System.out.println("테이블 생성 메소드 작동 성공\n");
				} catch (Exception e2) {
					System.out.println("테이블 생성 메소드 오류발생\n");
					admin.renewal();
				}
				
			}
		});
		
		deleteTable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					admin.tableDel();
					rs.tableSet();
					System.out.println("테이블 삭제 메소드 작동 성공\n");
				} catch (Exception e2) {
					System.out.println("테이블 삭제 메소드 오류발생\n");
					admin.renewal();
				}
				
			}
		});
		
		createTPanel.add(createTable);
		createTPanel.add(deleteTable);
		
		
		
		//메뉴생성 패널
		createMPanel = new JPanel();
		createMenu = new JButton("메뉴 생성");
		menuNameLabel = new JLabel("메뉴 이름 설정");
		menuNameText = new JTextField();
		menuCostLabel = new JLabel("메뉴 가격 설정");
		menuCostText = new JTextField();
		createMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					String name = menuNameText.getText();
					int cost = 0;
					try {
						cost = Integer.parseInt(menuCostText.getText());
						System.out.println("메뉴명 : " + name + "\n가격 : " + cost);
						rs.getAdmin().menuAdd(name, cost);
						menuBox.addItem(name);
						System.out.println("매뉴 생성 메소드 작동 성공\n");
					} catch (Exception e2) {
						System.out.println("가격은 문자열로 입력할 수 없습니다.\n");
					}
					menuNameText.setText("");
					menuCostText.setText("");
					
				} catch (Exception e2) {
					System.out.println("메뉴 생성 메소드 작동 오류발생\n");
				}
				
			}
		});
		
		createMPanel.setLayout(null);
		createMPanel.setBounds(1, 172, 310, 240);
		createMPanel.setBackground(new Color(255,220,255));
		
		menuNameLabel.setBounds(80, 30, 150, 20);
		menuNameLabel.setFont(new Font(getName(), 1, 18));
		
		menuNameText.setBounds(15, 55, 280, 35);
		menuNameText.setFont(new Font(getName(), 1, 16));
		menuNameText.setHorizontalAlignment(JTextField.CENTER);
		
		menuCostLabel.setBounds(80, 105, 150, 20);
		menuCostLabel.setFont(new Font(getName(), 1, 18));
		
		menuCostText.setBounds(15, 130, 280, 35);
		menuCostText.setFont(new Font(getName(), 1, 16));
		menuCostText.setHorizontalAlignment(JTextField.CENTER);
		
		createMenu.setBounds(80, 180, 150, 30);
		createMenu.setFont(new Font(getName(), 1, 18));
		createMenu.setBackground(new Color(255,255,255));
		
		createMPanel.add(menuNameLabel);
		createMPanel.add(menuNameText);
		createMPanel.add(menuCostLabel);
		createMPanel.add(menuCostText);
		createMPanel.add(createMenu);
		
		for (Menu menu : admin.getMenuList()) {
			menuCombo.add(menu.getMenuName());
		}
		
		menuBox = new JComboBox(menuCombo.toArray(new String[menuCombo.size()]));
		
		
		deleteMPanel = new JPanel();
		deleteMPanel.setLayout(null);
		deleteMPanel.setBounds(1, 413, 310, 140);
		deleteMPanel.setBackground(new Color(220,255,255));
		
		menuBox.setBounds(30, 30, 250, 35);
		menuBox.setFont(new Font(getName(), 1, 18));
		menuBox.setBackground(new Color(255,255,255));
		
		deleteMenu = new JButton("메뉴 삭제");
		deleteMenu.setBounds(80, 80, 150, 30);
		deleteMenu.setFont(new Font(getName(), 1, 18));
		deleteMenu.setBackground(new Color(255,255,255));
		
		deleteMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (admin.getMenuList().size() > 1) {
						String menuName = (String) menuBox.getSelectedItem();
						admin.menuDel(menuName);
						
						
						
						System.out.println("삭제된 메뉴 : " + menuName);
						System.out.println("매뉴 삭제 메소드 작동 성공\n");
						menuBox.removeItem(menuBox.getSelectedItem());
						menuBox.setSelectedIndex(0);
					}
					else {
						System.out.println("메뉴가 최소한 하나이상 있어야 합니다.");
						System.out.println("매뉴 삭제 메소드 작동 성공\n");
					}
				} catch (Exception e2) {
					System.out.println("매뉴 삭제 메소드 작동 오류발생\n");
				}
				
			}
		});
		
		
		restartPanel = new JPanel();
		/*restartCheak = (boolean) rs.getThread().getCheak();
		
		if (restartCheak) {
			restart = new JLabel("자동 새로고침 : 켜져있음");
			restartPower = new JButton("끄기");
		}
		else {
			restart = new JLabel("자동 새로고침 : 꺼져있음");
			restartPower = new JButton("켜기");
		}
		
		restartPower.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (restartCheak) {
					rs.getThread().onOff();
					restart.setText("자동 새로고침 : 꺼져있음");
					restartPower.setText("켜기");
					restartCheak = false;
					JOptionPane.showMessageDialog(null, "새로고침 쓰레드가 꺼졌습니다", "알림", JOptionPane.INFORMATION_MESSAGE);
					System.out.println("쓰레드 자동 갱신 종료");
				}
				else {
					rs.getThread().onOff();
					restart.setText("자동 새로고침 : 켜져있음");
					restartPower.setText("끄기");
					restartCheak = true;
					JOptionPane.showMessageDialog(null, "새로고침 쓰레드가 켜졌습니다", "알림", JOptionPane.INFORMATION_MESSAGE);
					System.out.println("쓰레드 자동 갱신 작동");
				}
				
				
			}
		});*/
		
		restartPanel.setBounds(1, 554, 310, 140);
		restartPanel.setBackground(new Color(250,230,250));
		restartPanel.setLayout(null);
		
		restart.setBounds(30, 30, 250, 30);
		restart.setFont(new Font(getName(), 1, 22));
		
		restartPower.setBounds(105, 80, 100, 30);
		restartPower.setBackground(new Color(255,255,255));
		restartPower.setFont(new Font(getName(), 1, 20));
		
		restartPanel.add(restart);
		restartPanel.add(restartPower);
		
		deleteMPanel.add(menuBox);
		deleteMPanel.add(deleteMenu);
		
		allPanel.add(createTPanel);
		allPanel.add(createMPanel);
		allPanel.add(deleteMPanel);
		allPanel.add(restartPanel);
		
		add(allPanel);
		
		
		
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("잘못됨");
	}
}
