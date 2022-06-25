package pl.sda.trivia;

import pl.sda.trivia.api.Category;
import pl.sda.trivia.api.Difficulty;
import pl.sda.trivia.api.Type;
import pl.sda.trivia.error.DataFormatException;
import pl.sda.trivia.error.DataNotAvailableException;
import pl.sda.trivia.model.QuizToComplete;
import pl.sda.trivia.repository.CategoryRepository;
import pl.sda.trivia.repository.QuizRepository;
import pl.sda.trivia.repository.TriviaAPICategoryRepository;
import pl.sda.trivia.repository.TriviaAPIQuizRepository;
import pl.sda.trivia.service.QuizService;
import pl.sda.trivia.service.TriviaQuizService;

import java.util.*;

public class TriviaAppConsole {

    private static final QuizRepository quizRepo = new TriviaAPIQuizRepository();

    private static final CategoryRepository categoryRepo = new TriviaAPICategoryRepository();
    //    Wstrzykiwanie zależności
    private static final QuizService quizService = new TriviaQuizService(quizRepo);
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<QuizToComplete> list = new ArrayList<>();

    public static void main(String[] args) {

//        TODO: dopisz fragment, w którym użytkownik może wybierać
//        kategorię: jedna z listy kategorii lub dowolna
//        poziom trudności: łatwy, średni, trudny lub dowolny
//        typy pytań: wielokrotny, prawda/ fałsz, dowolny
//        liczbę pytań: od 1 do 50
//        TODO: dopisz możliwość powtarzania całego cyklu
//        TODO: mozna zrobić projekt ze strony OpenWeather (API) na wzór tego projektu


        showQuizToCompleteWithOptionToRepeat();
    }

    private static void showQuizToCompleteWithOptionToRepeat() {
        Set<QuizToComplete> quizSet = chooseQuizParameters();

        List<QuizToComplete> list = new ArrayList<>(quizSet);
        int currentQuiz = 0;

        resolveQuizByUser(list, currentQuiz);

        int points = quizService.evaluateQuizSet(new HashSet<>(list));
        System.out.println("Zdobyłeś: " + points + " punktów");
        System.out.println("Twoje poprawne odpowiedzi:");
        showQuizzesResults(list);
        System.out.println("Czy chcesz rozwiązać nowy quiz? Wybierz TAK lub NIE");
        String userChoiceOfPlay = scanner.nextLine().toLowerCase();

        if(userChoiceOfPlay.equalsIgnoreCase("TAK")) {
            showQuizToCompleteWithOptionToRepeat();
        }
    }

    private static void resolveQuizByUser(List<QuizToComplete> list, int currentQuiz) {
        exit:
        while (true) {

            QuizToComplete quizToComplete = list.get(currentQuiz);
            screenTemplate(quizToComplete);

            String userChoice = scanner.nextLine().toUpperCase(); // Ignoruje mi ten scanner
            switch (userChoice) {
                case "1":
                    if (quizToComplete.getOptions().size() > 0) {
                        quizToComplete.setAnswer(quizToComplete.getOptions().get(0));
                    }
                    break;
                case "2":
                    if (quizToComplete.getOptions().size() > 0) {
                        quizToComplete.setAnswer(quizToComplete.getOptions().get(1));
                    }
                    break;
                case "3":
                    if (quizToComplete.getOptions().size() > 0) {
                        quizToComplete.setAnswer(quizToComplete.getOptions().get(2));
                    }
                    break;
                case "4":
                    if (quizToComplete.getOptions().size() > 0) {
                        quizToComplete.setAnswer(quizToComplete.getOptions().get(3));
                    }
                    break;
                case "N":
                    if (currentQuiz < list.size() - 1) {
                        currentQuiz++;
                    }
                    break;
                case "P":
                    if (currentQuiz > 0) {
                        currentQuiz--;
                    }
                    break;
                case "K":
                    break exit;
            }
        }
    }

    private static int getAndSetAmount() {
        int numberOfQuestion;
        int amount = 0;
        do {
            System.out.print("Wybierz liczbę pytań testowych (od 1 do 50): ");
            numberOfQuestion = scanner.nextInt();
            scanner.nextLine();

            if (numberOfQuestion > 0 && numberOfQuestion < 51) {
                amount = numberOfQuestion;
                break;
            } else {
                System.out.println("Wybrana liczba pytań spoza zakresu, spróbuj jeszcze raz!");
            }
        } while (numberOfQuestion >= 51 || numberOfQuestion <= 0);
        return amount;
    }

