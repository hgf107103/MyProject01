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
		setTitle("DB ID�� PASSWORD�Է�");
		setBounds(20, 20, 400, 300);
		setResizable(false);
		
		allPanel = new JPanel();
		allPanel.setLayout(null);
		allPanel.setBounds(0, 0, 400, 300);
		allPanel.setBackground(new Color(255,255,255));
		
		idLabel = new JLabel("���̵� �Է�");
		idLabel.setBounds(150, 20, 100, 30);
		idLabel.setFont(new Font(getName(), 1, 18));
		
		pwdLabel = new JLabel("��й�ȣ �Է�");
		pwdLabel.setBounds(140, 110, 120, 30);
		pwdLabel.setFont(new Font(getName(), 1, 18));
		
		idText = new JTextField();
		idText.setBounds(75, 60, 250, 30);
		idText.setFont(new Font(getName(), 1, 18));
		
		pwdText = new JTextField();
		pwdText.setBounds(75, 150, 250, 30);
		pwdText.setFont(new Font(getName(), 1, 18));
		
		
		submitTest = new JButton("�׽�Ʈ");
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
				int result = JOptionPane.showConfirmDialog(null, "�׽�Ʈ�� �����߽��ϴ�.\n�����Ͻðڽ��ϱ�?", "�˸�", JOptionPane.YES_NO_OPTION);
				
				if (result == JOptionPane.YES_OPTION) {
					dispose();
					System.out.println(uid + " : " + upass);
					new RestaurantView(uid, upass, db);
				} else {
					JOptionPane.showMessageDialog(null, "����ϼ̽��ϴ�.", "�˸�", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
			} else {
				JOptionPane.showMessageDialog(null, "���̵�� ��й�ȣ�� �߸��Ǿ����ϴ�.", "����", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e2) {
			// TODO: handle exception
		}
		
	}
	
}
