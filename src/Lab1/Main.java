package Lab1;

import Lab1.FiniteAutomaton;
import Lab1.Grammar;

import java.util.*;

public class Main {
    public static void main (String[] args) {
        Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "R", "L"));
        Set<String> terminals = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        Map<String, List<String>> productions = new HashMap<>();
        productions.put("S", Arrays.asList("aS", "bS", "cR", "dL"));
        productions.put("R", Arrays.asList("dL", "e"));
        productions.put("L", Arrays.asList("fL", "eL", "d"));
        Grammar grammar = new Grammar(nonTerminals, terminals, productions);

        List<String> strings = grammar.generateStrings("S", 5);
        FiniteAutomaton finiteAutomaton = grammar.toFiniteAutomaton();

        for (String str : strings) {
            System.out.println(str + ": " + finiteAutomaton.validate(str));
        }

    }
}
