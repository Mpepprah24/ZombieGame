import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SignInScreen extends JFrame {
    // Frame Dimensions
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 500;

    public SignInScreen() {
        // 1. Set up the main JFrame
        setTitle("Sign In");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Use BoxLayout for the main content pane to stack the panels vertically
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // 2. Create the 5 JPanels

        // Panel 1: Image Placeholder - FIXED LOGO DISPLAY
        JPanel imagePanel = new JPanel();
        imagePanel.setPreferredSize(new Dimension(FRAME_WIDTH, 200));
        imagePanel.setMaximumSize(new Dimension(FRAME_WIDTH, 200));
        imagePanel.setLayout(new BorderLayout());

        try {
            // FIX: Check if file exists first
            File logoFile = new File("C:/Users/carto/Downloads/nexusgaminglogo.png");
            if (logoFile.exists()) {
                ImageIcon icon = new ImageIcon("C:/Users/carto/Downloads/nexusgaminglogo.png");
                // Check if image loaded
                if (icon.getIconWidth() > 0) {
                    Image scaledImage = icon.getImage().getScaledInstance(280, 180, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    imageLabel.setVerticalAlignment(SwingConstants.CENTER);
                    imagePanel.add(imageLabel, BorderLayout.CENTER);
                } else {
                    throw new Exception("Image could not be loaded");
                }
            } else {
                throw new Exception("File not found: " + logoFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("Logo loading error: " + e.getMessage());
            JLabel placeholderLabel = new JLabel("<html><center>NEXUS<br>GAMING</center></html>", SwingConstants.CENTER);
            placeholderLabel.setFont(new Font("Arial", Font.BOLD, 24));
            placeholderLabel.setForeground(Color.BLUE);
            imagePanel.add(placeholderLabel, BorderLayout.CENTER);
        }

        // Panel 2: Username Entry
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.BOLD, 14));
        usernamePanel.add(usernameField);

        // Panel 3: Password Entry
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.BOLD, 14));
        passwordPanel.add(passwordField);

        // Panel 4: Submit Button
        JPanel buttonPanel = new JPanel();
        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("Arial", Font.BOLD, 18));
        buttonPanel.add(signInButton);

        // Panel 5: Create Account Label
        JPanel createAccountPanel = new JPanel();
        JLabel createAccountLabel = new JLabel("<HTML><U>Create Account</U></HTML>");
        createAccountLabel.setForeground(Color.BLUE.darker());
        createAccountLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountPanel.add(createAccountLabel);

        // Add click listener for create account
        createAccountLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showCreateAccountDialog();
            }
        });

        // 3. Add the panels to the JFrame in the correct order
        add(imagePanel);
        add(Box.createVerticalStrut(10));
        add(usernamePanel);
        add(passwordPanel);
        add(Box.createVerticalStrut(15));
        add(buttonPanel);
        add(Box.createVerticalStrut(20));
        add(createAccountPanel);

        // Sign In button action listener - FIXED: Pass username to Player
        signInButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String username = usernameField.getText();
                String passwd = new String(passwordField.getPassword());
            
                if (verifyUser(username, passwd)) {
                    dispose();
                    // Launch the game window with the username
                    SwingUtilities.invokeLater(() -> {
                        new GameWindow(username);
                    });
                } else {
                    JOptionPane.showMessageDialog(SignInScreen.this, "Invalid username or password!");
                }
            }
        });

        // Center frame and make visible
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showCreateAccountDialog() {
        // Create account dialog
        JDialog createAccountDialog = new JDialog(this, "Create Account", true);
        createAccountDialog.setSize(350, 300);
        createAccountDialog.setLayout(new BorderLayout());
        createAccountDialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Username field
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userPanel.add(new JLabel("Username:"));
        JTextField newUserField = new JTextField(15);
        userPanel.add(newUserField);

        // Password field
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passPanel.add(new JLabel("Password:"));
        JPasswordField newPassField = new JPasswordField(15);
        passPanel.add(newPassField);

        // Confirm Password field
        JPanel confirmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confirmPanel.add(new JLabel("Confirm Password:"));
        JPasswordField confirmPassField = new JPasswordField(15);
        confirmPanel.add(confirmPassField);

        // Email field
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(15);
        emailPanel.add(emailField);

        formPanel.add(userPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(passPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(confirmPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(emailPanel);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton createButton = new JButton("Create Account");
        JButton cancelButton = new JButton("Cancel");

        createButton.addActionListener(e -> {
            String username = newUserField.getText().trim();
            String password = new String(newPassField.getPassword());
            String confirmPassword = new String(confirmPassField.getPassword());
            String email = emailField.getText().trim();

            // Validation
            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(createAccountDialog, "All fields are required!");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(createAccountDialog, "Passwords do not match!");
                return;
            }

            if (username.length() < 3) {
                JOptionPane.showMessageDialog(createAccountDialog, "Username must be at least 3 characters!");
                return;
            }

            if (password.length() < 6) {
                JOptionPane.showMessageDialog(createAccountDialog, "Password must be at least 6 characters!");
                return;
            }

            if (userExists(username)) {
                JOptionPane.showMessageDialog(createAccountDialog, "Username already exists!");
                return;
            }

            // Create account
            if (createUser(username, password, email)) {
                JOptionPane.showMessageDialog(createAccountDialog, "Account created successfully!");
                createAccountDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(createAccountDialog, "Error creating account!");
            }
        });

        cancelButton.addActionListener(e -> createAccountDialog.dispose());

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        createAccountDialog.add(formPanel, BorderLayout.CENTER);
        createAccountDialog.add(buttonPanel, BorderLayout.SOUTH);
        createAccountDialog.setVisible(true);
    }

    private boolean userExists(String username) {
        File fle = new File("C:\\Users\\carto\\Downloads\\database.txt");
        
        try (Scanner scnr = new Scanner(fle)) {
            while (scnr.hasNextLine()) {
                String data = scnr.nextLine().trim();
                if (data.isEmpty()) continue;
                
                String[] parts = data.split(", "); 
                if (parts.length < 4) continue;
                
                String user = parts[1].trim();
                if (username.equals(user)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            // If file doesn't exist, no users exist yet
            return false;
        }
        return false;
    }

    private boolean createUser(String username, String password, String email) {
        try {
            File fle = new File("C:\\Users\\carto\\Downloads\\database.txt");
            
            // Get next user ID
            int nextId = 1;
            if (fle.exists()) {
                try (Scanner scnr = new Scanner(fle)) {
                    while (scnr.hasNextLine()) {
                        String data = scnr.nextLine().trim();
                        if (data.isEmpty()) continue;
                        String[] parts = data.split(", ");
                        if (parts.length >= 1) {
                            try {
                                int currentId = Integer.parseInt(parts[0].trim());
                                if (currentId >= nextId) {
                                    nextId = currentId + 1;
                                }
                            } catch (NumberFormatException e) {
                                // Skip invalid lines
                            }
                        }
                    }
                }
            }

            // Encrypt password with random shift
            int shift = (int)(Math.random() * 26) + 1; // 1-26
            String encryptedPassword = String.format("%02d", shift) + encrypt(password, shift);

            // Write to database
            FileWriter fw = new FileWriter(fle, true);
            PrintWriter pw = new PrintWriter(fw);
            
            String userEntry = nextId + ", " + username + ", " + encryptedPassword + ", " + email;
            pw.println(userEntry);
            
            pw.close();
            fw.close();
            
            System.out.println("Created user: " + username + " with ID: " + nextId);
            return true;
            
        } catch (IOException e) {
            System.out.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Method to encrypt a string using Caesar Cipher
    public static String encrypt(String text, int shift) { 
        StringBuilder encryptedText = new StringBuilder(); 
        
        // Loop through each character in the text 
        for (int i = 0; i < text.length(); i++) { 
            char c = text.charAt(i); 
            
            // Encrypt uppercase letters 
            if (Character.isUpperCase(c)) { 
                char encryptedChar = (char) (((c - 'A' + shift) % 26) + 'A'); 
                encryptedText.append(encryptedChar);
            } 
            // Encrypt lowercase letters 
            else if (Character.isLowerCase(c)) { 
                char encryptedChar = (char) (((c - 'a' + shift) % 26) + 'a'); 
                encryptedText.append(encryptedChar); 
            } 
            // Keep special characters unchanged 
            else {
                encryptedText.append(c); 
            } 
        } 
        return encryptedText.toString();
    } 
    
    // Method to decrypt a string using Caesar Cipher
    public static String decrypt(String encryptedText) { 
        StringBuilder decryptedText = new StringBuilder(); 
        
        try {
            // Extract shift from first 2 characters
            String shiftStr = encryptedText.substring(0, 2);
            int shift = Integer.parseInt(shiftStr);
            String passwd = encryptedText.substring(2);
            
            // Loop through each character in the text 
            for (int i = 0; i < passwd.length(); i++) { 
                char c = passwd.charAt(i); 
                
                // Decrypt uppercase letters 
                if (Character.isUpperCase(c)) { 
                    char decryptedChar = (char) (((c - 'A' - shift + 26) % 26) + 'A'); 
                    decryptedText.append(decryptedChar);
                } 
                // Decrypt lowercase letters 
                else if (Character.isLowerCase(c)) { 
                    char decryptedChar = (char) (((c - 'a' - shift + 26) % 26) + 'a'); 
                    decryptedText.append(decryptedChar); 
                } 
                // Keep special characters unchanged 
                else {
                    decryptedText.append(c); 
                } 
            } 
        } catch (Exception e) {
            // If decryption fails, return original text
            return encryptedText;
        }
        return decryptedText.toString();
    } 

    public boolean verifyUser(String username, String password) {
        boolean verified = false;
        File fle = new File("C:\\Users\\carto\\Downloads\\database.txt");
        
        try (Scanner scnr = new Scanner(fle)) {
            while (scnr.hasNextLine()) {
                String data = scnr.nextLine().trim();
                if (data.isEmpty()) continue;
                
                System.out.println("Database entry: " + data);
                // Split by comma - your database has format: ID, Username, Password, Email
                String[] parts = data.split(", "); 
                if (parts.length < 4) {
                    System.out.println("Invalid database entry: " + data);
                    continue;
                }
                
                String user = parts[1].trim(); // username is 2nd column
                String storedPassword = parts[2].trim(); // password is 3rd column
                
                System.out.println("Checking user: " + user + " against input: " + username);
                
                if (username.equals(user)) {
                    // Try to decrypt the password
                    String decryptedPass = decrypt(storedPassword);
                    System.out.println("Stored password: " + storedPassword);
                    System.out.println("Decrypted password: " + decryptedPass);
                    System.out.println("Input password: " + password);
                    
                    if (password.equals(decryptedPass)) {
                        verified = true;
                        System.out.println("User " + user + " verified successfully");
                        break;
                    } else {
                        System.out.println("Password mismatch for user: " + user);
                    }
                }
            }
            
            if (!verified) {
                JOptionPane.showMessageDialog(this, "Invalid Username & Password");
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database file not found!");
        }
        
        return verified;
    }
    
    /**
     * Main method to run the application.
     */
    public static void main(String[] args) {
        // Ensure the GUI updates are run on the Event Dispatch Thread (EDT)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SignInScreen();
            }
        });
    }
}
