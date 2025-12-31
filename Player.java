import javax.swing.JOptionPane;

public class Player {
    // instance variables
    public String playerName;
    public String playerCategory;
    public int playerHealth;
    public int playerXP;
    public int playerLives;
    public double playerGold;
    public boolean playerStatus = false;
    public int killCount;

    public Player() {
        // This prompts for name AFTER title screen
        playerName = JOptionPane.showInputDialog("Enter Your Name:");
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
        }
        System.out.println("Player name: " + playerName);
        
        playerCategory = "wizard";
        playerHealth = 100;
        playerXP = 1;
        playerLives = 3;
        playerGold = 0;
        playerStatus = true;
        killCount = 0;
    }
    
    // Alternative constructor for sign-in username (if needed)
    public Player(String name) {
        playerName = name;
        playerCategory = "wizard";
        playerHealth = 100;
        playerXP = 1;
        playerLives = 3;
        playerGold = 0;
        playerStatus = true;
        killCount = 0;
    }

    public String getPlayerName(){
        return playerName;
    }
    
    public void setPlayerName(String name) {
        playerName = name;
    }
    
    public double increaseGold(int zombieCount) {
        double pricePerZombie = 10.00;
        playerGold = playerGold + (zombieCount * pricePerZombie);
        return playerGold;
    }
    
    public int increasePlayerXP(int zombieCount) {
        playerXP = playerXP + zombieCount;
        return playerXP;
    }
}
