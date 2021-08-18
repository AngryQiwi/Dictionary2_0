package dictionary4.actions;

import dictionary4.dao.DaoImpl;

import java.util.Scanner;

public interface Action {
    void execute(DaoImpl dao, Scanner scanner);
}
