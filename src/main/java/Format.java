import java.util.*;

public class Format {

    public static String formatStringForPrint(Map<String, String> cellsMap) {
        Set<Double> setRow = new TreeSet<>();
        Set<Double> setColumn = new TreeSet<>();

        for (Map.Entry<String, String> entry : cellsMap.entrySet()) {
            String key = entry.getKey();
            List<Double> cellLocation = formatCellCoordinatesToNumber(key);
            setRow.add(cellLocation.get(0));
            setColumn.add(cellLocation.get(1));
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Double tempRow : setRow) {
            String prefix = "";
            for (Double tempCol : setColumn) {
                for (Map.Entry<String, String> entry : cellsMap.entrySet()) {
                    String key = entry.getKey();
                    boolean error = ErrorKeys.checkKey(key);
                    List<Double> cellLocation = formatCellCoordinatesToNumber(key);
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
                            stringBuilder.append("Invalid format: " + ErrorKeys.errorKeyMap.get(key));
                        }
                    }
                }
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public static ArrayList<Double> formatCellCoordinatesToNumber(String simbol) {
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
            for (Map.Entry<Integer, Character> entry : Alphabet.alphabetMap.entrySet()) {
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

    public static String formatCellСoordinatesToSimbol(Integer columnNumber, Integer rowNumber) {
        ArrayList<Integer> rowNumber26 = new ArrayList();

        while (rowNumber != 0) {
            rowNumber26.add(rowNumber % 26);
            rowNumber = rowNumber / 26;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = rowNumber26.size() - 1; i >= 0; i--) {
            stringBuilder.append(Alphabet.alphabetMap.get(rowNumber26.get(i)));
        }

        return stringBuilder.toString() + columnNumber;
    }

}
