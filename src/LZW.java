import java.util.*;

public class LZW {

    private static boolean verbose = true; // Change this to avoid junk in output stream

    /**
     * @param string The string to be encoded by LZW
     * @return Array on integers containing the numbers from the dictionary
     *
     * */
    public static int[] encode(String string) {
        Dictionary dictionary = new Dictionary(string);

        print("LZW : " + string);

        int result[] = dictionary.getEncodingForString(string);
        print("Encoded String: " + Arrays.toString(result));
        print("Dictionary: ");
        dictionary.printDictionary();


        try {
            decode(result, dictionary.abc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * @param encoded Integer array of numbers from the dictionary (what you got from the encode method)
     * @param abc The array of character of the string... you can pass the string or just
     *            the chars of the string
     *            CAUTION: Passing a wrong abc array will result in a false decoding process
     * */
    public static String decode(int[] encoded, char[] abc) {
        Arrays.sort(abc);


        print("Decoding : " + Arrays.toString(encoded) + " ABC: " + Arrays.toString(abc));

        Dictionary dictionary = new Dictionary(new String(abc)); // Initialize first dict

        LinkedHashMap<Integer, String> dict = dictionary.reversedDictionary;

        StringBuilder stringBuilder = new StringBuilder();
        int counter = dict.size();
        for (int i = 0; i < encoded.length; i++) {

            String decodedSubstring = dict.get(encoded[i]);

            stringBuilder.append(decodedSubstring);

            char nextChar = i < encoded.length-1 ? dict.get(encoded[i+1]).charAt(0) : '?';
            dict.put(counter++, decodedSubstring.concat(nextChar + ""));

        }

        dictionary.printReversedDictionary();

        print("Answer: " + stringBuilder.toString());
        return stringBuilder.toString();
    }



    /*Helper Data structure and methods*/
    private static class Dictionary {

        private LinkedHashMap<String, Integer> dictionary;
        private LinkedHashMap<Integer, String> reversedDictionary;

        private int charsCounter;
        char[] abc;

        public Dictionary(String string) {
            this.dictionary = new LinkedHashMap<>();
            this.reversedDictionary = new LinkedHashMap<>();
            this.charsCounter = 0;

            abc = string.toCharArray();
            Arrays.sort(abc);


            for (int i = 0; i < abc.length; i++) {
                char currentChar = abc[i];
                if (!this.dictionary.containsKey(currentChar + "")) {
                    this.reversedDictionary.put(charsCounter, currentChar + "");
                    this.dictionary.put(currentChar + "", charsCounter++);
                }
            }
        }

        public int[] getEncodingForString(String string) {

            int start = 0;

            List<Integer> encoded = new ArrayList<>();


            int n = start + 1;
            while (n <= string.length()) {
                n = start + 1;
                while (n <= string.length() &&
                        this.dictionary.containsKey(string.substring(start, n))) {
                    n++;
                }

                if (n < string.length()) {
                    this.dictionary.put(string.substring(start, n), charsCounter++);

                }

                encoded.add(this.dictionary.get(string.substring(start, n - 1)));

                start = n - 1;
            }




            int[] result = new int[encoded.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = encoded.get(i);
            }
            return result;
        }

        private void printDictionary() {
            if (verbose) {
                for (Map.Entry<String, Integer> entry : this.dictionary.entrySet()) {
                    System.out.println(entry.getKey() + " :: " + entry.getValue());
                }
                System.out.println();
            }
        }
        private void printReversedDictionary() {
            if (verbose) {
                for (Map.Entry<Integer, String> entry : this.reversedDictionary.entrySet()) {
                    System.out.println(entry.getKey() + " :: " + entry.getValue());
                }
                System.out.println();
            }
        }
    }

    private static void print(String string) {
        if (verbose) {
            System.out.println(string);
        }
    }

}
