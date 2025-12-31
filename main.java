import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Show title card first
        ImageIcon icon;
        try {
            icon = new ImageIcon("C:\\Users\\carto\\Downloads\\titlecard.png");
            JOptionPane.showMessageDialog(null, "", "ZOMBIE GAME", JOptionPane.INFORMATION_MESSAGE, icon);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ZOMBIE GAME", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Start with SignInScreen
        SwingUtilities.invokeLater(() -> {
            new SignInScreen();
        });
    }
}
