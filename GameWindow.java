import java.awt.*;
import javax.swing.*;

public class GameWindow extends JFrame {
    public Player ply;
    public Weapon wpn;
    public Zombie zom;
    private JTextArea gameText;
    private JLabel healthLabel, xpLabel, livesLabel, goldLabel, killsLabel, weaponLabel;
    private JLabel weaponImageLabel;
    private Inventory inventory;
    private boolean gameStarted = false;
    private Engine engine;
    
    public GameWindow(String username) {
        setTitle("Zombie Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // Initialize game objects - Player constructor will prompt for name
        ply = new Player(); // This will show "Enter Your Name" dialog
        // If username from sign-in was provided, use it (optional)
        if (username != null && !username.equals("Player")) {
            ply.setPlayerName(username);
        }
        
        wpn = new Weapon();
        zom = new Zombie();
        inventory = new Inventory();
        engine = new Engine(this);
        engine.plyr = ply;
        engine.wpn = wpn;
        engine.zom = zom;

        // Top Panel - Logo
        JPanel topPanel = new JPanel();
        try {
            ImageIcon icon = new ImageIcon("C:\\Users\\carto\\Downloads\\Zombie Text.png");
            Image scaledImage = icon.getImage().getScaledInstance(300, 100, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            topPanel.add(logoLabel);
        } catch (Exception e) {
            JLabel logoLabel = new JLabel("ZOMBIE GAME");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
            topPanel.add(logoLabel);
        }
        c.add(topPanel, BorderLayout.NORTH);

        // West Panel - Weapon
        JPanel westPanel = createWeaponPanel();
        c.add(westPanel, BorderLayout.WEST);

        // Center Panel - Game Action
        JPanel centerPanel = createCenterPanel();
        c.add(centerPanel, BorderLayout.CENTER);

        // East Panel - Player Status
        JPanel eastPanel = createStatusPanel();
        c.add(eastPanel, BorderLayout.EAST);

        // South Panel - Inventory
        JPanel southPanel = new JPanel();
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton inventoryButton = new JButton("Open Inventory");
        inventoryButton.addActionListener(e -> showInventoryPopup());
        southPanel.add(inventoryButton);

        c.add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    // Original constructor for backward compatibility
    public GameWindow() {
        this(null); // Will prompt for name
    }

    private JPanel createWeaponPanel() {
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel weaponTitleLabel = new JLabel("WEAPON");
        weaponTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        weaponTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        westPanel.add(weaponTitleLabel);
        westPanel.add(Box.createVerticalStrut(10));

        // Weapon icon - initially empty
        weaponImageLabel = new JLabel("Select Weapon", JLabel.CENTER);
        weaponImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        weaponImageLabel.setPreferredSize(new Dimension(100, 100));
        weaponImageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        westPanel.add(weaponImageLabel);

        westPanel.add(Box.createVerticalStrut(10));

        // Weapon info label
        weaponLabel = new JLabel("DMG: 0", JLabel.CENTER);
        weaponLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        weaponLabel.setFont(new Font("Arial", Font.BOLD, 12));
        westPanel.add(weaponLabel);
        westPanel.add(Box.createVerticalStrut(10));

        // Change Weapon button - always enabled
        JButton changeWeaponButton = new JButton("Change Weapon");
        changeWeaponButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeWeaponButton.addActionListener(e -> {
            wpn.chooseWeapon();
            updateWeaponDisplay();
            if (gameStarted) {
                gameText.append("\nChanged weapon to: " + wpn.weaponName + " (DMG: " + wpn.weaponDMG + ")");
                autoScrollToBottom();
            }
        });
        westPanel.add(changeWeaponButton);

        return westPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Game Action"));
        
        gameText = new JTextArea();
        gameText.setEditable(false);
        gameText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        gameText.setText("Welcome to the Zombie Game!\n\n" +
                        "Player: " + ply.playerName + "\n\n" +
                        "Click START GAME to begin!\n" +
                        "You will select your weapon when starting the game.\n" +
                        "You can change weapons anytime during the game.\n" +
                        "Use inventory items to heal and get bonuses.");
        
        JScrollPane scrollPane = new JScrollPane(gameText);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Start Game button
        JPanel buttonPanel = new JPanel();
        
        JButton startGameButton = new JButton("START GAME");
        startGameButton.setFont(new Font("Arial", Font.BOLD, 16));
        startGameButton.setBackground(Color.GREEN);
        startGameButton.setForeground(Color.WHITE);
        startGameButton.addActionListener(e -> startGame());

        buttonPanel.add(startGameButton);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        return centerPanel;
    }

    private JPanel createStatusPanel() {
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel statusLabel = new JLabel("PLAYER STATUS");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        eastPanel.add(statusLabel);
        eastPanel.add(Box.createVerticalStrut(15));

        // Player stats with dynamic labels
        healthLabel = new JLabel("Health: " + ply.playerHealth);
        healthLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        healthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        eastPanel.add(healthLabel);

        xpLabel = new JLabel("XP: " + ply.playerXP);
        xpLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        xpLabel.setFont(new Font("Arial", Font.BOLD, 14));
        eastPanel.add(xpLabel);

        livesLabel = new JLabel("Lives: " + ply.playerLives);
        livesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        eastPanel.add(livesLabel);

        goldLabel = new JLabel("Gold: " + ply.playerGold);
        goldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        goldLabel.setFont(new Font("Arial", Font.BOLD, 14));
        eastPanel.add(goldLabel);

        killsLabel = new JLabel("Kills: " + ply.killCount);
        killsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        killsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        eastPanel.add(killsLabel);

        eastPanel.add(Box.createVerticalStrut(20));

        // Add Fight Scene button
        JButton fightButton = new JButton("Start Combat");
        fightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        fightButton.addActionListener(e -> {
            if (gameStarted) {
                gameText.append("\n\n=== COMBAT STARTED ===");
                gameText.append("\nEngaging zombie: " + zom.zombieName);
                gameText.append("\nZombie Health: " + zom.zombieHealth);
                gameText.append("\nYour Health: " + ply.playerHealth);
                autoScrollToBottom();
                
                // Start fight in a separate thread to keep UI responsive
                new Thread(() -> {
                    engine.fightScene();
                    updateStatusDisplay();
                    gameText.append("\n=== COMBAT ENDED ===");
                    gameText.append("\nYour Health: " + ply.playerHealth);
                    gameText.append("\nKills: " + ply.killCount);
                    gameText.append("\nGold: " + ply.playerGold);
                    autoScrollToBottom();
                }).start();
            } else {
                JOptionPane.showMessageDialog(this, "Please start the game first!");
            }
        });
        eastPanel.add(fightButton);

        return eastPanel;
    }

    private void startGame() {
        // First, make sure player selects a weapon
        if (wpn.weapon == 0 || wpn.weaponName.equals("")) {
            JOptionPane.showMessageDialog(this, 
                "Please select your weapon first!\n\n" +
                "Click 'Change Weapon' to choose your starting weapon.",
                "Select Weapon", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        gameStarted = true;
        
        gameText.setText("GAME STARTED!\n\n" +
                        "Player: " + ply.playerName + "\n" +
                        "Starting Weapon: " + wpn.weaponName + " (DMG: " + wpn.weaponDMG + ")\n" +
                        "Current Zombie: " + zom.zombieName + "\n" +
                        "Zombie Health: " + zom.zombieHealth + "\n\n" +
                        "=== GAME LOG ===\n" +
                        "Game is running! Click 'Start Combat' to fight zombies.\n" +
                        "You can change weapons anytime using the 'Change Weapon' button.\n" +
                        "Use inventory items to help during the game.");
        
        engine.levelOne();
        updateStatusDisplay();
        
        // Add initial game message
        gameText.append("\n\n--- GAME BEGINS ---");
        gameText.append("\nStory: You are " + ply.playerName + ", one of the last survivors...");
        gameText.append("\nYou encounter a " + zom.zombieName + "!");
        gameText.append("\nEquipped with: " + wpn.weaponName + " (Damage: " + wpn.weaponDMG + ")");
        gameText.append("\nClick 'Start Combat' to begin fighting!");
        
        // Auto-scroll to bottom
        autoScrollToBottom();
    }

    public void updateStatusDisplay() {
        // Update player stats
        healthLabel.setText("Health: " + ply.playerHealth);
        xpLabel.setText("XP: " + ply.playerXP);
        livesLabel.setText("Lives: " + ply.playerLives);
        goldLabel.setText("Gold: " + ply.playerGold);
        killsLabel.setText("Kills: " + ply.killCount);
        
        // Update colors based on status
        if (ply.playerHealth <= 0) {
            healthLabel.setForeground(Color.RED);
        } else if (ply.playerHealth < 30) {
            healthLabel.setForeground(Color.ORANGE);
        } else {
            healthLabel.setForeground(Color.BLACK);
        }
        
        if (ply.playerLives <= 0) {
            livesLabel.setForeground(Color.RED);
        } else if (ply.playerLives == 1) {
            livesLabel.setForeground(Color.ORANGE);
        } else {
            livesLabel.setForeground(Color.BLACK);
        }
        
        // Repaint the panel
        healthLabel.repaint();
        livesLabel.repaint();
    }

    private void updateWeaponDisplay() {
        // Update weapon image
        try {
            ImageIcon weaponIcon = new ImageIcon(wpn.imagePath);
            Image scaledWeapon = weaponIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            weaponImageLabel.setIcon(new ImageIcon(scaledWeapon));
            weaponImageLabel.setText(""); // Clear text when image loads
        } catch (Exception e) {
            weaponImageLabel.setIcon(null); // Clear icon if image fails
            weaponImageLabel.setText(wpn.weaponName);
            weaponImageLabel.setForeground(Color.BLACK);
        }

        // Update weapon info
        weaponLabel.setText("DMG: " + wpn.weaponDMG);
        
        // Repaint the labels
        weaponImageLabel.repaint();
        weaponLabel.repaint();
    }

    private void showInventoryPopup() {
        JDialog popup = new JDialog(this, "Inventory", true);
        popup.setSize(600, 400);
        popup.setLocationRelativeTo(this);
        
        JPanel content = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Player Inventory - Use items to help in battle!", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        // Inventory items with PNG images
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Create inventory items with actual quantities from Inventory class
        Inventory.Item[] items = {
            Inventory.Item.HEALTH_PACK,
            Inventory.Item.ENERGY_DRINK,
            Inventory.Item.AMMO_BOX,
            Inventory.Item.REPAIR_KIT,
            Inventory.Item.MAGIC_POTION,
            Inventory.Item.LOCK_PICK
        };

        for (Inventory.Item item : items) {
            int quantity = inventory.getItemCount(item);
            JPanel itemPanel = createInventoryItemPanel(item, quantity);
            gridPanel.add(itemPanel);
        }

        content.add(new JScrollPane(gridPanel), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close Inventory");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.addActionListener(e -> popup.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        content.add(buttonPanel, BorderLayout.SOUTH);

        popup.setContentPane(content);
        popup.setVisible(true);
    }

    private JPanel createInventoryItemPanel(Inventory.Item item, int quantity) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setPreferredSize(new Dimension(150, 180));

        // Item image
        JLabel imageLabel;
        try {
            ImageIcon originalIcon = new ImageIcon(item.getImagePath());
            Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaledImage), JLabel.CENTER);
        } catch (Exception e) {
            // Fallback if image doesn't load
            imageLabel = new JLabel("[No Image]", JLabel.CENTER);
            imageLabel.setForeground(Color.RED);
        }

        // Item name
        JLabel nameLabel = new JLabel(item.getDisplayName(), JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

        // Item quantity - show red if zero
        JLabel qtyLabel = new JLabel("Qty: " + quantity, JLabel.CENTER);
        qtyLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        if (quantity <= 0) {
            qtyLabel.setForeground(Color.RED);
            qtyLabel.setText("Qty: OUT OF STOCK");
        } else {
            qtyLabel.setForeground(Color.BLUE);
        }

        // Use button - disabled if out of stock
        JButton useButton = new JButton("Use");
        useButton.setFont(new Font("Arial", Font.PLAIN, 11));
        
        if (quantity <= 0) {
            useButton.setEnabled(false);
            useButton.setText("Out of Stock");
            useButton.setBackground(Color.LIGHT_GRAY);
        } else {
            useButton.addActionListener(e -> useInventoryItem(item));
        }

        // Layout components
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(qtyLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(useButton);

        itemPanel.add(imageLabel, BorderLayout.CENTER);
        itemPanel.add(infoPanel, BorderLayout.SOUTH);

        return itemPanel;
    }

    private void useInventoryItem(Inventory.Item item) {
        if (inventory.useItem(item)) {
            String message = "";
            
            switch (item) {
                case HEALTH_PACK:
                    ply.playerHealth += 30;
                    if (ply.playerHealth > 100) ply.playerHealth = 100;
                    message = "Used Health Pack! +30 Health";
                    break;
                    
                case ENERGY_DRINK:
                    ply.playerHealth += 15;
                    if (ply.playerHealth > 100) ply.playerHealth = 100;
                    message = "Used Energy Drink! +15 Health";
                    break;
                    
                case AMMO_BOX:
                    // Increase weapon damage temporarily
                    wpn.weaponDMG += 10;
                    message = "Used Ammo Box! +10 Weapon Damage for next attack";
                    break;
                    
                case REPAIR_KIT:
                    ply.playerLives += 1;
                    message = "Used Repair Kit! +1 Extra Life";
                    break;
                    
                case MAGIC_POTION:
                    ply.playerXP *= 2;
                    message = "Used Magic Potion! XP Doubled";
                    break;
                    
                case LOCK_PICK:
                    ply.playerGold += 50;
                    message = "Used Lock Pick! Found 50 Gold";
                    break;
            }
            
            // Update status display
            updateStatusDisplay();
            
            // Show success message
            JOptionPane.showMessageDialog(this, 
                message + "\n\nItem used successfully!",
                "Item Used", 
                JOptionPane.INFORMATION_MESSAGE);
                
            // Update game text
            if (gameStarted) {
                gameText.append("\n" + message);
                autoScrollToBottom();
            }
            
            // Refresh inventory popup
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JDialog && ((JDialog)window).getTitle().equals("Inventory")) {
                    window.dispose();
                    showInventoryPopup();
                    break;
                }
            }
            
        } else {
            JOptionPane.showMessageDialog(this, 
                "You are out of " + item.getDisplayName() + "!\n" +
                "This item cannot be used.",
                "Out of Stock", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void autoScrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            gameText.setCaretPosition(gameText.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}
