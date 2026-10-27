import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class User {
    private String name;
    private double balance;
    private HashMap<String, Integer> portfolio; // Symbol -> Quantity

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.portfolio = new HashMap<>();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public HashMap<String, Integer> getPortfolio() {
        return portfolio;
    }

    public void buyStock(Stock stock, int quantity) {
        double totalCost = stock.getPrice() * quantity;
        if (balance >= totalCost) {
            balance -= totalCost;
            portfolio.put(stock.getSymbol(), portfolio.getOrDefault(stock.getSymbol(), 0) + quantity);
            System.out.println("Successfully bought " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("Error: Insufficient balance!");
        }
    }

    public void sellStock(Stock stock, int quantity) {
        int owned = portfolio.getOrDefault(stock.getSymbol(), 0);
        if (owned >= quantity) {
            portfolio.put(stock.getSymbol(), owned - quantity);
            balance += stock.getPrice() * quantity;
            System.out.println("Successfully sold " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("Error: You don't own enough shares to sell!");
        }
    }

    public void displayPortfolio(HashMap<String, Stock> market) {
        System.out.println("\n--- " + name + "'s Portfolio ---");
        System.out.println("Cash Balance: $" + String.format("%.2f", balance));
        System.out.println("Owned Stocks:");
        if (portfolio.isEmpty()) {
            System.out.println("  No stocks owned yet.");
        } else {
            for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
                String symbol = entry.getKey();
                int qty = entry.getValue();
                if (qty > 0) {
                    double currentVal = qty * market.get(symbol).getPrice();
                    System.out.println("  - " + symbol + ": " + qty + " shares (Value: $" + String.format("%.2f", currentVal) + ")");
                }
            }
        }
        System.out.println("-----------------------------");
    }
}

public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize Market Stocks
        HashMap<String, Stock> market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", 180.50));
        market.put("GOOGL", new Stock("GOOGL", 140.20));
        market.put("TSLA", new Stock("TSLA", 210.00));

        // Create a user with $5,000 starting cash
        User user = new User("TraderJoe", 5000.00);

        boolean running = true;
        while (running) {
            System.out.println("\n=== STOCK TRADING PLATFORM ===");
            System.out.println("1. View Market Prices");
            System.out.println("2. View Portfolio & Balance");
            System.out.println("3. Buy Stock");
            System.out.println("4. Sell Stock");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
               case 1:
    System.out.println("\n--- Market Prices ---");
    for (Stock stock : market.values()) {
        System.out.println(stock.getSymbol() + " : $" + stock.getPrice());
    }
    break;

                case 2:
                    user.displayPortfolio(market);
                    break;

                case 3:
                    System.out.print("Enter stock symbol to buy (AAPL, GOOGL, TSLA): ");
                    String buySymbol = scanner.next().toUpperCase();
                    if (market.containsKey(buySymbol)) {
                        System.out.print("Enter quantity: ");
                        int qty = scanner.nextInt();
                        user.buyStock(market.get(buySymbol), qty);
                    } else {
                        System.out.println("Invalid stock symbol!");
                    }
                    break;

                case 4:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSymbol = scanner.next().toUpperCase();
                    if (market.containsKey(sellSymbol)) {
                        System.out.print("Enter quantity: ");
                        int qty = scanner.nextInt();
                        user.sellStock(market.get(sellSymbol), qty);
                    } else {
                        System.out.println("Invalid stock symbol!");
                    }
                    break;

                case 5:
                    running = false;
                    System.out.println("Thank you for trading!");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }
}