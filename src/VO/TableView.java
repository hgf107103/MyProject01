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
	private ArrayList<String> orderCombo = new ArrayList<String>(); // �޺��ڽ��� ������ ��Ʈ�� ����Ʈ
	
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
			System.out.println(table.getTableNumber() + "�� ���̺� ����");
			
			for (Menu menu : admin.getMenuList()) {
				orderCombo.add(menu.getMenuName());
			}
			System.out.println("��� ������ �� ���� �Ϸ�");
			
			setTitle("���̺�");
			setBounds(100, 20, 600, 700);
			setBackground(new Color(255,255,255));
			System.out.println("������ ���� �Ϸ�");
			
			allPanel = new JPanel();
			allPanel.setLayout(null);
			allPanel.setBackground(new Color(255,255,255));
			System.out.println("�� �г� �����Ϸ�");
			
			numberLabel = new JLabel("���̺� ��ȣ : " + (table.getTableNumber()));
			myCustomerLabel = new JLabel("�� �̸� : " + table.getCustomers());
			
			numberLabel.setBounds(235, 10, 130, 30);
			myCustomerLabel.setBounds(200, 40, 200, 30);
			
			numberLabel.setFont(new Font(getName(), 1, 18));
			myCustomerLabel.setFont(new Font(getName(), 1, 18));
			System.out.println("�� ��ü ����");
			
			
			
			//���ΰ�ħ
			ImageIcon refreshIcon = new ImageIcon("refresh.png");
			refreshBtn = new JButton(refreshIcon);
			refreshBtn.setBackground(new Color(255,255,255));
			refreshBtn.setBorder(null);
			refreshBtn.setBounds(10, 10, 50, 50);
			refreshBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					table = admin.getTable(tableNumber);
					myCustomerLabel.setText("�� �̸� : " + table.getCustomers());
					totalCostLabel.setText("�� ���� : " + table.getCostTotal());
					revalidate();
					repaint();
					
					System.out.println("���ΰ�ħ �Ϸ�");
					
				}
			});
			
			
			//�ݹ�ư
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
			
			
			
			
			
			// �� �̸� �����ϴ� �κ�
			customerPanel = new JPanel();
			customerLabel = new JLabel("<���̸� ����>");
			customerText = new JTextField();
			customerBtn = new JButton("�̸����");
			System.out.println("����� ������");
			
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
						
						System.out.println("������ �̸� : " + table.getCustomers());
						String setname = customerText.getText();
						table.setCustomers(setname);
						myCustomerLabel.setText("�� �̸� : " + table.getCustomers());
						customerText.setText("");
						System.out.println("����Ϸ�");
						admin.tableRenewal(table);
						System.out.println("�ٲ� �̸� : " + table.getCustomers());
						rs.tableRenamed(table);
						JOptionPane.showMessageDialog(null, "�̸��� ����Ǿ����ϴ�.\n����� �̸� : " + table.getCustomers(), "�̸� ���� �Ϸ�", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception e2) {
						System.out.println("�˼����� ������ ������");
					}
					
					
				}
			});
			System.out.println("����� ��ü ���� �Ϸ�");
			
			customerPanel.add(customerLabel);
			customerPanel.add(customerText);
			customerPanel.add(customerBtn);
			System.out.println("����� �����");
			
			
			
			
			
			
			
			
			// �� �ֹ��ϴ� �κ�
			orderPanel = new JPanel();
			orderBox = new JComboBox(orderCombo.toArray(new String[orderCombo.size()]));
			
			//�޺��ڽ��� ��̸���Ʈ�� ����Ͽ� ����ϱ� ���Կ� �ִ�.
			orderLabel = new JLabel("<�� �ֹ�>");
			if (orderCombo.isEmpty() == false) {
				orderCostLabel = new JLabel("���� : " + admin.getMenuList().get(orderBox.getSelectedIndex()).getMenuCost() + "��");
			}
			else {
				orderCostLabel = new JLabel("���� : 0��");
			}
			
			orderCountPlus = new JButton("+");
			orderCountMinus = new JButton("-");
			orderCountClear = new JButton("C");
			orderCountLabel = new JLabel("�ֹ� ���� : " + orderCount + "��");
			
			orderBtn = new JButton("�ֹ��ϱ�");
			totalCostLabel = new JLabel("�� ���� : " + table.getCostTotal() + "��");

			System.out.println("�޴��߰� ������");
			
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
						System.out.println("���õ� �ε��� : " + orderBox.getSelectedIndex());
						orderCostLabel.setText("���� : " + admin.getMenuList().get(orderBox.getSelectedIndex()).getMenuCost() + "��");
					} catch (Exception e2) {
						orderCostLabel.setText("�����߻�");
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
							System.out.println("�÷�����ư ����");
							orderCount++;
						}
						else if (e.getActionCommand().equals("-")) {
							System.out.println("���̳ʽ���ư ����");
							
							if (orderCount <= 1) {
								orderCount = 1;
								JOptionPane.showMessageDialog(null, "�ֹ� ������ 1���Ϸ� ������ �� �����ϴ�.", "ī���� ����", JOptionPane.ERROR_MESSAGE);
							}
							else {
								orderCount--;
								
							}
							
						}
						else if (e.getActionCommand().equals("C")) {
							System.out.println("Ŭ�����ư ����");
							orderCount = 1;
							JOptionPane.showMessageDialog(null, "�ֹ� ������ 1�� �ʱ�ȭ�߽��ϴ�.", "ī���� �ʱ�ȭ", JOptionPane.INFORMATION_MESSAGE);
							
						}
						orderCostLabel.setText("���� : " + admin.getMenuList().get(orderBox.getSelectedIndex()).getMenuCost() * orderCount + "��");
						orderCountLabel.setText("�ֹ� ���� : " + orderCount + "��");
						
					} catch (Exception e2) {
						System.out.println("ī���� ��ư �׼Ǹ����� ���� �߻�");
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
							int result = JOptionPane.showConfirmDialog(null, "�ֹ��Ͻðڽ��ϱ�?\n�޴��̸� : " + orderBox.getSelectedItem() +"\n"+ orderCostLabel.getText(), "�ֹ�", JOptionPane.YES_NO_OPTION);
							
							if (result == JOptionPane.YES_OPTION) {
									admin.plusOrder(table.getTableNumber(), (String)orderBox.getSelectedItem(), orderCount);;
									table = admin.getTable(tableNumber);
									totalCostLabel.setText("�� ���� : " + table.getCostTotal());
									orderCount = 1;
									orderCountLabel.setText("�ֹ� ���� : " + orderCount + "��");
									System.out.println("�޴� �ֹ� �޼ҵ� �۵� ����\n");
							}
							else {
								JOptionPane.showMessageDialog(null, "�ֹ��� ��ҵǾ����ϴ�", "���", JOptionPane.INFORMATION_MESSAGE);
							}
						}		
						else {
							JOptionPane.showMessageDialog(null, "�ֹ��� �̸��� �����ϴ�.", "����", JOptionPane.INFORMATION_MESSAGE);
						}
						
					} catch (Exception e2) {
						System.out.println("�޴� �ֹ� �޼ҵ� �����߻�\n");
						JOptionPane.showMessageDialog(null, "������ �߻��߽��ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
					}
					
				}
			});
			
			totalCostLabel.setBounds(200, 260, 200, 30);
			totalCostLabel.setFont(new Font(getName(), 1, 18));
			
			
			System.out.println("�޴��߰� ��ü ���� �Ϸ��");
			
			
			viewOrderBtn = new JButton("�ֹ���");
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
			System.out.println("���г� �׷��� ������");
			
			setVisible(true);
			System.out.println("���̺� �� �����Ϸ�\n");
			
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
			System.out.println("�˼����� ������ �������� ����");
		}
		
	}
	
	public void setCustomers(String name, int index) {
		admin.getTable(index).setCustomers(name);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("�߸��� ����");
	}

	public static class restartThread implements Runnable{
		private RestaurantView rs;
		private TableView tv;
		
		public restartThread(RestaurantView rs, TableView tv) {
			this.tv = tv;
		}
		@Override
		public void run() {
			System.out.println("���ΰ�ħ ������ �۵�");
			try {
				tv.revalidate();
				tv.repaint();
			} catch (Exception e) {
				System.out.println("�����߻�");
			}	
		}

	}
}
