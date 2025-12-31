import java.util.HashMap;
import java.util.Map;

public class Inventory {
    public enum Item {
        HEALTH_PACK("Health Pack", "C:\\Users\\carto\\Downloads\\healthpack.png"),
        ENERGY_DRINK("Energy Drink", "C:\\Users\\carto\\Downloads\\energydrink.png"),
        AMMO_BOX("Ammo Box", "C:\\Users\\carto\\Downloads\\ammobox.png"),
        REPAIR_KIT("Repair Kit", "C:\\Users\\carto\\Downloads\\repairkit.png"),
        MAGIC_POTION("Magic Potion", "C:\\Users\\carto\\Downloads\\magicpotion.png"),
        LOCK_PICK("Lock Pick", "C:\\Users\\carto\\Downloads\\lockpick.png");

        private final String displayName;
        private final String imagePath;

        Item(String displayName, String imagePath) {
            this.displayName = displayName;
            this.imagePath = imagePath;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    private Map<Item, Integer> items;

    public Inventory() {
        items = new HashMap<>();
        // Initialize with some default items
        items.put(Item.HEALTH_PACK, 2);
        items.put(Item.ENERGY_DRINK, 3);
        items.put(Item.AMMO_BOX, 5);
        items.put(Item.REPAIR_KIT, 1);
        items.put(Item.MAGIC_POTION, 1);
        items.put(Item.LOCK_PICK, 1);
    }

    // Add items to inventory
    public void addItem(Item item, int quantity) {
        int currentQty = items.getOrDefault(item, 0);
        items.put(item, currentQty + quantity);
    }

    // Remove items from inventory
    public boolean removeItem(Item item, int quantity) {
        int currentQty = items.getOrDefault(item, 0);
        if (currentQty >= quantity) {
            items.put(item, currentQty - quantity);
            return true;
        }
        return false;
    }

    // Get item quantity
    public int getItemCount(Item item) {
        return items.getOrDefault(item, 0);
    }

    // Get all items as a map for GUI display
    public Map<Item, Integer> getItems() {
        return new HashMap<>(items);
    }

    // Use an item
    public boolean useItem(Item item) {
        return removeItem(item, 1);
    }

    // Display inventory contents
    public void displayInventory() {
        System.out.println("Inventory:");
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.printf("- %s: %d\n", entry.getKey().getDisplayName(), entry.getValue());
            }
        }
    }
}
