package DB;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import VO.MasterView;
import VO.RestaurantView;
import main.RestaurantMain;

public class DataBeseConn extends JFrame implements ActionListener{
	
	private JPanel allPanel;
	private JLabel idLabel;
	private JLabel pwdLabel;
	
	private JTextField idText;
	private JTextField pwdText;
	
	private JButton submitTest;
	
	public DataBeseConn() {
		setTitle("DB ID와 PASSWORD입력");
		setBounds(20, 20, 400, 300);
		setResizable(false);
		
		allPanel = new JPanel();
		allPanel.setLayout(null);
		allPanel.setBounds(0, 0, 400, 300);
		allPanel.setBackground(new Color(255,255,255));
		
		idLabel = new JLabel("아이디 입력");
		idLabel.setBounds(150, 20, 100, 30);
		idLabel.setFont(new Font(getName(), 1, 18));
		
		pwdLabel = new JLabel("비밀번호 입력");
		pwdLabel.setBounds(140, 110, 120, 30);
		pwdLabel.setFont(new Font(getName(), 1, 18));
		
		idText = new JTextField();
		idText.setBounds(75, 60, 250, 30);
		idText.setFont(new Font(getName(), 1, 18));
		
		pwdText = new JTextField();
		pwdText.setBounds(75, 150, 250, 30);
		pwdText.setFont(new Font(getName(), 1, 18));
		
		
		submitTest = new JButton("테스트");
		submitTest.setBounds(150, 210, 100, 30);
		submitTest.setBackground(new Color(255,255,255));
		submitTest.setFont(new Font(getName(), 1, 18));
		submitTest.addActionListener(this);
		
		
		allPanel.add(idLabel);
		allPanel.add(pwdLabel);
		allPanel.add(idText);
		allPanel.add(pwdText);
		allPanel.add(submitTest);
		
		add(allPanel);
		
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {
			DataBese db = DataBese.getInstance();
			boolean check = db.DataBaseConnectingTest(idText.getText(), pwdText.getText());
			//db = null;
			
			String uid = idText.getText();
			String upass = pwdText.getText();
			
			if (check) {
				int result = JOptionPane.showConfirmDialog(null, "테스트에 성공했습니다.\n연결하시겠습니까?", "알림", JOptionPane.YES_NO_OPTION);
				
				if (result == JOptionPane.YES_OPTION) {
					dispose();
					System.out.println(uid + " : " + upass);
					new RestaurantView(uid, upass, db);
				} else {
					JOptionPane.showMessageDialog(null, "취소하셨습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			} else {
				JOptionPane.showMessageDialog(null, "아이디와 비밀번호가 잘못되었습니다.", "오류", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
	}
	
}
