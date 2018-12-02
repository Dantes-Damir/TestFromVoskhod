import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Table {
    private Map<String, String> cellsMap = new HashMap<>();

    public Map<String, String> getCellsMap() {
        return cellsMap;
    }

    public void setCellsMap(Map<String, String> cellsMap) {
        this.cellsMap = cellsMap;
    }

    public void readTable() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/example.csv"))) {
            String nextLine;
            int countColumn = 0;

            while ((nextLine = reader.readLine()) != null) {
                countColumn++;
                if (nextLine != null) {
                    String[] rowMass = nextLine.split(";");

                    for (int i = 0; i < rowMass.length; i++) {
                        if (countColumn == 1 && i == 0) {
                            cellsMap.put(Format.formatCellСoordinatesToSimbol(countColumn, i + 1), rowMass[i].substring(1));
                        } else {
                            cellsMap.put(Format.formatCellСoordinatesToSimbol(countColumn, i + 1), rowMass[i]);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден или заблокирован!");
            System.exit(-1);
        }
    }

    public void calculateTable() {
        for (Map.Entry<String, String> entry : cellsMap.entrySet()) {
            String key = entry.getKey();
            try {
                cellsMap.put(key, Calculate.calculateExpressions(entry.getValue(), this.getCellsMap()).toString());
            } catch (FormatException e) {
                ErrorKeys.errorKeyMap.put(key, e.getMessage());
            }
        }
    }

    public void printTable() throws Exception {
        String resultString = Format.formatStringForPrint(this.getCellsMap());
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("./src/main/resources/example2.csv"), "UTF-8"))) {
            pw.write(resultString);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден или заблокирован!");
            System.exit(-1);
        }
    }
}
