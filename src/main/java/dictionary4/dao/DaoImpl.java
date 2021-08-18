package dictionary4.dao;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static java.lang.String.join;

public class DaoImpl {
    private String pathToFile;

    public void setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public DaoImpl(String pathToFile) {
        this.pathToFile = pathToFile;
    }

    public ArrayList<String> read() {
        try {
            ArrayList<String> values;
            try (BufferedReader fileReader = new BufferedReader(new FileReader(pathToFile))) {
                values = new ArrayList<>();
                String value;
                while ((value = fileReader.readLine()) != null) {
                    values.add(value);
                }
            }
            return values;
        } catch (IOException e) {
            System.out.println("ОШИБКА: Файл поврежден или не существует");
            return new ArrayList<>();
        }
    }

    public boolean write(String key, String value) {
        try {
            try (FileWriter writer = new FileWriter(pathToFile, true)) {
                writer.write(key + '-' + value + '\n');
            }
        } catch (IOException e) {
            System.out.println("ОШИБКА: Файл поврежден или не существует");
            return false;
        }
        return true;
    }

    public boolean delete(String[] entry) {
        var key = entry[0];
        var value = entry[1];
        var readLines = read();

        try {

            var arrayToWrite = new ArrayList<String>();
            for (String line : readLines) {
                if (entryIsNotEqual(key, value, line)) {
                    arrayToWrite.add(line + "\n");
                }
            }

            Files.writeString(Path.of(pathToFile), join("", arrayToWrite));


        } catch (IOException ioException) {
            System.out.println("ОШИБКА: Файл поврежден или не существует");
            return false;
        }
        return true;
    }

    private boolean entryIsNotEqual(String key, String value, String line) {
        var kv = line.split("-");
        if (kv.length == 2) {
            return !key.equals(kv[0]) || !value.equals(kv[1]);
        }
        return true;
    }


    public boolean update(String[] entry, String newValue) {
        var key = entry[0];
        var value = entry[1];
        var readLines = read();

        try {

            var arrayToWrite = new ArrayList<String>();
            for (String line : readLines) {
                if (entryIsNotEqual(key, value, line)) {
                    arrayToWrite.add(line + "\n");
                } else {
                    arrayToWrite.add(key + '-' + newValue + '\n');
                }
            }

            Files.writeString(Path.of(pathToFile), join("", arrayToWrite));


        } catch (IOException ioException) {
            System.out.println("ОШИБКА: Файл поврежден или не существует");
            return false;
        }
        return true;
    }

    public ArrayList<String[]> search(String key) {
        var fundedEntries = new ArrayList<String[]>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(pathToFile));
        } catch (FileNotFoundException e) {
            System.out.println("ОШИБКА: Файл поврежден или не существует.");
            return null;
        }
        String readEntry = null;
        while (true) {
            try {
                readEntry = reader.readLine();
            } catch (IOException e) {
                System.out.println("ОШИБКА: Сбой чтения.");
            }
            if (readEntry == null) break;
            String[] potentialFundedEntry;
            potentialFundedEntry = new String[]{readEntry.split("-")[0], readEntry.split("-")[1]};
            if (potentialFundedEntry[0].equals(key))
                fundedEntries.add(potentialFundedEntry);
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fundedEntries;
    }

}
