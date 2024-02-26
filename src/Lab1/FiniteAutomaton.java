package Lab1;

import java.util.*;

public class FiniteAutomaton {
    private Set<String> states;
    private static Set<String> alphabet;
    private static String startState;
    private static Set<String> finalStates;
    private static Map<String, Map<String, String>> transitions;

    public FiniteAutomaton(Set<String> states, Set<String> alphabet, String startState, Set<String> finalStates, Map<String, Map<String, String>> transitions) {
        this.states = states;
        this.alphabet = alphabet;
        this.startState = startState;
        this.finalStates = finalStates;
        this.transitions = transitions;
    }

    public static boolean validate(String word) {
        String currentState = startState;

        for (char symbol : word.toCharArray()) {
            String symbolStr = String.valueOf(symbol);
            if (!alphabet.contains(symbolStr)) {
                return false;
            }
            if (transitions.containsKey(currentState) && transitions.get(currentState).containsKey(symbolStr)) {
                currentState = transitions.get(currentState).get(symbolStr);
            } else {
                return true;
            }
        }
        return finalStates.contains(currentState);
    }
}