import java.util.HashMap;
import java.util.Map;

public class ArithmeticEncoding {

    public static void arithmeticEncode(HashMap<Character, Double> probabilities, String string, int lengthToCode) {

        System.out.println("Arithmetic encoding for " + lengthToCode + " of chars of : " + string);
        double start = 0;
        double end = 1;
        for (int i = 0; i < lengthToCode; i++) {
            char currentChar = string.charAt(i);
            double Ci = 0.0;
            for (Map.Entry<Character, Double> entry : probabilities.entrySet()) {
                if (entry.getKey().equals(currentChar)) {
                    break;
                }
                Ci = Ci + entry.getValue();
            }

            double width = end - start;
            start = start + (width * Ci);
            end = start + (width * probabilities.get(currentChar));

            double k = Math.ceil((Math.log(1/(end-start))/Math.log(2))) + 1;
            double tag = (end+start)/2;
            System.out.println(currentChar + " starts: " + start + " end: " + end + " TAG: " + tag +  " K: " + k);
        }
        System.out.println();
    }
}
