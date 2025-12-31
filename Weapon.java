import javax.swing.*;
import java.awt.*;

public class Weapon {
    public int weapon;
    public String weaponName;
    public int weaponDMG;
    public String imagePath;

    public Weapon() {
        weapon = 0;
        weaponName = "";
        weaponDMG = 0;
        imagePath = "";
    }

    public void chooseWeapon() {
        // Weapon options and images
        String[] weaponOptions = {"Knife", "Hatchet", "RPG", "Nuke", "Fists"};
        String[] imagePaths = {
            "C:\\Users\\carto\\Downloads\\knife.png",
            "C:\\Users\\carto\\Downloads\\hatchet.png", 
            "C:\\Users\\carto\\Downloads\\rpg.png",
            "C:\\Users\\carto\\Downloads\\nuke.png",
            "C:\\Users\\carto\\Downloads\\fists.png"
        };
        int[] weaponDamages = {5, 20, 50, 130, 2};

        // Create a panel with GridLayout for organized selection boxes
        JPanel panel = new JPanel(new GridLayout(1, weaponOptions.length, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton[] buttons = new JButton[weaponOptions.length];

        // Track the user's choice
        final int[] choice = {-1};

        // Create buttons with images
        for (int i = 0; i < weaponOptions.length; i++) {
            ImageIcon icon = null;
            try {
                icon = new ImageIcon(imagePaths[i]);
                // Check if image loaded successfully
                if (icon.getIconWidth() <= 0) {
                    throw new Exception("Image not found: " + imagePaths[i]);
                }
                // Scale the icon to uniform size
                Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaled);
                buttons[i] = new JButton("<html><center>" + weaponOptions[i] + "<br>DMG: " + weaponDamages[i] + "</center></html>", icon);
            } catch (Exception e) {
                // Fallback if image fails to load - use text only
                System.out.println("Failed to load image: " + imagePaths[i] + " - " + e.getMessage());
                buttons[i] = new JButton("<html><center>" + weaponOptions[i] + "<br>DMG: " + weaponDamages[i] + "</center></html>");
            }
            
            buttons[i].setVerticalTextPosition(SwingConstants.BOTTOM);
            buttons[i].setHorizontalTextPosition(SwingConstants.CENTER);
            buttons[i].setPreferredSize(new Dimension(120, 140));
            buttons[i].setBackground(Color.LIGHT_GRAY);
            buttons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));

            int index = i;
            buttons[i].addActionListener(e -> {
                choice[0] = index;
                SwingUtilities.getWindowAncestor(panel).dispose();
            });

            panel.add(buttons[i]);
        }

        // Show dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Choose your weapon");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        // Handle selection or cancellation
        if (choice[0] >= 0 && choice[0] < weaponOptions.length) {
            weapon = choice[0] + 1;
        } else {
            weapon = 5; // default fists if cancelled
        }

        // Set weapon properties
        switch (weapon) {
            case 1 -> { weaponName = "Knife"; weaponDMG = 5; imagePath = imagePaths[0]; }
            case 2 -> { weaponName = "Hatchet"; weaponDMG = 20; imagePath = imagePaths[1]; }
            case 3 -> { weaponName = "RPG"; weaponDMG = 50; imagePath = imagePaths[2]; }
            case 4 -> { weaponName = "Nuke"; weaponDMG = 130; imagePath = imagePaths[3]; }
            default -> { weaponName = "Fists"; weaponDMG = 2; imagePath = imagePaths[4]; }
        }

        // Show selected weapon image
        showWeaponImage();
        System.out.println("Selected: " + weaponName + " (Damage: " + weaponDMG + ")");
    }

    private void showWeaponImage() {
        try {
            ImageIcon weaponIcon = new ImageIcon(imagePath);
            // Check if image exists and is valid
            if (weaponIcon.getIconWidth() > 0) {
                Image scaled = weaponIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                weaponIcon = new ImageIcon(scaled);
                JOptionPane.showMessageDialog(null, 
                    "You selected: " + weaponName + "\nDamage: " + weaponDMG,
                    "Weapon Choice", 
                    JOptionPane.INFORMATION_MESSAGE, 
                    weaponIcon);
            } else {
                throw new Exception("Invalid image");
            }
        } catch (Exception e) {
            // Show text-only message if image fails
            System.out.println("Could not display weapon image: " + e.getMessage());
            JOptionPane.showMessageDialog(null, 
                "You selected: " + weaponName + "\nDamage: " + weaponDMG,
                "Weapon Choice", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Getter methods for accessing weapon properties
    public String getWeaponName() {
        return weaponName;
    }

    public int getWeaponDamage() {
        return weaponDMG;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getWeaponId() {
        return weapon;
    }
}
