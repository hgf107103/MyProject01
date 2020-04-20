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
			setTitle("�ֹ���ȸ");
			allPanel = new JPanel();
			allPanel.setBackground(new Color(255,255,255));
			allPanel.setLayout(null);
			
			title = new JLabel("�󼼺���");
			title.setBounds(190, 15, 120, 20);
			title.setFont(new Font(getName(), 1, 20));
			
			name = new JLabel("�ֹ��� : " + table.getCustomers());
			name.setBounds(180, 45, 200, 20);
			name.setFont(new Font(getName(), 1, 20));
			
			for (Order order : table.getOrderList()) {
				menuPanel.add(new JPanel());
				int size = menuPanel.size();
				menuPanel.get(size - 1).setLayout(null);
				menuPanel.get(size - 1).setBounds(0, panelY, 500, 120);
				menuPanel.get(size - 1).setBackground(new Color(255,240,255));
				
				menuName.add(new JLabel("�޴��̸� : " + order.getMenuName()));
				menuName.get(size - 1).setBounds(190, 15, 200, 20);
				menuName.get(size - 1).setFont(new Font(getName(), 1, 18));
				
				menuCount.add(new JLabel("�ֹ� �� : " + String.valueOf(order.getOrderCount())));
				menuCount.get(size - 1).setBounds(200, 50, 100, 20);
				menuCount.get(size - 1).setFont(new Font(getName(), 1, 18));
				
				menuCost.add(new JLabel("���� : " + String.valueOf(order.getOrderTotal()) + "��"));
				menuCost.get(size - 1).setBounds(190, 85, 200, 20);
				menuCost.get(size - 1).setFont(new Font(getName(), 1, 18));
				
				
				menuPanel.get(size - 1).add(menuName.get(size - 1));
				menuPanel.get(size - 1).add(menuCount.get(size - 1));
				menuPanel.get(size - 1).add(menuCost.get(size - 1));	
				allPanel.add(menuPanel.get(size - 1));
				
				panelY += 125;
				
				total = total + order.getOrderTotal();
			}
			
			totalCost = new JLabel("�Ѿ� : " + total + "��");
			totalCost.setBounds(180, (panelY + 20), 200, 30);
			totalCost.setFont(new Font(getName(), 1, 20));
			
			payment = new JButton("���");
			payment.setBounds(180, (panelY + 70), 110, 30);
			payment.setFont(new Font(getName(), 1, 20));
			payment.setBackground(Color.WHITE);
			
			payment.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					int result = JOptionPane.showConfirmDialog(null, "����Ͻðڽ��ϱ�?\n���� : " + table.getCostTotal(), "���", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						admin.plusCustomersCount();
						receipt(table, admin);
						JOptionPane.showMessageDialog(null, "����Ǿ����ϴ�.\n���� ���� �� : " + admin.getCustomersCount(), "���", JOptionPane.INFORMATION_MESSAGE);
						admin.tableReset(table);
						rs.tableSet();
						System.out.println("��� �Ϸ�");
						tv.dispose();
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(null, "���簡 ��ҵǾ����ϴ�", "���", JOptionPane.INFORMATION_MESSAGE);
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
			System.out.println("���̺� ���� �� �����߻�");
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
			
			out.println("====== ������ ======");
			out.println("\n��¥ : " + today.format(date));
			out.println("\n�ֹ���ȣ : " + call.format(date) + admin.getCustomersCount());
			out.println("�� �̸� : " + table.getCustomers());
			out.println("���̺� ��ȣ : " + (table.getTableNumber()));
			out.println("\n====== �ֹ����� ======\n");
			for (Order order : table.getOrderList()) {
				out.println("�޴� �̸� : " + order.getMenuName());
				out.println("�޴� ��ȣ : " + order.getOrderNumber());
				out.println("���� : " + order.getOrderCount() + "��");
				out.println("���� : " + order.getOrderTotal() + "��\n");
			}
			out.println("====================");
			out.println("\n�̿����ּż� �����մϴ�.");
			out.close();
			System.out.println("������ ��� ����");
		} catch (Exception e) {
			System.out.println("������ ��� ����");
		}
	}
}

