import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Engine {
    // Global variables
    public Player plyr;
    public Weapon wpn;
    public Zombie zom;
    private GameWindow gameWindow;
    
    public Engine(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }
    
    public void gameStart() {
        // Show title card
        ImageIcon icon;
        try {
            icon = new ImageIcon("C:\\Users\\carto\\Downloads\\titlecard.png");
            JOptionPane.showMessageDialog(null, "", "ZOMBIE GAME", JOptionPane.INFORMATION_MESSAGE, icon);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ZOMBIE GAME", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Initialize player and weapon
        plyr = new Player(); // This will prompt for name
        wpn = new Weapon();
    }
    public void levelOne() {
        System.out.println("COD Zombies");
        System.out.println("A wild fungus was mixed" +
                          " with the drinking water,\n" +
                          "which caused the locals to mutate." +
                          "\nMutate into something horrible." +
                          "\nThey craved brains instead of normal food" +
                          " and the virus spread. \nYou, " + plyr.playerName + " are one" +
                          " of the last survivors \n" +
                          "of the town.....HELP US!!! \n");
        
        String[] map = {"center of town", "to the shed", "hospital", "the woods", "a hideout"};
        System.out.println("\n Welcome to level 1: " + map[0] + "\n");
    }
    
    public void fightScene() {
        // Fight Scene
        char heart = '\u2665';
        char skull = '\u2620';
        
        zom = new Zombie();       
        while (plyr.playerHealth > 0 && zom.zombieHealth > 0) {
            // check for DOT based zombie
            if (zom.zombieType == 2) {
                zom.zombieHealth = zom.zombieHealth - wpn.weaponDMG;
                plyr.playerHealth = plyr.playerHealth - zom.zombieDMG;
                
                try { Thread.sleep(2000); } catch (InterruptedException e) {}
                plyr.playerHealth = plyr.playerHealth - zom.zombieDMG;
                System.out.println(" ... " + skull + " ... " + skull + "\n");
                
                try { Thread.sleep(2000); } catch (InterruptedException e) {}
                plyr.playerHealth = plyr.playerHealth - zom.zombieDMG;
                System.out.println(" ... " + skull + " ...  \n");
                
                try { Thread.sleep(2000); } catch (InterruptedException e) {}
                plyr.playerHealth = plyr.playerHealth - zom.zombieDMG;
                System.out.println(skull + " ... " + skull + " ... " + skull + "\n");
                System.out.println("You took poison damage, your health is: " + plyr.playerHealth);
                zom = new Zombie();
            } else {
                // normal fight 'round'
                zom.zombieHealth = zom.zombieHealth - wpn.weaponDMG;
                plyr.playerHealth = plyr.playerHealth - zom.zombieDMG;
            }
            
            if (zom.zombieHealth <= 0) {
                System.out.println("You erraticated a zombie!");
                plyr.killCount++;
                plyr.playerXP = plyr.playerXP * 2;
                plyr.playerGold += zom.goldOutput;
                System.out.println("Zombies Killed# " + plyr.killCount);
                System.out.println("Player " + heart + heart + ": " + plyr.playerHealth);
                zom = new Zombie();
            }
            
            if (plyr.playerHealth <= 0) {
                System.out.println("You died in battle!  GAME OVER");
                plyr.playerStatus = false;
                break;
            }
            
            // Update the game window status
            if (gameWindow != null) {
                gameWindow.updateStatusDisplay();
            }
        }
    }
    
    public void results() {
        // Final Stats Output
        char radioactive = '\u2622';
        char money = '\u20AC';
        String frowny = "\uD83D\uDE41";
        
        System.out.println("\n*****************************");
        System.out.println("******    GAME STATS   ******");
        System.out.println("***** Zombies " + radioactive + "  " + plyr.killCount + " *****");
        System.out.println("***** Player XP: " + plyr.playerXP + "      *****");
        System.out.println("***** Player Status: " + frowny + " *****");
        System.out.println("***** Gold " + money + " " + plyr.playerGold + "        *****");
        System.out.println("*****************************");
    }
    
    // Main method to test the engine
    public static void main(String[] args) {
        Engine engine = new Engine(null);
        engine.gameStart();
        engine.levelOne();
        engine.fightScene();
        engine.results();
    }
}
