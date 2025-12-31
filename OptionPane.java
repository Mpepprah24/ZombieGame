import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class OptionPane {

	public static ImageIcon icon;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Pane window without an image 
		JOptionPane.showMessageDialog(null, "HELLO", "TITLE BAR", JOptionPane.INFORMATION_MESSAGE);
		
		//Pane window with image
		icon = new ImageIcon("FILE PATH TO IMAGE");        
        JOptionPane.showMessageDialog(null, "HELLO", "TITLE BAR", JOptionPane.INFORMATION_MESSAGE, icon);
		
	}

}
