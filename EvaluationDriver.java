import java.util.Arrays;

/**
 * Assignment 4 for CS 1410
 * This program evaluates the linear and binary searching, along
 * with comparing performance difference between the selection sort
 * and the built-in java.util.Arrays.sort.
 *
 * @author James Dean Mathias
 */
public class EvaluationDriver {
    static final int MAX_VALUE = 1_000_000;
    static final int MAX_ARRAY_SIZE = 100_000;
    static final int ARRAY_SIZE_START = 20_000;
    static final int ARRAY_SIZE_INCREMENT = 20_000;
    static final int NUMBER_SEARCHES = 50_000;

    public static void main(String[] args) {
        demoLinearSearchUnsorted(MAX_ARRAY_SIZE, ARRAY_SIZE_START, ARRAY_SIZE_INCREMENT, MAX_VALUE, NUMBER_SEARCHES);
        demoLinearSearchSorted(MAX_ARRAY_SIZE, ARRAY_SIZE_START, ARRAY_SIZE_INCREMENT, MAX_VALUE, NUMBER_SEARCHES);
        demoBinarySearchSelectionSort(MAX_ARRAY_SIZE, ARRAY_SIZE_START, ARRAY_SIZE_INCREMENT, MAX_VALUE, NUMBER_SEARCHES);
        demoBinarySearchFastSort(MAX_ARRAY_SIZE, ARRAY_SIZE_START, ARRAY_SIZE_INCREMENT, MAX_VALUE, NUMBER_SEARCHES);
    }

    // If the howMany variable isn't greater than 0, array is null.
    // Else, we make an array the size of howMany. We then input random values with a min of 0 and max of maxValue.
    // We then return the array.
    public static int[] generateNumbers(int howMany, int maxValue) {
        if (howMany <= 0) {
            System.out.println("Array out of bounds");
            return null;
        }
        else {
            int[] array = new int[howMany];
            for (int i = 0; i < howMany; i++) {
                array[i] = (int) (Math.random() * maxValue);
            }
            return array;
        }
    }

