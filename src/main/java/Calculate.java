import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.Map;

public class Calculate {

    public static Double calculateExpressions(String a, Map<String, String> cellsMap) throws FormatException {
        String[] mas = a.split("\\+|\\/|\\*|\\-");
        ArrayList<Double> numberList = new ArrayList<>();
        Double result;

        for (int i = 0; i < mas.length; i++) {
            try {
                numberList.add(Double.parseDouble(cellsMap.get(mas[i])));
            } catch (NumberFormatException e) {
                numberList.add(calculateExpressions(cellsMap.get(mas[i]), cellsMap));
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
