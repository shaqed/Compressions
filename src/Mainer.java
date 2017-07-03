import java.util.*;


public class Mainer {
    public static void main(String[] args) {

        go();

    }




    public static void go() {
        String str1 = "0101010101210101001001001";
        String str2 = "gospookgolookgoloon";
        String str3 = "COOKEDCOCOA";




        int[][] encodedMatrix = {
                {34, 0, 1, 0, 0, 0},
                {0, -1, 0 ,1, 0, 0},
                {1, 0, -1, 0, 1, 0},
                {0, 1, 0, -1, 0, 1},
                {0, 0, 1, 0, -1, 0},
                {0, 0, 0, 1, 0, -1}
        };

        calculateProbabilitiesFractions(str1);
        calculateProbabilitiesFractions(str2);
        calculateProbabilitiesFractions(str3);

        LinkedHashMap<Character, Double> str1Map = calculateProbabilities(str1);
        LinkedHashMap<Character, Double> str2Map = calculateProbabilities(str2);
        LinkedHashMap<Character, Double> str3Map = calculateProbabilities(str3);


        System.out.println("Entropy for: " + str1 + " " + calculateEntropy(str1Map));
        System.out.println("Entropy for: " + str2 + " " + calculateEntropy(str2Map));
        System.out.println("Entropy for: " + str3 + " " + calculateEntropy(str3Map));
        System.out.println();

        ArithmeticEncoding.arithmeticEncode(str1Map, str1, str1.length());
        ArithmeticEncoding.arithmeticEncode(str2Map, str2, str2.length());
        ArithmeticEncoding.arithmeticEncode(str3Map, str3, str3.length());



        BurrowsWheeler.encode(str3);
        System.out.println();
//        encode(str3);



        /* I(x,y) = I(x-1, y) + I(x-1,y-1) - I(x-1,y-1) */
        ImageEncode.JPEGFunction jpegFunction = new ImageEncode.JPEGFunction() {
            @Override
            public int codingFunction(int[][] matrix, int x, int y) {
                int left = y == 0 ? 0 : matrix[x][y-1];
                int up = x == 0 ? 0 : matrix[x-1][y];

                int upperLeft = 0;
                try {
                    upperLeft = matrix[x-1][y-1];
                } catch (IndexOutOfBoundsException e) {
                    upperLeft = 0;
                }

                return left + up - upperLeft;
            }
        };

        int[][] matrix = ImageEncode.decodeImage(encodedMatrix, jpegFunction);

        int result[][] = ImageEncode.encodeMatrix(matrix, jpegFunction);
        printArray(result, "ENCODED MATRIX:");

        LZW.encode(str2);

        LZ77.encode(str1, 6, 6);

        System.out.println("----- Huffman -----");
        String str4 = "GOGO,BOOMBOOM,OMAN,BANDANA,ALA";

        LinkedHashMap<Character, Double> str4Map = calculateProbabilities(str4);

        System.out.println("Entropy for : " + str4 + ": " + calculateEntropy(str4Map));
        Huffman.encode(str4, str4Map);


    }


    static void printArray(int[][] matrix, String msg) {
        System.out.println(msg + ": ");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }






    public static double calculateEntropy(LinkedHashMap<Character, Double> probabilities) {
        double entropy = 0.0;
        for (double i : probabilities.values()) {
            entropy -= i * (Math.log(i)/Math.log(2));
        }
        return entropy;
    }

    public static LinkedHashMap<Character, Double> calculateProbabilities(String string) throws ArrayIndexOutOfBoundsException{
        System.out.println("Printing probabilities of " + string);
        int[] occurrences = new int[256]; // All are zeros...
        for (int i = 0; i < string.length(); i++) {
            char currentChar = string.charAt(i);
            occurrences[currentChar]++;
        }

        LinkedHashMap<Character, Double> probabilities = new LinkedHashMap<>();
        for (int i = 0; i < occurrences.length; i++) {
            if (occurrences[i] > 0) {
                probabilities.put((char) i, ((double) occurrences[i]/string.length()));
            }
        }

        printMap(probabilities);
        System.out.println();
        return probabilities;
    }

    public static LinkedHashMap<Character, String> calculateProbabilitiesFractions(String string) throws ArrayIndexOutOfBoundsException{
        System.out.println("Printing probabilities of " + string);
        int[] occurrences = new int[256]; // All are zeros...
        for (int i = 0; i < string.length(); i++) {
            char currentChar = string.charAt(i);
            occurrences[currentChar]++;
        }

        LinkedHashMap<Character, String> probabilities = new LinkedHashMap<>();
        for (int i = 0; i < occurrences.length; i++) {
            if (occurrences[i] > 0) {
                probabilities.put((char) i,occurrences[i] + "/" + string.length());
            }
        }

        printMapF(probabilities);
        System.out.println();
        return probabilities;
    }

    public static void printMap(HashMap<Character, Double> map) {
        for (Map.Entry<Character, Double> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    public static void printMapF(HashMap<Character, String> map) {
        for (Map.Entry<Character, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
