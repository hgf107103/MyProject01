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
		
		System.out.println("시작");
		setTitle("레스토랑");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(600, 200, 800, 500);
		setResizable(false);
		
		
		//프레임 설정
		tablePanel = new JPanel(); //테이블이 추가될 패널
		buttonPanel = new JPanel(); // 버튼 패널
		tablePanel.setLayout(new GridLayout(2, 3, 2, 2)); //테이블을 2행 3열로 정리하는 레이아웃
		buttonPanel.setLayout(null);
		tablePanel.setBackground(new Color(255,255,255)); //테이블 패널의 백그라운드
		buttonPanel.setBackground(new Color(0,0,0));
		
		tableSet();
		
		System.out.println("\n테이블 패널 : " + tbPanelList.size() + "개 추가됨\n");
		
		tablePanel.setBounds(0, 0, 800, 400);
		
		goMasterView = new JButton("마스터", null);
		reset = new JButton("새로고침", null);
		
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
					System.out.println("새로고침 됨");
				} catch (Exception e2) {
					System.out.println("새로고침 안됨");
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
				//JOptionPane.showMessageDialog(null, "환영합니다", "오픈", JOptionPane.INFORMATION_MESSAGE);
				
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
				JOptionPane.showMessageDialog(null, "안녕히가십시오", "종료", JOptionPane.INFORMATION_MESSAGE);
				
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
		
		System.out.println("레스토랑 뷰 생성완료");
		
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
				tbNameLabel.get(table.getTableNumber() - 1).setText("고객 이름 : " + table.getCustomers());
			}
			else {
				tbNameLabel.get(table.getTableNumber() - 1).setText("고객 이름 : 없음");
			}
		} catch (Exception e) {
			System.out.println("테이블 리네임드 오류발생");
		}
	}
	
	public void tableSet() {
		try {
			System.out.println("tableSet : 테이블 세트 시작");
			int i = 0;
			
			for (JPanel myPanel : tbPanelList) {
				remove(myPanel);
			}
			System.out.println("tableSet : 테이블 패널리스트 리무브 완료");
			
			tbPanelList.clear();
			tbNumberLabel.clear();
			tbNameLabel.clear();
			tbButton.clear();
			
			System.out.println("tableSet : 테이블 패널 리스트 클리어 완료");
			
			for (Table table : admin.getTableList()) {
				
				tbPanelList.add(new JPanel());
				tbPanelList.get(i).setLayout(null);
				tbPanelList.get(i).setBackground(new Color(255,255,255));
				
				tbNumberLabel.add(new JLabel(table.getTableNumber() + "번 테이블"));
				tbNumberLabel.get(i).setBounds(30, 30, 300, 25);
				tbNumberLabel.get(i).setFont(new Font(getName(), 1, 20));
				
				if (table.getCustomers() != null) {
					tbNameLabel.add(new JLabel("고객 이름 : " + table.getCustomers()));
				}
				else {
					tbNameLabel.add(new JLabel("고객 이름 : 없음"));
				}
				tbNameLabel.get(i).setBounds(30, 70, 300, 35);
				tbNameLabel.get(i).setFont(new Font(getName(), 1, 25));
				
				tbButton.add(new MyButton("상세보기", i));
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
				System.out.println("tableSet : 테이블 " + i + "개 생성됨");
				i++;
			}
			System.out.println("tableSet : 테이블 세트 완료");
			
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null, "테이블을 불러오는데 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
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
			int result = JOptionPane.showConfirmDialog(null, table.getTableNumber() + "번 테이블에서 호출입니다.", "호출", JOptionPane.OK_OPTION);
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
		System.out.println("잘못됨");
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
			System.out.println("새로고침 쓰레드 작동\n");
			try {
				do {
					while (cheak) {
						System.out.println("쓰레드 작동중");
						rs.revalidate();
						rs.repaint();
						Thread.sleep(2000);
					}
					Thread.sleep(2000);
				} while (true);
			} catch (Exception e) {
				System.out.println("쓰레드 오류 발생");
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