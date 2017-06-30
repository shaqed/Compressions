/**
 * Created by DELL on 20/06/2017.
 */
public class ImageEncode {
    public interface JPEGFunction {
        int codingFunction(int[][] matrix, int x, int y);
    }


    /**
     * @param regularMatrix Your image represented by a matrix
     * @param jpegFunction The function to be coded by this
     *
     * @return The encoded matrix
     * */
    public static int[][] encodeMatrix(int[][] regularMatrix, JPEGFunction jpegFunction) {
        int[][] encodedMatrix = new int[regularMatrix.length][regularMatrix[0].length];
        for (int x = 0; x < regularMatrix.length; x++) {
            for (int y = 0; y < regularMatrix[x].length; y++) {

                encodedMatrix[x][y] = regularMatrix[x][y] - jpegFunction.codingFunction(regularMatrix, x, y);

            }
        }
        return encodedMatrix;
    }

    /**
     * @param encodedMatrix ENCODED MATRIX !! the matrix with encoded value AFTER being encoded
     * @param jpegFunction Interface the implements the SAME FUNCTION THAT WAS USED TO ENCODE THIS MATRIX!
     *
     * @return The decoded matrix
     * */
    public static int[][] decodeImage(int[][] encodedMatrix, JPEGFunction jpegFunction) {
        int[][] decodeMatrix = new int[encodedMatrix.length][encodedMatrix[0].length];
        for (int x = 0; x < encodedMatrix.length; x++) {
            for (int y = 0; y < encodedMatrix[x].length; y++) {
                int encodedValue = encodedMatrix[x][y];
                int predict = encodedValue + jpegFunction.codingFunction(decodeMatrix, x, y);
                decodeMatrix[x][y] = predict;
            }
        }
        printArray(decodeMatrix, "Decoded");
        return decodeMatrix;
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
}
