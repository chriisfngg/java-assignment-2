import java.util.Scanner;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

public class game {
    static Scanner input = new Scanner(System.in);
    static Random rand = new Random();

    // player variables
    static String character;
    static int health = 100;
    static boolean savedVillage;

    // main method that runs the game loop
    public static void main(String[] args) {
        readScoreFile(); // show previous scores

        while (true) {
            startGame(); // play one run
            System.out.println("play again? (yes/no)");
            String again = input.nextLine();
            if (!again.equalsIgnoreCase("yes")) {
                System.out.println("thanks for playing!");
                break;
            }
            health = 100; // reset health
        }
    }

    // method that starts the game and lets player pick a character
    public static void startGame() {
        System.out.println("welcome to journey to the west!");
        System.out.println("choose your character:");
        System.out.println("1. sun wukong");
        System.out.println("2. pigsy");
        System.out.println("3. sandy");

        int choice = input.nextInt();
        input.nextLine(); // clear newline

        switch (choice) {
            case 1: character = "sun wukong"; break;
            case 2: character = "pigsy"; break;
            case 3: character = "sandy"; break;
            default: character = "random traveler"; break;
        }

        System.out.println("you chose: " + character);
        System.out.print("traveling to the next area");

        // loading dots animation
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                // do nothing
            }
        }
        System.out.println();

        askToSaveVillage();
    }

    // method that asks if player wants to save the village
    public static void askToSaveVillage() {
        System.out.println("a nearby village is under attack. do you want to save it? (yes/no)");
        String decision = input.nextLine();

        if (decision.equalsIgnoreCase("yes")) {
            savedVillage = true;
            System.out.println("you chose to save the village.");
            health += 10;
        } else {
            savedVillage = false;
            System.out.println("you ignored the village.");
            health -= 10;
        }

        System.out.println("your health is now: " + health);
        health = banditFight(health); // fight the bandit and update health
    }

    // method that simulates a fight with a bandit
    public static int banditFight(int hp) {
        // create some bandits
        Bandit[] bandits = {
            new Bandit("fire demon", 15),
            new Bandit("river troll", 20),
            new Bandit("thief monkey", 10)
        };

        Bandit enemy = bandits[rand.nextInt(bandits.length)];

        System.out.println("\nyou are ambushed!");
        enemy.attack(); // call overridden method

        int banditDamage = rand.nextInt(20) + 5;
        hp -= banditDamage;

        System.out.println("you fought back and took " + banditDamage + " damage.");
        System.out.println("remaining health: " + hp);

        if (hp <= 0) {
            System.out.println("you were defeated. game over.");
        } else {
            System.out.println("you survived the battle.");
            ending(hp);
        }

        return hp;
    }

    // method that prints the final result based on choices
    public static void ending(int finalHealth) {
        System.out.println("\n-- ending --");
        System.out.println();


        if (savedVillage && finalHealth > 50) {
            System.out.println("you are hailed as a hero who saved the people!");
        } else if (!savedVillage && finalHealth > 50) {
            System.out.println("you survived, but many blame you for ignoring the weak.");
        } else {
            System.out.println("your journey ends quietly. you learned a lot about yourself.");
        }

        saveScore(character, finalHealth); // log score
    }

    // method to write score to a text file
    public static void saveScore(String name, int hp) {
        try {
            FileWriter fw = new FileWriter("score.txt", true);
            fw.write(name + " ended with hp: " + hp + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("error saving score.");
        }
    }

    // method to read past scores from file
    public static void readScoreFile() {
        try {
            File file = new File("score.txt");
            Scanner reader = new Scanner(file);
            System.out.println("\n--- previous scores ---");
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("no previous scores found.");
        }
    }
}

// enemy class (parent)
class Enemy {
    String name;

    public Enemy(String name) {
        this.name = name;
    }

    public void attack() {
        System.out.println(name + " attacks you!");
    }
}

// bandit class inherits from enemy
class Bandit extends Enemy {
    int strength;

    public Bandit(String name, int strength) {
        super(name);
        this.strength = strength;
    }

    // overloaded constructor with default strength
    public Bandit(String name) {
        super(name);
        this.strength = 10;
    }

    // overridden attack method
    @Override
    public void attack() {
        System.out.println(name + " attacks with strength " + strength + "!");
    }
}
