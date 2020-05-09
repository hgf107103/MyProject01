package VO;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import DB.DataBese;
import master.*;

public class RestaurantView extends JFrame implements ActionListener{

	private ArrayList<JPanel> tbPanelList = new ArrayList<JPanel>();
	private ArrayList<JLabel> tbNumberLabel = new ArrayList<JLabel>();
	private ArrayList<JLabel> tbNameLabel = new ArrayList<JLabel>();
	private ArrayList<MyButton> tbButton = new ArrayList<MyButton>();
	
	private JPanel tablePanel;
	private JPanel buttonPanel;
	private JButton goMasterView;
	private JButton reset;
	
	private DataBese admin;
	
	private RestaurantView rs = this;
	private restartThread rt;
	
	
	public RestaurantView(String uid, String upass) {
		admin = DataBese.getInstance();
		admin.setDataBese(uid, upass);
		
		System.out.println("����");
		setTitle("�������");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(600, 200, 800, 500);
		setResizable(false);
		
		
		//������ ����
		tablePanel = new JPanel(); //���̺��� �߰��� �г�
		buttonPanel = new JPanel(); // ��ư �г�
		tablePanel.setLayout(new GridLayout(2, 3, 2, 2)); //���̺��� 2�� 3���� �����ϴ� ���̾ƿ�
		buttonPanel.setLayout(null);
		tablePanel.setBackground(new Color(255,255,255)); //���̺� �г��� ��׶���
		buttonPanel.setBackground(new Color(0,0,0));
		
		tableSet();
		
		System.out.println("\n���̺� �г� : " + tbPanelList.size() + "�� �߰���\n");
		
		tablePanel.setBounds(0, 0, 800, 400);
		
		goMasterView = new JButton("������", null);
		reset = new JButton("���ΰ�ħ", null);
		
		goMasterView.setBounds(245, 405, 150, 50);
		reset.setBounds(397, 405, 150, 50);
		
		goMasterView.setBackground(new Color(255,192,203));
		reset.setBackground(new Color(255,192,203));
		
		goMasterView.setFont(new Font(null, 1, 20));
		reset.setFont(new Font(null, 1, 20));
		
		goMasterView.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				MasterView masterview = new MasterView(rs, admin);
			}
		});
		reset.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					rs.revalidate();
					admin.renewal();
					System.out.println("���ΰ�ħ ��");
				} catch (Exception e2) {
					System.out.println("���ΰ�ħ �ȵ�");
				}
			}
		});
		
		buttonPanel.add(goMasterView);
		buttonPanel.add(reset);
		
		add(tablePanel);
		add(buttonPanel);
		
		
		setVisible(true);
		
		/*this.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				//JOptionPane.showMessageDialog(null, "ȯ���մϴ�", "����", JOptionPane.INFORMATION_MESSAGE);
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// 
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// 
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// 
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				JOptionPane.showMessageDialog(null, "�ȳ������ʽÿ�", "����", JOptionPane.INFORMATION_MESSAGE);
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// 
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// 
				
			}
		});*/
		
		System.out.println("������� �� �����Ϸ�");
		
		admin.threadRun();
		/*rt = new restartThread(rs, admin);
		rt.run();*/
		
	}
	
	
	
	public DataBese getAdmin() {
		return admin;
	}
	
	public void tableRenamed(Table table) {
		try {
			if (table.getCustomers() != null) {
				tbNameLabel.get(table.getTableNumber() - 1).setText("�� �̸� : " + table.getCustomers());
			}
			else {
				tbNameLabel.get(table.getTableNumber() - 1).setText("�� �̸� : ����");
			}
		} catch (Exception e) {
			System.out.println("���̺� �����ӵ� �����߻�");
		}
	}
	
	public void tableSet() {
		try {
			System.out.println("tableSet : ���̺� ��Ʈ ����");
			int i = 0;
			
			for (JPanel myPanel : tbPanelList) {
				remove(myPanel);
			}
			System.out.println("tableSet : ���̺� �гθ���Ʈ ������ �Ϸ�");
			
			tbPanelList.clear();
			tbNumberLabel.clear();
			tbNameLabel.clear();
			tbButton.clear();
			
			System.out.println("tableSet : ���̺� �г� ����Ʈ Ŭ���� �Ϸ�");
			
			for (Table table : admin.getTableList()) {
				
				tbPanelList.add(new JPanel());
				tbPanelList.get(i).setLayout(null);
				tbPanelList.get(i).setBackground(new Color(255,255,255));
				
				tbNumberLabel.add(new JLabel(table.getTableNumber() + "�� ���̺�"));
				tbNumberLabel.get(i).setBounds(30, 30, 300, 25);
				tbNumberLabel.get(i).setFont(new Font(getName(), 1, 20));
				
				if (table.getCustomers() != null) {
					tbNameLabel.add(new JLabel("�� �̸� : " + table.getCustomers()));
				}
				else {
					tbNameLabel.add(new JLabel("�� �̸� : ����"));
				}
				tbNameLabel.get(i).setBounds(30, 70, 300, 35);
				tbNameLabel.get(i).setFont(new Font(getName(), 1, 25));
				
				tbButton.add(new MyButton("�󼼺���", i));
				tbButton.get(i).addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int index = 0;
						MyButton mybtn = (MyButton) e.getSource();
						index = mybtn.getIndex();
						TableView tv = new TableView(rs, mybtn.getIndex(), admin);
						rs.dispose();
					}
				});
				
				tbButton.get(i).setBounds(90, 150, 83, 30);
				tbButton.get(i).setBorder(null);
				tbButton.get(i).setFont(new Font(getName(), 1, 19));
				tbButton.get(i).setBackground(new Color(255,255,255));
				
				tbPanelList.get(i).add(tbNumberLabel.get(i));
				tbPanelList.get(i).add(tbNameLabel.get(i));
				tbPanelList.get(i).add(tbButton.get(i));
				tablePanel.add(tbPanelList.get(i));
				System.out.println("tableSet : ���̺� " + i + "�� ������");
				i++;
			}
			System.out.println("tableSet : ���̺� ��Ʈ �Ϸ�");
			
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null, "���̺��� �ҷ����µ� ������ �߻��߽��ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	public int tableSize() {
		return admin.getTableList().size();
	}
	
	public void ownerCall(Table table) {
		try {
			boolean cheak = true;
			while(cheak) {
			int result = JOptionPane.showConfirmDialog(null, table.getTableNumber() + "�� ���̺��� ȣ���Դϴ�.", "ȣ��", JOptionPane.OK_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				cheak = false;
			}
			else {
				cheak = true;
			}
			}
		} catch (Exception e) {

		}
	}
	
	public restartThread getThread() {
		return rt;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("�߸���");
	}

	public static class restartThread implements Runnable{
		private RestaurantView rs;
		private DataBese admin;
		private boolean cheak;
		
		public restartThread(RestaurantView rs, DataBese admin) {
			try {
				this.rs = rs;
				this.admin = admin;
				cheak = false;
			} catch (Exception e) {
				System.out.println("restartThread ERROR : " + e);
			}
			
		}
		
		@Override
		public void run() {
			System.out.println("���ΰ�ħ ������ �۵�\n");
			try {
				do {
					while (cheak) {
						System.out.println("������ �۵���");
						rs.revalidate();
						rs.repaint();
						Thread.sleep(2000);
					}
					Thread.sleep(2000);
				} while (true);
			} catch (Exception e) {
				System.out.println("������ ���� �߻�");
			}
		}
		
		public boolean getCheak() {
			return cheak;
		}
		
		public void onOff() {
			try {
				if (cheak) {
					cheak = false;
				}
				else {
					cheak = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
}