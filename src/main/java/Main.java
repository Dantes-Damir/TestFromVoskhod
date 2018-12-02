
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.io.*;
import java.util.*;

public class Main {
    static Map<String, String> cellsMap = new HashMap<>();
    static Map<String, String> errorKey = new HashMap<>();
    static Map<Integer, Character> alphabetMap = new HashMap<Integer, Character>() {{
        put(1, 'A');
        put(2, 'B');
        put(3, 'C');
        put(4, 'D');
        put(5, 'E');
        put(6, 'F');
        put(7, 'G');
        put(8, 'H');
        put(9, 'I');
        put(10, 'J');
        put(11, 'K');
        put(12, 'L');
        put(13, 'M');
        put(14, 'N');
        put(15, 'O');
        put(16, 'P');
        put(17, 'Q');
        put(18, 'R');
        put(19, 'S');
        put(20, 'T');
        put(21, 'U');
        put(22, 'V');
        put(23, 'W');
        put(24, 'X');
        put(25, 'Y');
        put(26, 'Z');
    }};

    public static void main(String[] args) throws Exception {
        readTable();
        calculateTable();
        printTable();

    }

    public static void readTable() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/example.csv"))) {


            String nextLine;
            int countColumn = 0;

            while ((nextLine = reader.readLine()) != null) {
                countColumn++;
                if (nextLine != null) {
                    String[] rowMass = nextLine.split(";");

                    for (int i = 0; i < rowMass.length; i++) {
                        if (countColumn == 1 && i == 0) {
                            cellsMap.put(getFormatCellSimbol(countColumn, i + 1), rowMass[i].substring(1));
                        } else {
                            cellsMap.put(getFormatCellSimbol(countColumn, i + 1), rowMass[i]);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден или заблокирован!");
            System.exit(-1);
        }
    }

    public static void calculateTable() {
        for (Map.Entry<String, String> entry : cellsMap.entrySet()) {
            String key = entry.getKey();
            try {
                cellsMap.put(key, calculateExpressions(entry.getValue()).toString());
            } catch (FormatException e) {
                errorKey.put(key, e.getMessage());
            }
        }
    }

    public static void printTable() throws Exception {
        String resultString = getStringForPrint();
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("./src/main/resources/example2.csv"), "UTF-8"))) {
            pw.write(resultString);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден или заблокирован!");
            System.exit(-1);
        }
    }

    public static String getStringForPrint() {
        Set<Double> setRow = new TreeSet<>();
        Set<Double> setColumn = new TreeSet<>();

        for (Map.Entry<String, String> entry : cellsMap.entrySet()) {
            String key = entry.getKey();
            List<Double> cellLocation = getFormatCellNumber(key);
            setRow.add(cellLocation.get(0));
            setColumn.add(cellLocation.get(1));
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Double tempRow : setRow) {
            String prefix = "";
            for (Double tempCol : setColumn) {
                for (Map.Entry<String, String> entry : cellsMap.entrySet()) {
                    String key = entry.getKey();
                    boolean error = checkKey(key);
                    List<Double> cellLocation = getFormatCellNumber(key);
                    if (!error) {
                        if (cellLocation.get(0).equals(tempRow) && cellLocation.get(1).equals(tempCol)) {
                            stringBuilder.append(prefix);
                            prefix = ";";
                            stringBuilder.append(entry.getValue());
                        }
                    } else {
                        if (cellLocation.get(0).equals(tempRow) && cellLocation.get(1).equals(tempCol)) {
                            stringBuilder.append(prefix);
                            prefix = ";";
                            stringBuilder.append("Invalid format: " + errorKey.get(key));
                        }
                    }
                }
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public static boolean checkKey(String key) {
        for (Map.Entry<String, String> entry : errorKey.entrySet()) {
            if (entry.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Double> getFormatCellNumber(String simbol) {
        ArrayList<Double> resultList = new ArrayList<>();
        ArrayList<Character> letterChar = new ArrayList<>();
        ArrayList<Double> letterDouble = new ArrayList<>();
        Double numberRow;
        Double numberColumn = 0.0;
        char[] chars = simbol.toCharArray();
        int count = 0;

        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                break;
            }
            letterChar.add(chars[i]);
            count++;
        }

        numberRow = Double.parseDouble(simbol.substring(count));


        for (int i = 0; i < letterChar.size(); i++) {
            for (Map.Entry<Integer, Character> entry : alphabetMap.entrySet()) {
                if (entry.getValue() == letterChar.get(i)) {
                    count--;
                    double m = Math.pow(26, count);
                    letterDouble.add(i, entry.getKey() * m);
                }
            }
        }

        for (Double temp : letterDouble) {
            numberColumn += temp;
        }

        resultList.add(numberRow);
        resultList.add(numberColumn);
        return resultList;
    }

    public static String getFormatCellSimbol(Integer columnNumber, Integer rowNumber) {
        ArrayList<Integer> rowNumber26 = new ArrayList();

        while (rowNumber != 0) {
            rowNumber26.add(rowNumber % 26);
            rowNumber = rowNumber / 26;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = rowNumber26.size() - 1; i >= 0; i--) {
            stringBuilder.append(alphabetMap.get(rowNumber26.get(i)));
        }

        return stringBuilder.toString() + columnNumber;
    }

    public static Double calculateExpressions(String a) throws FormatException {
        String[] mas = a.split("\\+|\\/|\\*|\\-");
        ArrayList<Double> numberList = new ArrayList<>();
        Double result;

        for (int i = 0; i < mas.length; i++) {
            try {
                numberList.add(Double.parseDouble(cellsMap.get(mas[i])));
            } catch (NumberFormatException e) {
                numberList.add(calculateExpressions(cellsMap.get(mas[i])));
            } catch (NullPointerException e) {
                try {
                    numberList.add(Double.parseDouble(mas[i]));
                } catch (NumberFormatException q) {
                    System.out.println("Неверный формат записи: " + mas[i]);
                    FormatException formatException = new FormatException();
                    formatException.setMessage(mas[i]);
                    throw formatException;
                }
            }
        }

        result = getResultOperations(a, numberList);
        return result;
    }

    public static Double getResultOperations(String a, ArrayList<Double> numberList) {
        Double result;
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Character> operators = getOperators(a);

        for (int i = 0; i < numberList.size(); i++) {
            stringBuilder.append(numberList.get(i));

            if (i < operators.size()) {
                stringBuilder.append(operators.get(i));
            }
        }

        Expression e = new ExpressionBuilder(stringBuilder.toString()).build();
        result = e.evaluate();

        return result;
    }

    public static ArrayList<Character> getOperators(String a) {
        char[] charArray = a.toCharArray();
        ArrayList<Character> operators = new ArrayList();

        for (int i = 0; i < charArray.length; i++) {
            switch (charArray[i]) {
                case '+':
                    operators.add('+');
                    break;
                case '-':
                    operators.add('-');
                    break;
                case '*':
                    operators.add('*');
                    break;
                case '/':
                    operators.add('/');
                    break;
            }
        }
        return operators;
    }
}
