package dictionary4;

import dictionary4.actions.Action;
import dictionary4.dao.DaoImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Scanner;

public class Program {
    @Autowired
    private DaoImpl dao;
    @Autowired
    private Action readAction;
    @Autowired
    private Action searchAction;
    @Autowired
    private Action addAction;
    @Autowired
    private Action updateAction;
    @Autowired
    private Action deleteAction;
    @Autowired
    private Action changeDictionaryAction;
    @Autowired
    private Action exitAction;
    private final Map<String, Action> actions;

    public Program(Action readAction, Action searchAction, Action addAction, Action updateAction, Action deleteAction, Action changeDictionaryAction, Action exitAction) {
        actions = Map.of(
                "1", readAction,
                "2", searchAction,
                "3", addAction,
                "4", updateAction,
                "5", deleteAction,
                "6", changeDictionaryAction,
                "7", exitAction
        );
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
                actions.get(scanner.nextLine()).execute(dao, scanner);
            } catch (NullPointerException ignored) {
            }
        }
    }
}
