import java.util.*;

/**
 * Created by DELL on 02/07/2017.
 */
public class Huffman {

    public static void encode(String string, LinkedHashMap<Character, Double> probabilities) {

        // Generate the Heap as a Priority Queue ADT in java
        List<Entity> characters = createSortedEntities(probabilities);
        PriorityQueue<Entity> heap = new PriorityQueue<>(characters);


        // Create Huffman tree
        while (heap.size() > 2) {
            Entity temp1 = heap.poll();
            Entity temp2 = heap.poll();

            Entity tempFather = new Entity(temp1,temp2, temp1.probability + temp2.probability);
            heap.add(tempFather);
        }
        // Size of the heap is 2
        Entity root = new Entity(heap.poll(), heap.poll());





        // Done creating the Huffman Tree

        // Create a character -> encoding map for the characters of the string
        LinkedHashMap<Character, String> encodingMap = new LinkedHashMap<>();

        fillEncodingsList(root, encodingMap, new String());




        /*DEBUG*/
        for (Map.Entry<Character, String> entry : encodingMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }


        double cost = calculateCost(encodingMap, probabilities);
        System.out.println("Cost: " + cost);

        StringBuilder encodedString = new StringBuilder((int) (string.length() * cost));
        for (int i = 0; i < string.length(); i++) {
            char currentChar = string.charAt(i);

            String encodingForChar = encodingMap.get(currentChar);

            encodedString.append(encodingForChar);
            encodedString.append(' ');

        }
        String encoded = encodedString.toString();

        System.out.println("Encoded: " + encoded);

    }



    private static void fillEncodingsList(Entity root,
                                          LinkedHashMap<Character, String> encodingMap,
                                          String encoding) {
        if (root != null) {

            if (root.isDataNode()) {
                // Data node.. contains a character
                // Add it
                encodingMap.put(root.value, encoding);
            }

            fillEncodingsList(root.leftChild, encodingMap, encoding.concat("0"));
            fillEncodingsList(root.rightChild, encodingMap, encoding.concat("1"));

        }
    }

    private static List<Entity> createSortedEntities(LinkedHashMap<Character, Double> probabilities) {
        List<Entity> entities = new ArrayList<>(probabilities.size());

        for (Map.Entry<Character, Double> entry : probabilities.entrySet()) {

            Entity entity = new Entity(entry.getKey(), entry.getValue(), null);
            entities.add(entity);
        }

        Collections.sort(entities);

        return entities;
    }

    private static double calculateCost(LinkedHashMap<Character,String> encodingMap,
                                        LinkedHashMap<Character, Double> probabilities) {

        double cost = 0.0;
        for (Map.Entry<Character, String> entry : encodingMap.entrySet()) {
            cost += (entry.getValue().length() * probabilities.get(entry.getKey()));
        }
        return cost;

    }

    private static class Entity implements Comparable<Entity> {

        private char value;
        private double probability;
        private Entity father;

        /* ONLY for the parent nodes */
        private Entity leftChild;
        private Entity rightChild;
        public Entity(Entity leftChild, Entity rightChild) {
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        public Entity(Entity leftChild, Entity rightChild, double probability) {
            this.probability = probability;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        /* Child node... has data but no children */
        public Entity(char value, double probability, Entity father) {
            this.value = value;
            this.probability = probability;
            this.father = father;
        }



        /**
         * If this nodes holds data (i.e - character and probability)
         * */
        public boolean isDataNode() {
            return this.rightChild == null && this.leftChild == null;
        }

        /* To support comparing items */
        @Override
        public int compareTo(Entity o) {
            if (this.probability > o.probability) {
                return 1;
            } else if (this.probability < o.probability){
                return -1;
            } else {
                return 0;
            }
        }
    }


}
