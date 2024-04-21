package Lab5;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Initializing a HashMap to store rules
        Map<String, List<String>> rules = new HashMap<>();

        // Initializing lists to store vocabulary and letters of the alphabet
        List<String> voc = new ArrayList<>();
        List<String> letters = new ArrayList<>();

        // Generating letters from 'A' to 'Z' and adding them to the list
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            letters.add(String.valueOf(ch));
        }
        // Removing letter 'E' from the list
        letters.remove("E");

        // Prompting user for the number of rules, ensuring it's at least 2
        int N;
        while (true) {
            System.out.print("Give number of rules: ");
            try {
                N = Integer.parseInt(scanner.nextLine());
                if (N <= 2) {
                    System.out.println("N must be a number >= 2!");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not an integer!");
            }
        }

        // Prompting user for the initial state, ensuring it's a single character
        String S;
        while (true) {
            System.out.print("Give initial state: ");
            S = scanner.nextLine();
            if (S.matches("[a-zA-Z]") && S.length() == 1) {
                break;
            } else {
                System.out.println("Initial state must be a single character!");
            }
        }

        // Getting rules from the user
        System.out.println("+------------------------------------------------------+");
        System.out.println("|Give rules in the form A B (space-delimited), for A->B|");
        System.out.println("|or A BCD, if more than one states in the right part   |");
        System.out.println("|(without spaces between right part members).          |");
        System.out.println("+------------------------------------------------------+");

        for (int i = 0; i < N; i++) {
            System.out.print("Rule #" + (i + 1) + ": ");
            String[] input = scanner.nextLine().split(" ");
            String fr = input[0]; // Source state
            String to = input[1]; // Destination state(s)

            // Updating vocabulary and letters based on the source and destination states
            for (char l : fr.toCharArray()) {
                String letter = String.valueOf(l);
                if (!letter.equals("e") && !letter.equals("E") && !voc.contains(letter)) {
                    voc.add(letter);
                }
                if (letters.contains(letter)) {
                    letters.remove(letter);
                }
            }
            for (char l : to.toCharArray()) {
                String letter = String.valueOf(l);
                if (!letter.equals("e") && !letter.equals("E") && !voc.contains(letter)) {
                    voc.add(letter);
                }
                if (letters.contains(letter)) {
                    letters.remove(letter);
                }
            }

            // Adding the rule to the rules map
            if (!rules.containsKey(fr)) {
                rules.put(fr, new ArrayList<>());
            }
            rules.get(fr).add(to);
        }

        // Displaying rules after removing large rules
        System.out.println("\nRules after large rules removal");
        voc = large(rules, letters, voc);
        printRules(rules);

        // Displaying rules after removing empty rules
        System.out.println("\nRules after empty rules removal");
        rules = empty(rules, voc);
        printRules(rules);

        // Displaying rules after removing short rules
        System.out.println("\nRules after short rules removal");
        Map<String, List<String>> D = short(rules, voc);
        printRules(rules);

        // Displaying final rules after processing
        System.out.println("\nFinal rules");
        rules = finalRules(rules, D, S);
        printRules(rules);
    }

    // Method to remove large rules and update vocabulary
    static List<String> large(Map<String, List<String>> rules, List<String> letters, List<String> voc) {
        Map<String, List<String>> newRules = new HashMap<>(rules);
        // Iterating over each rule
        for (String key : newRules.keySet()) {
            List<String> values = new ArrayList<>(newRules.get(key));
            // Checking for rules longer than 2 characters
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i).length() > 2) {
                    for (int j = 0; j < values.get(i).length() - 2; j++) {
                        if (j == 0) {
                            // Replacing the rule with a shorter one
                            String newKey = letters.get(0);
                            rules.get(key).set(i, rules.get(key).get(i).charAt(0) + newKey);
                        } else {
                            // Updating vocabulary
                            voc.add(letters.get(0));
                        }
                    }
                }
            }
        }
        return voc;
    }

    // Method to remove empty rules
    static Map<String, List<String>> empty(Map<String, List<String>> rules, List<String> voc) {
        List<String> eList = new ArrayList<>();
        Map<String, List<String>> newRules = new HashMap<>(rules);
        for (String key : newRules.keySet()) {
            List<String> values = new ArrayList<>(newRules.get(key));
            for (int i = 0; i < values.size(); i++) {
                // Removing empty rules
                if (values.get(i).equals("e") || values.get(i).equals("E")) {
                    if (!eList.contains(key)) {
                        eList.add(key);
                        rules.get(key).remove(values.get(i));
                    }
                }
                // Removing keys with no values
                if (rules.get(key).isEmpty()) {
                    if (!rules.containsKey(key)) {
                        voc.remove(key);
                    }
                    rules.remove(key);
                }
            }
            // Creating new rules based on empty states
            for (String key2 : newRules.keySet()) {
                List<String> values2 = new ArrayList<>(newRules.get(key2));
                for (int i = 0; i < values2.size(); i++) {
                    if (values2.get(i).length() == 2) {
                        if (eList.contains(String.valueOf(values2.get(i).charAt(0))) && !key.equals(String.valueOf(values2.get(i).charAt(1)))) {
                            rules.putIfAbsent(key2, new ArrayList<>());
                            rules.get(key2).add(String.valueOf(values2.get(i).charAt(1)));
                        }
                        if (eList.contains(String.valueOf(values2.get(i).charAt(1))) && !key.equals(String.valueOf(values2.get(i).charAt(0))) && values2.get(i).charAt(0) != values2.get(i).charAt(1)) {
                            rules.putIfAbsent(key2, new ArrayList<>());
                            rules.get(key2).add(String.valueOf(values2.get(i).charAt(0)));
                        }
                    }
                }
            }
        }
        return rules;
    }

    // Method to remove short rules
    static Map<String, List<String>> short(Map<String, List<String>> rules, List<String> voc) {
        Map<String, List<String>> D = new HashMap<>();
        // Creating a map for each symbol in the vocabulary
        for (String v : voc) {
            D.put(v, new ArrayList<>(Collections.singletonList(v)));
        }
        // Iterating over rules to find short ones
        for (String letter : voc) {
            for (String key : rules.keySet()) {
                if (D.containsKey(letter)) {
                    List<String> values = rules.get(key);
                    for (int i = 0; i < values.size(); i++) {
                        if (values.get(i).length() == 1 && !D.get(letter).contains(values.get(i))) {
                            D.putIfAbsent(letter, new ArrayList<>());
                            D.get(letter).add(values.get(i));
                        }
                    }
                }
            }
        }
        // Updating rules based on short rules
        rules = short1(rules, D);
        return rules;
    }

    // Method to further process short rules
    static Map<String, List<String>> short1(Map<String, List<String>> rules, Map<String, List<String>> D) {
        Map<String, List<String>> newRules = new HashMap<>(rules);
        // Iterating over rules
        for (String key : newRules.keySet()) {
            List<String> values = new ArrayList<>(newRules.get(key));
            for (int i = 0; i < values.size(); i++) {
                // Removing short rules
                if (values.get(i).length() == 1) {
                    rules.get(key).remove(values.get(i));
                }
                // Removing keys with no values
                if (rules.get(key).isEmpty()) {
                    rules.remove(key);
                }
            }
        }
        // Creating new rules based on short rules
        for (String key : rules.keySet()) {
            List<String> values = new ArrayList<>(rules.get(key));
            for (int i = 0; i < values.size(); i++) {
                for (String j : D.get(String.valueOf(values.get(i).charAt(0)))) {
                    for (String k : D.get(String.valueOf(values.get(i).charAt(1)))) {
                        if (!values.contains(j + k)) {
                            rules.putIfAbsent(key, new ArrayList<>());
                            rules.get(key).add(j + k);
                        }
                    }
                }
            }
        }
        return rules;
    }

    // Method to finalize rules
    static Map<String, List<String>> finalRules(Map<String, List<String>> rules, Map<String, List<String>> D, String S) {
        // Checking for final rules
        for (String letter : D.get(S)) {
            if (rules.get(S).isEmpty() && rules.get(letter).isEmpty()) {
                for (String v : rules.get(letter)) {
                    if (!rules.get(S).contains(v)) {
                        rules.putIfAbsent(S, new ArrayList<>());
                        rules.get(S).add(v);
                    }
                }
            }
        }
        return rules;
    }

    // Method to print rules
    static void printRules(Map<String, List<String>> rules) {
        for (String key : rules.keySet()) {
            for (String value : rules.get(key)) {
                System.out.println(key + "->" + value);
            }
        }
    }
}