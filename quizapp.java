import java.io.*;
import java.util.*;

class Question {
    String question;
    String[] options;
    char correctOption;

    public Question(String question, String[] options, char correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }
}

class QuizCategory {
    String name;
    List<Question> questions;

    public QuizCategory(String name) {
        this.name = name;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(String question, String[] options, char correctOption) {
        questions.add(new Question(question, options, correctOption));
    }

    public List<Question> getQuestions() {
        return questions;
    }
}

class LeaderboardEntry {
    String name;
    int score;

    public LeaderboardEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }
}

class QuizStats {
    int totalQuestions;
    int correctAnswers;
    long totalTime;

    public QuizStats(int totalQuestions, int correctAnswers, long totalTime) {
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.totalTime = totalTime;
    }

    public double getAccuracy() {
        return (totalQuestions == 0) ? 0 : (correctAnswers * 100.0 / totalQuestions);
    }

    public void displayStats() {
        System.out.println("\n===== QUIZ STATS =====");
        System.out.println("Total Questions: " + totalQuestions);
        System.out.println("Correct Answers: " + correctAnswers);
        System.out.println("Accuracy: " + getAccuracy() + "%");
        System.out.println("Total Time: " + totalTime / 1000.0 + " seconds");
    }
}

class Admin {
    public static void addNewCategory() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new category name: ");
        String name = sc.nextLine();
        QuizCategory newCat = new QuizCategory(name);
        System.out.print("How many questions to add?: ");
        int count = sc.nextInt();
        sc.nextLine();
        for (int i = 0; i < count; i++) {
            System.out.print("Enter question: ");
            String q = sc.nextLine();
            String[] opts = new String[4];
            for (int j = 0; j < 4; j++) {
                System.out.print((char) ('A' + j) + ": ");
                opts[j] = sc.nextLine();
            }
            System.out.print("Enter correct option (A/B/C/D): ");
            char correct = sc.nextLine().toUpperCase().charAt(0);
            newCat.addQuestion(q, opts, correct);
        }
        quizapp.categories.add(newCat);
        System.out.println("New category added successfully!");
    }
}

public class quizapp {
    static Scanner sc = new Scanner(System.in);
    static List<QuizCategory> categories = new ArrayList<>();
    static List<LeaderboardEntry> leaderboard = new ArrayList<>();

    public static void main(String[] args) {
        initCategories();
        loadLeaderboard();
        showMainMenu();
    }

    static void initCategories() {
        QuizCategory java = new QuizCategory("Java");
        java.addQuestion("What is JVM?", new String[]{"Java Virtual Machine", "Java Variable Method", "Just Virtual Method", "None"}, 'A');
        java.addQuestion("Which keyword is used to inherit a class in Java?", new String[]{"this", "extends", "super", "implements"}, 'B');
        java.addQuestion("Which method is the entry point in Java?", new String[]{"start()", "main()", "run()", "init()"}, 'B');
        categories.add(java);

        QuizCategory dsa = new QuizCategory("DSA");
        dsa.addQuestion("Which data structure uses LIFO?", new String[]{"Queue", "Array", "Stack", "List"}, 'C');
        dsa.addQuestion("Which sorting algorithm is fastest in average case?", new String[]{"Bubble", "Selection", "Quick", "Insertion"}, 'C');
        categories.add(dsa);

        QuizCategory aptitude = new QuizCategory("Aptitude");
        aptitude.addQuestion("If 5x = 20, then x = ?", new String[]{"2", "3", "4", "5"}, 'C');
        aptitude.addQuestion("Speed = Distance / ?", new String[]{"Time", "Mass", "Volume", "Velocity"}, 'A');
        categories.add(aptitude);
    }

    static void showMainMenu() {
        while (true) {
            System.out.println("\n===== QUIZ APP MAIN MENU =====");
            System.out.println("1. Start Quiz");
            System.out.println("2. Review Questions");
            System.out.println("3. Endless Quiz Mode");
            System.out.println("4. View Leaderboard");
            System.out.println("5. Admin - Add Category");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    startQuiz();
                    break;
                case 2:
                    reviewQuestions();
                    break;
                case 3:
                    endlessMode();
                    break;
                case 4:
                    showLeaderboard();
                    break;
                case 5:
                    Admin.addNewCategory();
                    break;
                case 6:
                    System.out.println("Thank you for using Quiz App. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    static void startQuiz() {
        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        QuizCategory selected = selectCategory();
        List<Question> quiz = selected.getQuestions();
        int score = 0;
        int correct = 0;
        long start = System.currentTimeMillis();

        for (Question q : quiz) {
            System.out.println("\n" + q.question);
            char opt = 'A';
            for (String o : q.options) {
                System.out.println(opt + ". " + o);
                opt++;
            }
            System.out.print("Your answer (A/B/C/D): ");
            char ans = sc.nextLine().toUpperCase().charAt(0);
            if (ans == q.correctOption) {
                score++;
                correct++;
            }
        }

        long end = System.currentTimeMillis();
        QuizStats stats = new QuizStats(quiz.size(), correct, end - start);
        stats.displayStats();

        System.out.println("\nYour score: " + score + "/" + quiz.size());
        leaderboard.add(new LeaderboardEntry(name, score));
        saveLeaderboard();
    }

    static QuizCategory selectCategory() {
        System.out.println("\nSelect Category:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).name);
        }
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        return categories.get(choice - 1);
    }

    static void reviewQuestions() {
        System.out.println("\n===== REVIEW MODE =====");
        for (QuizCategory cat : categories) {
            System.out.println("\nCategory: " + cat.name);
            for (Question q : cat.getQuestions()) {
                System.out.println("Q: " + q.question);
                char opt = 'A';
                for (String o : q.options) {
                    System.out.println(opt + ". " + o);
                    opt++;
                }
                System.out.println("Correct Answer: " + q.correctOption);
            }
        }
    }

    static void endlessMode() {
        System.out.println("\n===== ENDLESS MODE =====");
        Random rand = new Random();
        while (true) {
            QuizCategory cat = categories.get(rand.nextInt(categories.size()));
            Question q = cat.getQuestions().get(rand.nextInt(cat.getQuestions().size()));

            System.out.println("\nCategory: " + cat.name);
            System.out.println("Q: " + q.question);
            char opt = 'A';
            for (String o : q.options) {
                System.out.println(opt + ". " + o);
                opt++;
            }
            System.out.print("Your answer (A/B/C/D or Q to quit): ");
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("Q")) break;
        }
    }

    static void showLeaderboard() {
        System.out.println("\n===== LEADERBOARD =====");
        leaderboard.sort((a, b) -> b.score - a.score);
        int rank = 1;
        for (LeaderboardEntry entry : leaderboard) {
            System.out.println(rank + ". " + entry.name + " - " + entry.score);
            if (++rank > 10) break;
        }
    }

    static void saveLeaderboard() {
        try (PrintWriter writer = new PrintWriter("leaderboard.txt")) {
            for (LeaderboardEntry entry : leaderboard) {
                writer.println(entry.name + "," + entry.score);
            }
        } catch (IOException e) {
            System.out.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    static void loadLeaderboard() {
        try (BufferedReader reader = new BufferedReader(new FileReader("leaderboard.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    leaderboard.add(new LeaderboardEntry(parts[0], Integer.parseInt(parts[1])));
                }
            }
        } catch (IOException e) {
            // Ignore if file not found
        }
    }
}
