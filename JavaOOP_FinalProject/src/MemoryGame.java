import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

// Interface to represent an object that can be displayed
interface Displayable {
    void display();
}

// Class to represent a Card that implements the Displayable interface
class Card implements Displayable {
    private String fruit;
    private boolean isFaceUp;
    private boolean isMatched;

    public Card(String fruit) {
        this.fruit = fruit;
        this.isFaceUp = false;
        this.isMatched = false;
    }

    public String getFruit() {
        return fruit;
    }

    public boolean isFaceUp() {
        return isFaceUp;
    }

    public void setFaceUp(boolean faceUp) {
        isFaceUp = faceUp;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    @Override
    public void display() {
        if (isFaceUp || isMatched) {
            System.out.print(" " + fruit + " ");
        } else {
            System.out.print(" * ");
        }
    }
}

// Class to represent a deck of cards
class Deck {
    private List<Card> cards;

    public Deck(int numPairs) {
        cards = new ArrayList<>();

        // Generate a list of fruit names based on the number of pairs
        List<String> fruitNames = generateFruitNames(numPairs);

        // Create pairs of cards with the same fruit and add them to the deck
        for (int i = 0; i < numPairs; i++) {
            String fruit = fruitNames.get(i);
            cards.add(new Card(fruit));
            cards.add(new Card(fruit));
        }

        // Shuffle the deck of cards
        Collections.shuffle(cards);
    }

    // Helper method to generate a list of fruit names
    private List<String> generateFruitNames(int numPairs) {
        List<String> fruitNames = new ArrayList<>();

        String[] fruits = { "Apple", "Banana", "Cherry", "Grape", "Lemon", "Orange", "Peach", "Pear", "Strawberry",
                "Watermelon" };

        // Add fruit names to the list based on the number of pairs
        for (int i = 0; i < numPairs; i++) {
            fruitNames.add(fruits[i]);
        }

        return fruitNames;
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public int size() {
        return cards.size();
    }

    public boolean isGameOver() {
        // Check if all cards in the deck have been matched
        for (Card card : cards) {
            if (!card.isMatched()) {
                return false;
            }
        }
        return true;
    }
}

// Class to represent a player
class Player {
    private String name;
    private int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }
}

// Class to represent the game
class Game {
    private Player player;
    private Deck deck;
    private Scanner scanner;
    private int level;

    public Game() {
        scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();
        player = new Player(playerName);
        level = 1;
    }

    public void play() {
        System.out.println("Memory Game");
        System.out.println("------------");

        while (level <= 5) {
            System.out.println("Level " + level);
            System.out.println("------------");

            int numPairs = level * 2;
            deck = new Deck(numPairs);

            while (!deck.isGameOver()) {
                displayCards();
                int card1Index = getCardIndex();
                int card2Index = getCardIndex();

                Card card1 = deck.getCard(card1Index);
                Card card2 = deck.getCard(card2Index);

                if (!card1.isMatched() && !card2.isMatched()) {
                    card1.setFaceUp(true);
                    card2.setFaceUp(true);

                    if (card1.getFruit().equals(card2.getFruit())) {
                        System.out.println("Match found!");
                        player.incrementScore();
                        card1.setMatched(true);
                        card2.setMatched(true);
                    } else {
                        System.out.println("No match. Try again.");
                    }
                } else {
                    System.out.println("One or both cards have already been matched. Try again.");
                }

                System.out.println();
            }

            System.out.println("Level " + level + " completed!");
            System.out.println("Score: " + player.getScore());
            System.out.println();

            level++;
        }

        System.out.println("Congratulations, " + player.getName() + "! You beat all levels with a final score of " + player.getScore() + ".");
    }

    private void displayCards() {
        System.out.println("\nCurrent Cards:");

        for (int i = 1; i <= deck.size(); i++) {
            Card card = deck.getCard(i - 1);

            if (card.isMatched()) {
                System.out.print("  -  "); // Display matched cards as a separate category
            } else {
                card.display();
            }

            // Add a line break after every 4 cards for better readability
            if (i % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    private int getCardIndex() {
        int cardIndex;
        do {
            System.out.print("Enter card index (1 to " + deck.size() + "): ");
            String input = scanner.nextLine();
            try {
                cardIndex = Integer.parseInt(input);
                if (cardIndex < 1 || cardIndex > deck.size()) {
                    System.out.println("Invalid number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                cardIndex = -1;
            }
        } while (cardIndex < 1 || cardIndex > deck.size());

        return cardIndex - 1;
    }
}

public class MemoryGame {
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
