public class Zombie {
    // instance variables
    public int zombieType;
    public String zombieName = "";
    public int zombieHealth = 50;
    public int zombieDMG = 5;
    public int goldOutput = 0;
    
    public Zombie() {
        zombieType = (int)(Math.random() * 5);//zero Type is allowed, but not listed below
        if (zombieType%2 == 0) {
            zombieType = (int)(Math.random() * 5);
        } else if(zombieType == 0) {
            zombieType++;
        }
        getZombie();
    }

    public void getZombie() {
        System.out.println("Oh NO here comes a Zombie!");
        
        switch (zombieType) {
            case 1:
                zombieName = "crawler zombie";
                zombieHealth = 20;
                zombieDMG = 5;
                goldOutput = 1;
                System.out.println(zombieName);
                break;
            case 2:
                zombieName = "poison zombie";
                zombieHealth = 30;
                zombieDMG = 5;
                goldOutput = 0;
                System.out.println(zombieName);
                break;
            case 3:
                zombieName = "knight zombie";
                zombieHealth = 50;
                zombieDMG = 25; 
                goldOutput = 3;
                System.out.println(zombieName);
                break;
            case 4:
                zombieName = "HUGE zombie";
                zombieHealth = 100;
                zombieDMG = 50;
                goldOutput = 10;
                System.out.println(zombieName);
                break;
            default:
                zombieName = "basic zombie";
                zombieHealth = 50;
                zombieDMG = 5;
                goldOutput = 2;
                System.out.println("Error! Using default zombie");
        }
    }
    
    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
