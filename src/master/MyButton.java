package master;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MyButton extends JButton{
	int index;
	
	public MyButton() {
		super();
	}
	
	public MyButton(String string, ImageIcon Icon) {
		super();
		setText(string);
		setIcon(Icon);
	}
	
	public MyButton(String string) {
		super();
		setText(string);
	}
	
	public MyButton(ImageIcon Icon) {
		super();
		setIcon(Icon);
	}
	
	public MyButton(ImageIcon Icon, int index) {
		super();
		setIcon(Icon);
		this.index = index;
	}
	
	public MyButton(String string, int index) {
		super();
		setText(string);
		this.index = index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
}