    // We assume we have not found the search var in the data.
    // For every value in data, we test to see if it's the search var.
    // Return true if found, else return false.
    public static boolean linearSearch(int[] data, int search) {
        boolean found = false;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == search) {
                found = true;
                break;
            }
        }
        return found;
    }

    // We assign left and right to represent min/max indexes.
    // While left and right don't overlap one another
    // The middle is the average between the two sides
    // If search is found, return true
    // If search is less than middle, move right inward
    // If search is more than middle, move left inward
    // All else fails, return false
    public static boolean binarySearch(int[] data, int search) {
        int left = 0;
        int right = data.length - 1;
        while (left <= right) {
            int mid = (left + right)/2;
            if (data[mid] == search) {
                return true;
            }
            else if (data[mid] > search) {
                right = mid - 1;
            }
            else {
                left = mid + 1;
            }
        }
        return false;
    }

    // We pick index i to represent the known minimum.
    // For every value in data, we take the indexes greater than i, and test to see if it's less than current min
    // If yes, then reassign min
    // Once min is found, assign to i index
    // Continue to narrow down until no indexes above i exist
    public static void selectionSort(int[] data) {
        for (int i = 0; i < (data.length - 1); i++) {
            int min = i;
            for (int j = i + 1; j < data.length; j++) {
                if (data[j] < data[min]) {
                    min = j;
                }
            }
            int temp = data[min];
            data[min] = data[i];
            data[i] = temp;
        }
    }

    // Add the beauty marks (format/results)
    // For every increment, we generate a randomArray
    // For the number of searches, assign a new random number, and find with linear search
    // Add to counter if found, while also recording difference in time between beginning and end..
    // Repeat for every increment, resetting numberFound and totalTime
    public static void demoLinearSearchUnsorted(int maxArraySize, int startArraySize, int incrementArraySize, int maxValue, int numberSearches) {
        System.out.println("--- Linear Search Timing (unsorted) ---");
        int increments = (maxArraySize-startArraySize)/incrementArraySize;

        for (int i = 0; i <= increments; i++) {
            int numberFound = 0;
            int howMany = startArraySize + i * incrementArraySize;
            long totalTime = 0;

            int[] randomArray = generateNumbers(howMany, maxValue);

            for (int j = 0; j <= numberSearches; j++) {
                int randomNumber = (int) (Math.random() * maxValue);

                long searchTime = 0;
                searchTime = System.currentTimeMillis();
                if (linearSearch(randomArray, randomNumber)) {
                    numberFound += 1;
                }
                totalTime += System.currentTimeMillis() - searchTime;
            }
            System.out.printf("%-22s: %d\n", "Number of items", howMany);
            System.out.printf("%-22s: %d\n", "Times value was found" ,numberFound);
            System.out.printf("%-22s: %d ms\n", "Total search time", totalTime);
            System.out.println();
        }
    }

    // Same formula as before, but we calculate the time to sort
    // Include in first for so that it's not an absurb amount of time
    // Add time to sort to overall time
    public static void demoLinearSearchSorted(int maxArraySize, int startArraySize, int incrementArraySize, int maxValue, int numberSearches) {
        System.out.println("--- Linear Search Timing (Selection Sort) ---");
        int increments = (maxArraySize-startArraySize)/incrementArraySize;

        for (int i = 0; i <= increments; i++) {
            int numberFound = 0;
            int howMany = startArraySize + i * incrementArraySize;
            long totalTime = 0;
            long sortTime = 0;

            int[] randomArray = generateNumbers(howMany, maxValue);

            sortTime = System.currentTimeMillis();
            selectionSort(randomArray);
            sortTime = System.currentTimeMillis() - sortTime;

            for (int j = 0; j <= numberSearches; j++) {
                int randomNumber = (int) (Math.random() * maxValue);
                long searchTime = 0;

                searchTime = System.currentTimeMillis();
                if (linearSearch(randomArray, randomNumber)) {
                    numberFound += 1;
                }
                totalTime += System.currentTimeMillis() - searchTime + sortTime;
            }
            System.out.printf("%-22s: %d\n", "Number of items", howMany);
            System.out.printf("%-22s: %d\n", "Times value was found" ,numberFound);
            System.out.printf("%-22s: %d ms\n", "Total search time", totalTime);
            System.out.println();
        }
    }

    // Literally the same layout as demoLinearSearchSorted, only we replace linearSearch with binarySearch
    public static void demoBinarySearchSelectionSort(int maxArraySize, int startArraySize, int incrementArraySize, int maxValue, int numberSearches) {
        System.out.println("--- Binary Search Timing (Selection Sort) ---");
        int increments = (maxArraySize-startArraySize)/incrementArraySize;
        for (int i = 0; i <= increments; i++) {
            int numberFound = 0;
            int howMany = startArraySize + i * incrementArraySize;
            long totalTime = 0;
            long sortTime = 0;

            int[] randomArray = generateNumbers(howMany, maxValue);

            sortTime = System.currentTimeMillis();
            selectionSort(randomArray);
            sortTime = System.currentTimeMillis() - sortTime;

            for (int j = 0; j <= numberSearches; j++) {
                int randomNumber = (int) (Math.random() * maxValue);
                long searchTime = 0;

                searchTime = System.currentTimeMillis();
                if (binarySearch(randomArray, randomNumber)) {
                    numberFound += 1;
                }
                totalTime += System.currentTimeMillis() - searchTime + sortTime;
            }
            System.out.printf("%-22s: %d\n", "Number of items", howMany);
            System.out.printf("%-22s: %d\n", "Times value was found" ,numberFound);
            System.out.printf("%-22s: %d ms\n", "Total search time", totalTime);
            System.out.println();
        }
    }

    // Same as demoBinarySearchSelectionSort, but we replace the selectionSort with builtin java sort.
    public static void demoBinarySearchFastSort(int maxArraySize, int startArraySize, int incrementArraySize, int maxValue, int numberSearches) {
        System.out.println("--- Binary Search Timing (Arrays.sort) ---");
        int increments = (maxArraySize-startArraySize)/incrementArraySize;
        for (int i = 0; i <= increments; i++) {
            int numberFound = 0;
            int howMany = startArraySize + i * incrementArraySize;
            long totalTime = 0;
            long sortTime = 0;
            int[] randomArray = generateNumbers(howMany, maxValue);
            sortTime = System.currentTimeMillis();
            java.util.Arrays.sort(randomArray);
            sortTime = System.currentTimeMillis() - sortTime;
            for (int j = 0; j <= numberSearches; j++) {
                int randomNumber = (int) (Math.random() * maxValue);
                long searchTime = 0;
                searchTime = System.currentTimeMillis();
                if (binarySearch(randomArray, randomNumber)) {
                    numberFound += 1;
                }
                totalTime += System.currentTimeMillis() - searchTime + sortTime;
            }
            System.out.printf("%-22s: %d\n", "Number of items", howMany);
            System.out.printf("%-22s: %d\n", "Times value was found" ,numberFound);
            System.out.printf("%-22s: %d ms\n", "Total search time", totalTime);
            System.out.println();
        }
    }
}