    private static Type getAndSetType() {
        String typeString;
        Type type = Type.ANY;
        do {
            System.out.print("Wybierz typ pytań testowych (wielokrotny, prawda/fałsz, dowolny): ");
            typeString = scanner.nextLine();

            if (typeString.equalsIgnoreCase("wielokrotny")) {
                type = Type.MULTIPLE_CHOICE;
                break;
            } else if (typeString.equalsIgnoreCase("prawda/fałsz")) {
                type = Type.TRUE_FALSE;
                break;
            } else if (typeString.equalsIgnoreCase("dowolny")) {
                type = Type.ANY;
                break;
            } else {
                System.out.println("Nie wybrano typu pytań, spróbuj jeszcze raz!");
            }
        } while (typeString != "wielokrotny".toLowerCase() && typeString != "prawda/fałsz".toLowerCase()
                && typeString != "dowolny".toLowerCase() && !typeString.isBlank());
        return type;
    }

    private static Difficulty getAndSetDifficulty() {
        String level;
        Difficulty difficulty = Difficulty.ANY;
        do {
            System.out.print("Wybierz poziom trudności (łatwy, średni, trudny lub dowolny): ");
            level = scanner.nextLine(); // Ignoruje mi ten scanner

            if (level.equalsIgnoreCase("łatwy")) {
                difficulty = Difficulty.EASY;
                break;
            } else if (level.equalsIgnoreCase("średni")) {
                difficulty = Difficulty.MEDIUM;
                break;
            } else if (level.equalsIgnoreCase("trudny")) {
                difficulty = Difficulty.HARD;
                break;
            } else if (level.equalsIgnoreCase("dowolny")) {
                difficulty = Difficulty.ANY;
                break;
            } else {
                System.out.println("Nie wybrano poziomu, spróbuj jeszcze raz!");
            }
        } while (level != "łatwy".toLowerCase() && level != "średni".toLowerCase()
                && level != "trudny".toLowerCase() && level != "dowolny".toLowerCase()
                || level.isBlank());
        return difficulty;
    }

    private static int getAndSetCategoryId() {
        int categoryIndex;
        int categoryId = 0;
        do {
            System.out.print("Wybierz numer kategorii od 1 do 32: ");
            categoryIndex = scanner.nextInt();
            scanner.nextLine();

            if (categoryIndex > 0 && categoryIndex < 33) {
                categoryId = categoryIndex;
            } else {
                System.out.println("Nie wybrano kategorii, spróbuj jeszcze raz!");
            }
        } while (categoryIndex <= 0 || categoryIndex >= 33);
        return categoryId;
    }

    // Zdefiniuj metodę screenTemplate, aby pobierała dane z quiz
    static void screenTemplate(QuizToComplete quiz) {
//        System.out.println("Pytanie ?");
//        System.out.println("---------"); // taka sama szerokość jak długość pytania
//        System.out.println("1.[] Opcja 1"); // liczba opcji wynika z kolekcji options
//        System.out.println("2.[] Opcja 2");
//        System.out.println("3.[x] Opcja 3"); // Zaznaczenie udzielonej odpowiedzi
//        System.out.println("4.[] Opcja 4");
//        System.out.println("Menu: odpowiedź: 1, 2, 3, 4 lub 1, 2;" +
//                " następne pytanie: N; poprzednie: P; koniec: K");

        int count = 1;
//
        String showedQuestion = quiz.getQuestion();
        System.out.println(showedQuestion);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < showedQuestion.length(); i++) {
            sb.append("-");
        }
        String delimiter = sb.toString();
        System.out.println(delimiter);

        for (String s : quiz.getOptions()) {

            if (s.equals(quiz.getAnswer())) {
                System.out.println(count + "[x] " + s);
            } else {
                System.out.println(count + "[] " + s);
            }
            count++;
        }
        System.out.println("Menu: odpowiedź: 1, 2, 3, 4 lub 1, 2;" +
                " następne pytanie: N; poprzednie: P; koniec: K");
    }

    static void showQuizzesResults(List<QuizToComplete> quizzes) {

        for (var quiz : quizzes) {

            int count = 0;

            String showedQuestion = quiz.getQuestion();
            System.out.println(showedQuestion);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < showedQuestion.length(); i++) {
                sb.append("-");
            }
            String delimiter = sb.toString();
            System.out.println(delimiter);

            for (String s : quiz.getOptions()) {

                if (s.equals(quiz.getAnswer()) && s.equals(quiz.getCorrectAnswer())) {
                    System.out.println(count + "[x] " + s);

                }
                else if (s.equals(quiz.getAnswer())) {
                    System.out.println(count + "[-] " + s);

                }
                else if (s.equals(quiz.getCorrectAnswer())) {
                    System.out.println(count + "[*] " + s);
                } else {
                    System.out.println(count + "[] " + s);
                }
                count++;
            }
        }
    }

    static Set<QuizToComplete> chooseQuizParameters() {

        List<Category> categories = categoryRepo.findAll();

        Set<QuizToComplete> quizSet;

        try {
            quizSet = quizService.findQuizSet(getAndSetAmount(),
                   getAndSetDifficulty(), getAndSetType(), categories.get(getAndSetCategoryId()));
        } catch (DataNotAvailableException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Aplikacja nie może działać.");
            return null;

        } catch (DataFormatException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
        return quizSet;
    }
}

