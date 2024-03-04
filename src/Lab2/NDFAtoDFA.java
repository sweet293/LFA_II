package Lab2;

import java.util.*;

public class NDFAtoDFA {
    // Function to convert NDFA to DFA
    public static Map<Set<String>, Map<Character, Set<String>>> convertNDFAtoDFA(Map<String, Map<Character, Set<String>>> ndfa, String initialState) {
        Map<Set<String>, Map<Character, Set<String>>> dfa = new HashMap<>();
        Queue<Set<String>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Initial state of DFA
        Set<String> initialSet = epsilonClosure(ndfa, Collections.singleton(initialState));
        queue.add(initialSet);
        visited.add(initialState);
        dfa.put(initialSet, new HashMap<>());

        while (!queue.isEmpty()) {
            Set<String> currentState = queue.poll();

            // Calculate epsilon closure for current state
            Set<String> epsilonClosure = epsilonClosure(ndfa, currentState);

            for (char symbol : getAlphabet(ndfa)) {
                Set<String> newState = new HashSet<>();
                for (String state : epsilonClosure) {
                    if (ndfa.containsKey(state) && ndfa.get(state).containsKey(symbol)) {
                        newState.addAll(ndfa.get(state).get(symbol));
                    }
                }
                Set<String> epsilonClosureNewState = epsilonClosure(ndfa, newState);

                if (!epsilonClosureNewState.isEmpty()) {
                    if (!dfa.containsKey(epsilonClosureNewState)) {
                        queue.add(epsilonClosureNewState);
                        dfa.put(epsilonClosureNewState, new HashMap<>());
                    }
                    dfa.get(currentState).put(symbol, epsilonClosureNewState);
                }
            }
        }

        return dfa;
    }

    // Function to calculate epsilon closure of a set of states
    public static Set<String> epsilonClosure(Map<String, Map<Character, Set<String>>> ndfa, Set<String> states) {
        Set<String> closure = new HashSet<>(states);
        Queue<String> queue = new LinkedList<>(states);

        while (!queue.isEmpty()) {
            String state = queue.poll();
            if (ndfa.containsKey(state) && ndfa.get(state).containsKey('ε')) {
                for (String nextState : ndfa.get(state).get('ε')) {
                    if (!closure.contains(nextState)) {
                        closure.add(nextState);
                        queue.add(nextState);
                    }
                }
            }
        }

        return closure;
    }

    // Function to get the alphabet of the NDFA
    public static Set<Character> getAlphabet(Map<String, Map<Character, Set<String>>> ndfa) {
        Set<Character> alphabet = new HashSet<>();
        for (Map<Character, Set<String>> transitions : ndfa.values()) {
            alphabet.addAll(transitions.keySet());
        }
        alphabet.remove('ε');
        return alphabet;
    }

    public static void main(String[] args) {
        // Define the NDFA transition function
        Map<String, Map<Character, Set<String>>> ndfa = new HashMap<>();
        ndfa.put("q0", Map.of('a', Set.of("q1"), 'ε', Set.of("q2")));
        ndfa.put("q1", Map.of('b', Set.of("q2", "q3")));
        ndfa.put("q2", Map.of('c', Set.of("q3")));
        ndfa.put("q3", Map.of('a', Set.of("q3"), 'b', Set.of("q4")));
        ndfa.put("q4", Collections.emptyMap());

        // Convert NDFA to DFA
        Map<Set<String>, Map<Character, Set<String>>> dfa = convertNDFAtoDFA(ndfa, "q0");

        // Print DFA transition table
        System.out.println("DFA Transition Table:");
        System.out.printf("| %-10s | %-10s | %-10s | %-10s |\n", "State", "a", "b", "c");
        System.out.println("|------------|------------|------------|------------|");
        for (Map.Entry<Set<String>, Map<Character, Set<String>>> entry : dfa.entrySet()) {
            Set<String> state = entry.getKey();
            Map<Character, Set<String>> transitions = entry.getValue();
            System.out.printf("| %-10s ", state);
            for (char symbol : getAlphabet(ndfa)) {
                System.out.printf("| %-10s ", transitions.containsKey(symbol) ? transitions.get(symbol) : "∅");
            }
            System.out.println("|");
        }
    }
}
