package dictionary4;

import dictionary4.actions.Action;
import dictionary4.dao.DaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class DictionaryConfig {
    private String[] dictionary;
    private String regex;
    @Bean
    public Map<String, String[]> dictionaries(){
        return Map.of(
                "1", new String[]{"src/main/resources/endict.txt", "[a-zA-Z]{4}"},
                "2", new String[]{"src/main/resources/numdict.txt", "\\d{5}"}
        );
    }
    @Bean
    public DaoImpl dao(Map<String, String[]> dictionaries){
        System.out.println("""
                Выберите словарь:
                   1. Английский(4 символа);
                   2. Числовой(5 символов).""");
        while ((dictionary = dictionaries.get(new Scanner(System.in).nextLine())) == null) {
            System.out.println("ОШИБКА: Такой словарь не существует");
        }
        regex = dictionary[1];
        return new DaoImpl(dictionary[0]);
    }
    @Bean
    public Program program(Action readAction, Action searchAction, Action addAction, Action updateAction, Action deleteAction, Action changeDictionaryAction, Action exitAction){
        return new Program(readAction, searchAction, addAction, updateAction, deleteAction, changeDictionaryAction, exitAction);
    }

    @Bean(name = "addAction")
    public Action addAction() {
        return (dao, scanner) -> {
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
    @Bean(name = "deleteAction")
    public Action deleteAction() {
        return (dao, scanner) -> {
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
    @Bean(name = "searchAction")
    public Action searchAction() {
        return (dao, scanner) -> {
            System.out.println("Введите слово");
            ArrayList<String[]> fundedEntries = dao.search(scanner.nextLine());
            if (fundedEntries.isEmpty()) System.out.println("Значений не найдено");
            for (String[] fundedEntry :
                    fundedEntries) {
                System.out.println(fundedEntry[0] + " - " + fundedEntry[1]);
            }
        };
    }
    @Bean(name = "updateAction")
    public Action updateAction() {
        return (dao, scanner) -> {
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
    @Bean(name = "readAction")
    public Action readAction() {
        return (dao, scanner) -> {
            ArrayList<String> values = dao.read();
            for (String value :
                    values) {
                System.out.println(value);
            }
        };
    }
    @Bean(name = "changeDictionaryAction")
    public Action changeDictionaryAction(Map<String, String[]> dictionaries) {
        return (dao, scanner) -> {
            System.out.println("""
                Выберите словарь:
                   1. Английский(4 символа);
                   2. Числовой(5 символов).""");
            while ((dictionary = dictionaries.get(new Scanner(System.in).nextLine())) == null) {
                System.out.println("ОШИБКА: Такой словарь не существует");
            }
            regex = dictionary[1];
            dao.setPathToFile(dictionary[0]);};
    }
    @Bean(name = "exitAction")
    public Action exitAction() {
        return (dao, scanner) -> System.exit(0);
    }
}
