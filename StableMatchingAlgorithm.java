import java.util.*;

public class StableMatchingAlgorithm {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        // Input: number of men/women
        System.out.print("Enter number of men (or women): ");
        int n = scanner.nextInt();

         if(n>0) {
             // Generate preference lists
             int[][] menPreferences = generateRandomPreferences(n);
             int[][] womenPreferences = generateRandomPreferences(n);

             // Print preference lists
             printPreferences("Men", menPreferences);
             printPreferences("Women", womenPreferences);

             // Perform the Gale-Shapley Algorithm
             int[] womenPartner = galeShapleyAlgorithm(n, menPreferences, womenPreferences, rand);

             // Print the stable matching result
             printStableMatching(womenPartner);
         }
         else{
             System.out.println("Number Participants Should be Greater than 0");
         }
    }

    // Generates random preference lists for either men or women
    private static int[][] generateRandomPreferences(int n) {
        Random rand = new Random();
        int[][] preferences = new int[n][n];
        for (int i = 0; i < n; i++) {
            List<Integer> prefList = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                prefList.add(j);
            }
            Collections.shuffle(prefList, rand);
            for (int j = 0; j < n; j++) {
                preferences[i][j] = prefList.get(j);
            }
        }
        return preferences;
    }

    // Gale-Shapley Algorithm Implementation
    private static int[] galeShapleyAlgorithm(int n, int[][] menPreferences, int[][] womenPreferences, Random rand) {
        int[] womenPartner = new int[n]; // Stores current partner for each woman
        boolean[] menFree = new boolean[n]; // Array to track free men
        int[] menProposals = new int[n]; // Array to track the number of proposals made by each man
        Arrays.fill(womenPartner, -1); // Initially all women are free
        Arrays.fill(menFree, true); // Initially all men are free

        int freeCount = n; // The number of free men

        while (freeCount > 0) {
            int m;
            do {
                m = rand.nextInt(n); // Select a random free man
            } while (!menFree[m]);

            int w = menPreferences[m][menProposals[m]]; // Get the next woman from his list
            menProposals[m]++; // Increment the proposal count

            if (womenPartner[w] == -1) { // If the woman is free, engage with the man
                womenPartner[w] = m;
                menFree[m] = false;
                freeCount--;
            } else { // If the woman is already engaged
                int currentPartner = womenPartner[w];
                if (preferNewMan(womenPreferences[w], m, currentPartner)) { // Check if she prefers the new man
                    womenPartner[w] = m; // Engage with the new man
                    menFree[m] = false; // The new man is no longer free
                    menFree[currentPartner] = true; // The old partner is now free
                }
            }
        }

        return womenPartner;
    }

    // Helper function to check if a woman prefers the new man m over her current partner m1
    private static boolean preferNewMan(int[] womanPreferences, int m, int currentPartner) {
        for (int preference : womanPreferences) {
            if (preference == m) {
                return true; // Prefers the new man
            }
            if (preference == currentPartner) {
                return false; // Prefers the current partner
            }
        }
        return false;
    }

    // Print the stable matching results
    private static void printStableMatching(int[] womenPartner) {
        System.out.println("\nStable Matching:");
        for (int i = 0; i < womenPartner.length; i++) {
            System.out.println("M" + (womenPartner[i] + 1) + " - W" + (i + 1));
        }
    }

    // Print preference lists for men or women
    private static void printPreferences(String groupName, int[][] preferences) {
        System.out.println("\n" + groupName + "'s Preference Lists:");
        for (int i = 0; i < preferences.length; i++) {
            System.out.print(groupName.charAt(0) + "" + (i + 1) + ": ");
            for (int j = 0; j < preferences[i].length; j++) {
                System.out.print(groupName.charAt(0) == 'M' ? "W" + (preferences[i][j] + 1) : "M" + (preferences[i][j] + 1));
                if (j < preferences[i].length - 1) System.out.print(", ");
            }
            System.out.println();
        }
    }
}