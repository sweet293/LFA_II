package Lab4;
import java.util.Random;


public class Main {
    public static void main(String[] args) {
        String rule1 = "M?N{" + "2}" + "(O|P){" + "3}" +"Q*R+";
        System.out.println("Final string: " + generateString(rule1));

        String rule2 = "(X|Y|Z){" + "3}" + "8+(9|0)";
        System.out.println("Final string: " + generateString(rule2));

        String rule3 = "(H|i)(J|K)L*N?";
        System.out.println("Final string: " + generateString(rule3));
    }

    public static String generateString(String rule) {
        StringBuilder string = new StringBuilder();
        int i = 0;
        Random random = new Random();

        while (i < rule.length()) {
            if ((rule.charAt(i) == '(' && rule.indexOf(")", i) == rule.length() - 1) ||
                    (rule.charAt(i) == '(' && rule.charAt(rule.indexOf(")", i) + 1) != '*' &&
                            rule.charAt(rule.indexOf(")", i) + 1) != '+' &&
                            rule.charAt(rule.indexOf(")", i) + 1) != '?' &&
                            rule.charAt(rule.indexOf(")", i) + 1) != '{')) {
                char c = choice(options(rule.substring(i + 1, rule.indexOf(")", i))));
                string.append(c);
                i = rule.indexOf(")", i);
                System.out.println("Just one occurrence from options: Adding " + c + " to string => " + string);
            } else if (rule.charAt(i) == '(' && rule.charAt(rule.indexOf(")", i) + 1) == '+') {
                char c = rule.charAt(i - 1);
                int times = random.nextInt(5) + 1;
                for (int j = 0; j < times; j++) {
                    string.append(c);
                    System.out.println("One or more occurrences from options: Adding " + c + " to string => " + string);
                }
                i = rule.indexOf(")", i) + 1;
            } else if (rule.charAt(i) == '(' && rule.charAt(rule.indexOf(")", i) + 1) == '*') {
                char c = rule.charAt(i - 1);
                int times = random.nextInt(6);
                for (int j = 0; j < times; j++) {
                    string.append(c);
                    System.out.println("Zero or more occurrences from options: Adding " + c + " to string => " + string);
                }
                i = rule.indexOf(")", i) + 1;
            } else if (rule.charAt(i) == '(' && rule.charAt(rule.indexOf(")", i) + 1) == '{') {
                char c = rule.charAt(i - 1);
                int times = Integer.parseInt(rule.substring(rule.indexOf("{", i) + 1, rule.indexOf("}", i)));
                for (int j = 0; j < times; j++) {
                    string.append(c);
                    System.out.println("Fixed occurrences from options: Adding " + c + " to string => " + string);
                }
                i = rule.indexOf("}", i) + 1;
            } else if (rule.charAt(i) == '(' && rule.charAt(rule.indexOf(")", i) + 1) == '?') {
                if (random.nextInt(2) == 1) {
                    char c = choice(options(rule.substring(i + 1, rule.indexOf(")", i))));
                    string.append(c);
                    System.out.println("Zero or one occurrence from options: Adding " + c + " to string => " + string);
                }
                i = rule.indexOf(")", i) + 1;
            } else if (i < rule.length() - 2 && rule.charAt(i + 1) == '?') {
                if (random.nextInt(2) == 1) {
                    string.append(rule.charAt(i));
                    System.out.println("Zero or one occurrence: Adding " + rule.charAt(i) + " to string => " + string);
                }
                i += 2;
            } else if (Character.isDigit(rule.charAt(i)) && rule.charAt(i + 1) == '{') {
                int times = Character.getNumericValue(rule.charAt(i));
                char c = rule.charAt(i - 1);
                for (int j = 0; j < times; j++) {
                    string.append(c);
                    System.out.println("Repeat " + c + " " + times + " times: Adding " + c + " to string => " + string);
                }
                i += 2;
            } else if (i > 0 && rule.charAt(i - 1) == '{') {
                i++;
                continue;
            } else {
                string.append(rule.charAt(i));
                System.out.println("Adding " + rule.charAt(i) + " to string => " + string);
                i++;
            }
        }
        return string.toString();
    }

    public static char choice(String options) {
        Random random = new Random();
        int index = random.nextInt(options.length());
        return options.charAt(index);
    }

    public static String[] options(String sequence) {
        return sequence.split("\\|");
    }
}
