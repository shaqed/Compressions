import java.util.ArrayList;
import java.util.List;

public class LZ77 {

    /**
     * Encodes a string using the LZ77 Algorithm
     *
     * @param string The string to be encoded
     * @param searchBufferSize Size of the search buffer. AKA S
     * @param lookAheadBufferSize Size of the lookahead buffer. AKA T
     *
     * */
    public static List<Tuple> encode(String string, int searchBufferSize, int lookAheadBufferSize) {

        System.out.println("LZ77 on: " + string +" S: " + searchBufferSize + " T: " + lookAheadBufferSize);

        List<Tuple> tuples = new ArrayList<>();

        int pointer = 0;
        while (pointer < string.length()) {

            char currentChar = string.charAt(pointer);

            int searchPointer = pointer;
            int bestLength = 0;
            for (int i = (pointer-searchBufferSize) < 0 ? 0 : (pointer-searchBufferSize);
                 i < pointer && i >= 0;
                 i++) {
                // Look for a matching character


                if (string.charAt(i) == currentChar) {

                    int lengthOfThatOffset =lookForMatch(string,i, pointer);

                    if (lengthOfThatOffset > bestLength) {
                        searchPointer = i;
                        bestLength = lengthOfThatOffset;
                    }

                }
            }
            int offset = pointer - searchPointer;
            int length = bestLength;
            char nextChar = '\0';
            if (pointer + length < string.length()) {
                nextChar = string.charAt(pointer+length);
            }
            Tuple tuple = new Tuple(offset, length, nextChar);

            System.out.println(tuple);

            tuples.add(tuple);

            pointer = bestLength +pointer + 1;
        }


        System.out.println("DECODING:");

        decode(tuples);

        return tuples;
    }



    /**
     * Decodes a list of tuples to the string they form
     * @param tuples The array you got from encoding you string
     * @return The decoded string
     * */
    public static String decode(List<Tuple> tuples) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < tuples.size(); i++) {
            Tuple currentTuple = tuples.get(i);


            if (currentTuple.length > 0) {

                int currentIndex = stringBuilder.length();
                int remainingLength = currentTuple.length;
                int startOffset = currentTuple.offset;
                while (remainingLength > 0) {
                    int toPrint = currentIndex - startOffset;
                    stringBuilder.append(stringBuilder.charAt(toPrint));
                    remainingLength--;
                    currentIndex++;
                }
                // Done.. now print the last char
                stringBuilder.append(currentTuple.endChar);

            } else {
                stringBuilder.append(currentTuple.endChar);
            }

        }

        System.out.println("Decoded: " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    private static int lookForMatch(String string, int searchPointer, int pointer) {
        int offset = pointer - searchPointer;

        if (offset > 0) {
            int lengthOfMatch = 0;
            try {
                while (string.charAt(pointer) == string.charAt(searchPointer)) {
                    lengthOfMatch++;
                    pointer++;
                    if (searchPointer < string.length()-1) {
                        searchPointer++;
                    }
                }
            } catch (IndexOutOfBoundsException e) {

            }

            return lengthOfMatch;
        } else {
            return 0;
        }
    }

    public static class Tuple{
        private int offset;
        private int length;
        private char endChar;

        public Tuple(int offset, int length, char endChar) {
            this.offset = offset;
            this.length = length;
            this.endChar = endChar;
        }

        @Override
        public String toString() {
            return "<" + this.offset + ", " + length + ", " + endChar + ">";
        }
    }
}
