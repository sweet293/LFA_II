package Lab1;

import Lab1.FiniteAutomaton;

import java.util.*;

public class Grammar {
    private Set<String> VN;
    private Set<String> VT;
    private Map<String, List<String>> productions;

    public Grammar(Set<String> VN, Set<String> VT, Map<String, List<String>> productions) {
        this.VN = VN;
        this.VT = VT;
        this.productions = productions;
    }

    public List<String> generateStrings(String startSymbol, int numStrings) {
        Set<String> Strings = new HashSet<>();

        while (Strings.size() < numStrings) {
            StringBuilder sb = new StringBuilder();
            generateString(startSymbol, sb);
            Strings.add(sb.toString());
        }
        return new ArrayList<>(Strings);
    }


    private void generateString(String symbol, StringBuilder sb) {
        if (!productions.containsKey(symbol)) {
            sb.append(symbol);
            return;
        }

        List<String> choices = productions.get(symbol);
        String choice = choices.get(new Random().nextInt(choices.size()));
        for (char c : choice.toCharArray()) {
            if (Character.isUpperCase(c)) {
                generateString(String.valueOf(c), sb);
            } else {
                sb.append(c);
            }
        }
    }

    public FiniteAutomaton toFiniteAutomaton() {
        Set<String> states = VN;
        Set<String> alphabet = VT;
        Map<String, Map<String, String>> transitions = new HashMap<>();
        transitions.put("S", Collections.singletonMap("a", "S"));
        transitions.put("S", Collections.singletonMap("b", "S"));
        transitions.put("S", Collections.singletonMap("c", "R"));
        transitions.put("S", Collections.singletonMap("d", "L"));
        transitions.put("R", Collections.singletonMap("d", "L"));
        transitions.put("R", Collections.singletonMap("e", ""));
        transitions.put("L", Collections.singletonMap("d", ""));
        transitions.put("L", Collections.singletonMap("f", "L"));
        transitions.put("L", Collections.singletonMap("e", "L"));
        FiniteAutomaton finiteAutomaton = new FiniteAutomaton(states, alphabet, "S", new HashSet<>(Collections.singletonList("S")), transitions);

        return finiteAutomaton;
    }

    public String classifyGrammarType() {
        // Check if it's a regular grammar
        if (isRegularGrammar()) {
            return "Type 3 (Regular Grammar)";
        }

        // Check if it's a context-free grammar
        if (isContextFreeGrammar()) {
            return "Type 2 (Context-Free Grammar)";
        }

        // Check if it's a context-sensitive grammar
        if (isContextSensitiveGrammar()) {
            return "Type 1 (Context-Sensitive Grammar)";
        }

        // If none of the above, it's an unrestricted grammar (Type 0)
        return "Type 0 (Unrestricted Grammar)";
    }

    private boolean isRegularGrammar() {
        // Regular grammars have only productions of the form A -> a or A -> aB
        for (String nonTerminal : productions.keySet()) {
            for (String production : productions.get(nonTerminal)) {
                // If there's a production that violates regular grammar rules, return false
                if (!(production.length() == 1 || (production.length() == 2 && Character.isLowerCase(production.charAt(0))))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isContextFreeGrammar() {
        // Context-free grammars have productions of the form A -> α, where α is any string
        for (String nonTerminal : productions.keySet()) {
            for (String production : productions.get(nonTerminal)) {
                // If there's a production that violates context-free grammar rules, return false
                if (production.length() == 0 || !Character.isUpperCase(production.charAt(0))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isContextSensitiveGrammar() {
        // Context-sensitive grammars have productions of the form αAβ -> αγβ, where A is a non-terminal and α, β, γ are strings
        for (String nonTerminal : productions.keySet()) {
            for (String production : productions.get(nonTerminal)) {
                // If there's a production that violates context-sensitive grammar rules, return false
                if (production.length() < 3 || !Character.isUpperCase(production.charAt(1))) {
                    return false;
                }
            }
        }
        return true;
    }

}