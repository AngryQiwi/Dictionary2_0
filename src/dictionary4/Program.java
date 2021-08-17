package dictionary4;

import dictionary4.actions.Action;
import dictionary4.dao.DaoImpl;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {
    private static final Map<String, String[]> dictionaries = Map.of(
            "1", new String[]{"src/dictionary4/dictfiles/endict.txt", "[a-zA-Z]{4}"},
            "2", new String[]{"src/dictionary4/dictfiles/numdict.txt", "\\d{5}"}
    );
    private static DaoImpl dao;
    private static String regex;
    private final Map<String, Action> actions = Map.of(
            "1", getReadAction(),
            "2", getSearchAction(),
            "3", getAddAction(),
            "4", getUpdateAction(),
            "5", getDeleteAction(),
            "6", getChangeDictionaryAction(),
            "7", getExitAction()
    );

    private Program() {
    }

    public static Program init() {
        System.out.println("""
                Выберите словарь:
                   1. Английский(4 символа);
                   2. Числовой(5 символов).""");
        String[] dictionary;
        while ((dictionary = dictionaries.get(new Scanner(System.in).nextLine())) == null) {
            System.out.println("ОШИБКА: Такой словарь не существует");
        }
        dao = new DaoImpl(dictionary[0]);
        regex = dictionary[1];
        return new Program();
    }

    public void run() {
        var scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    Выберите действие:
                       1. Показать всё;
                       2. Найти;
                       3. Добавить;
                       4. Редактировать;
                       5. Удаление;
                       6. Смена словаря;
                       7. Выход.""");
            try {
                actions.get(scanner.nextLine()).execute(scanner);
            } catch (NullPointerException ignored) {
            }
        }
    }


    private Action getAddAction() {
        return scanner -> {
            System.out.println("Введите слово");
            String key = scanner.nextLine();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(key);
            if (matcher.matches()) {
                System.out.println("Введите перевод");
                String value = scanner.nextLine();
                if(value.equals("")){
                    System.out.println("ОШИБКА: Пустое значение");
                    return;
                }
                dao.write(key, value);
                return;
            }
            System.out.println("ОШИБКА: Некорректное значение");
        };
    }

    private Action getDeleteAction() {
        return scanner -> {
            System.out.println("Введите ключ значения, которое нужно удалить");
            String deletedKey = scanner.nextLine();
            ArrayList<String[]> fundedEntries = dao.search(deletedKey);
            if (fundedEntries.isEmpty()) {
                System.out.println("Значений не найдено");
                return;
            }
            System.out.println("Введите перевод, который нужно удалить:");
            for (String[] entry :
                    fundedEntries) {
                System.out.println("    " + (fundedEntries.indexOf(entry) + 1) + ". " + entry[0] + " - " + entry[1]);
            }
            String deletedValue = scanner.nextLine();
            dao.delete(fundedEntries.get(Integer.parseInt(deletedValue) - 1));
        };
    }

    private Action getSearchAction() {
        return scanner -> {
            System.out.println("Введите слово");
            ArrayList<String[]> fundedEntries = dao.search(scanner.nextLine());
            if (fundedEntries.isEmpty()) System.out.println("Значений не найдено");
            for (String[] fundedEntry :
                    fundedEntries) {
                System.out.println(fundedEntry[0] + " - " + fundedEntry[1]);
            }
        };
    }

    private Action getUpdateAction() {
        return scanner -> {
            System.out.println("Введите ключ значения, которое нужно обновить");
            String deletedKey = scanner.nextLine();
            ArrayList<String[]> fundedEntries = dao.search(deletedKey);
            if (fundedEntries.isEmpty()) {
                System.out.println("Значений не найдено");
                return;
            }
            System.out.println("Введите перевод, который нужно обновить:");
            for (String[] entry :
                    fundedEntries) {
                System.out.println("    " + (fundedEntries.indexOf(entry) + 1) + ". " + entry[0] + " - " + entry[1]);
            }
            String deletedValue = scanner.nextLine();
            String[] deletedEntryArray = fundedEntries.get(Integer.parseInt(deletedValue) - 1);
            System.out.println("Введите новое значение");
            dao.update(deletedEntryArray, scanner.nextLine());
        };
    }

    private Action getReadAction() {
        return scanner -> {
            ArrayList<String> values = dao.read();
            for (String value :
                    values) {
                System.out.println(value);
            }
        };
    }

    private Action getChangeDictionaryAction() {
        return scanner -> init();
    }

    private Action getExitAction() {
        return scanner -> System.exit(0);
    }
}
