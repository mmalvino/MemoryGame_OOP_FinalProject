import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

// Interface for displaying objects
interface Displayable {
    void display();
}

// Represents a card in the memory game
class Card implements Displayable {
    private String fruit;
    private boolean isFaceUp;
    private boolean isMatched;

    // Constructor to initialize the Card object with a fruit name
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
        if (isMatched) {
            System.out.print(" - ");
        } else if (isFaceUp) {
            System.out.print(" " + fruit + " ");
        } else {
            System.out.print(" * ");
        }
    }
}

// Represents a deck of cards in the memory game
class Deck {
    private List<Displayable> cards;

    // Constructor to initialize the Deck with a specified number of pairs
    public Deck(int numPairs) {
        cards = new ArrayList<>();

        // Generate fruit names for the pairs
        List<String> fruitNames = generateFruitNames(numPairs);

        // Create pairs of cards with the same fruit and add them to the deck
        for (int i = 0; i < numPairs; i++) {
            String fruit = fruitNames.get(i);
            cards.add(new Card(fruit));
            cards.add(new Card(fruit));
        }

        // Shuffle the cards
        Collections.shuffle(cards);
    }

    // Generate a list of fruit names for the pairs
    private List<String> generateFruitNames(int numPairs) {
        List<String> fruitNames = new ArrayList<>();

        // List of available fruit names
        String[] fruits = { "Apple", "Banana", "Cherry", "Grape", "Lemon", "Orange", "Peach", "Pear", "Strawberry",
                "Watermelon" };

        // Select numPairs number of fruit names from the available list
        for (int i = 0; i < numPairs; i++) {
            fruitNames.add(fruits[i]);
        }

        return fruitNames;
    }

    // Get the card at a specific index in the deck
    public Displayable getCard(int index) {
        return cards.get(index);
    }

    // Get the total number of cards in the deck
    public int size() {
        return cards.size();
    }

    // Check if all cards in the deck are matched
    public boolean isGameOver() {
        for (Displayable card : cards) {
            if (!((Card) card).isMatched()) {
                return false;
            }
        }
        return true;
    }
}

// Represents a player in the memory game
class Player {
    private String name;
    private int score;

    // Constructor to initialize the Player with a name and score of zero
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

    // Increment the player's score by 1
    public void incrementScore() {
        score++;
    }
}

// Represents the game logic and flow
class Game {
    private Player player;
    private Deck deck;
    private Scanner scanner;
    private int level;

    // Constructor to initialize the game, player, and level
    public Game() {
        scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();
        player = new Player(playerName);
        level = 1;
    }

    // Start playing the memory game
    public void play() {
        System.out.println("Memory Game");
        System.out.println("------------");

        // Play each level until level 5
        while (level <= 5) {
            System.out.println("Level " + level);
            System.out.println("------------");

            int numPairs = level * 2;
            deck = new Deck(numPairs);

            // Play the game until all cards are matched
            while (!deck.isGameOver()) {
                displayCards();

                // Get the indices of two cards to compare
                int card1Index = getCardIndex();
                int card2Index = getCardIndex();

                // Check if the selected cards are different
                if (card1Index != card2Index) {
                    // Get the actual card objects from the deck
                    Displayable card1 = deck.getCard(card1Index);
                    Displayable card2 = deck.getCard(card2Index);

                    Card realCard1 = (Card) card1;
                    Card realCard2 = (Card) card2;

                    // Check if both cards are face down and not yet matched
                    if (!realCard1.isMatched() && !realCard2.isMatched()) {
                        realCard1.setFaceUp(true);
                        realCard2.setFaceUp(true);

                        // Check if the fruits on the cards match
                        if (realCard1.getFruit().equals(realCard2.getFruit())) {
                            System.out.println("Match found!");
                            player.incrementScore();
                            realCard1.setMatched(true);
                            realCard2.setMatched(true);
                        } else {
                            System.out.println("No match. Try again.");
                        }
                    } else {
                        System.out.println("One or both cards have already been matched. Try again.");
                    }
                } else {
                    System.out.println("Same card selected. Try again.");
                }

                System.out.println();
            }

            System.out.println("Level " + level + " completed!");
            System.out.println("Score: " + player.getScore());
            System.out.println();

            level++;
        }

        System.out.println(
                "Congratulations, " + player.getName() + "! You beat all levels with a final score of " + player.getScore() + ".");
    }

    // Display the current state of the cards
    private void displayCards() {
        System.out.println("\nCurrent Cards:");

        // Display the cards in a grid-like format
        for (int i = 1; i <= deck.size(); i++) {
            Displayable displayable = deck.getCard(i - 1);
            displayable.display();

            if (i % 4 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    // Get the index of a card from user input
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

// Main class to start the memory game
public class MemoryGame {
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
