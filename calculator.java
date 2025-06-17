import java.util.*;
public class calculator {
    public static int getPrecedence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0;
        };
    }

    public static boolean isOperator(String token) {
        return token.matches("[+\\-*/]");
    }

    public static boolean isNumber(String token) {
        return token.matches("-?\\d+(\\.\\d+)?");
    }

    public static List<String> infixToPostfix(String expr) {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(expr, "+-*/() ", true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty()) continue;

            if (isNumber(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && isOperator(stack.peek())
                        && getPrecedence(token) <= getPrecedence(stack.peek())) {
                    output.add(stack.pop());
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop();
                } else {
                    System.out.println("Error: Mismatched parentheses");
                    return null;
                }
            } else {
                System.out.println("Invalid token: " + token);
                return null;
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek().equals("(")) {
                System.out.println("Error: Mismatched parentheses");
                return null;
            }
            output.add(stack.pop());
        }

        return output;
    }

    public static double evaluatePostfix(List<String> postfix, Collection<Number> numberStore) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (isNumber(token)) {
                double val = Double.parseDouble(token);
                stack.push(val);
                numberStore.add((val == (int) val) ? (int) val : val);
            } else if (isOperator(token)) {
                double b = stack.pop();
                double a = stack.pop();
                double res = switch (token) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> {
                        if (b == 0) throw new ArithmeticException("Division by zero");
                        yield a / b;
                    }
                    default -> throw new IllegalArgumentException("Unknown operator: " + token);
                };
                stack.push(res);
            }
        }

        if (stack.size() != 1) throw new IllegalStateException("Invalid postfix expression");
        return stack.pop();
    }

    public static double evaluatePostfixWithSteps(List<String> postfix, Queue<Number> numberStore) {
        Stack<Double> stack = new Stack<>();
        int step = 1;

        for (String token : postfix) {
            System.out.print("Step " + step++ + ": ");
            if (isNumber(token)) {
                double val = Double.parseDouble(token);
                stack.push(val);
                numberStore.add((val == (int) val) ? (int) val : val);
                System.out.println("Push " + val);
            } else if (isOperator(token)) {
                double b = stack.pop();
                double a = stack.pop();
                double res = switch (token) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> {
                        if (b == 0) throw new ArithmeticException("Division by zero");
                        yield a / b;
                    }
                    default -> throw new IllegalArgumentException("Unknown operator: " + token);
                };
                stack.push(res);
                System.out.println("Apply " + token + ": " + a + " " + token + " " + b + " = " + res);
            }
        }

        if (stack.size() != 1) throw new IllegalStateException("Invalid postfix expression");
        return stack.pop();
    }

    public static void analyzeNumbers(Collection<Number> numbers) {
        System.out.println("\n--- Analysis ---");
        System.out.println("Entered values: " + numbers);

        List<Number> sorted = new ArrayList<>(numbers);
        sorted.sort(Comparator.comparingDouble(Number::doubleValue));
        System.out.println("Sorted: " + sorted);

        Set<Double> seen = new HashSet<>();
        List<Number> uniqueSorted = new ArrayList<>();
        for (Number n : sorted) {
            double val = n.doubleValue();
            if (seen.add(val)) {
                uniqueSorted.add((val == (int) val) ? (int) val : val);
            }
        }
        System.out.println("Sorted (no duplicates): " + uniqueSorted);

        List<Number> even = new ArrayList<>();
        List<Number> odd = new ArrayList<>();
        for (Number n : uniqueSorted) {
            if (n instanceof Integer && (int) n % 2 == 0) even.add(n);
            else odd.add(n);
        }
        System.out.println("Even numbers: " + even);
        System.out.println("Odd numbers: " + odd);
    }

    public static void arrayListMode(Scanner sc) {
        List<Number> numbers = new ArrayList<>();
        System.out.print("Enter expression: ");
        String expr = sc.nextLine();
        List<String> postfix = infixToPostfix(expr);
        if (postfix == null) return;

        try {
            double result = evaluatePostfix(postfix, numbers);
            System.out.println("Result: " + result);
            analyzeNumbers(numbers);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void linkedListMode(Scanner sc) {
        List<Number> numbers = new LinkedList<>();
        System.out.print("Enter expression: ");
        String expr = sc.nextLine();
        List<String> postfix = infixToPostfix(expr);
        if (postfix == null) return;

        try {
            double result = evaluatePostfix(postfix, numbers);
            System.out.println("Result: " + result);
            analyzeNumbers(numbers);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void queueMode(Scanner sc) {
        System.out.print("Enter queue size: ");
        int maxSize = Integer.parseInt(sc.nextLine());
        Queue<Number> numbers = new LinkedList<>();

        System.out.print("Enter expression: ");
        String expr = sc.nextLine();
        List<String> postfix = infixToPostfix(expr);
        if (postfix == null) return;

        long operandCount = postfix.stream().filter(calculator::isNumber).count();
        if (operandCount > maxSize) {
            System.out.println("Error: Queue size exceeded. Expression contains " + operandCount + " operands, but size limit is " + maxSize);
            return;
        }

        try {
            double result = evaluatePostfixWithSteps(postfix, numbers);
            System.out.println("Final Result: " + result);
            analyzeNumbers(numbers);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Calculator Menu ===");
            System.out.println("1. Use ArrayList");
            System.out.println("2. Use LinkedList");
            System.out.println("3. Use Queue");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> arrayListMode(sc);
                case "2" -> linkedListMode(sc);
                case "3" -> queueMode(sc);
                case "4" -> {
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }

            System.out.print("\nDo you want to continue? (yes/no): ");
            String again = sc.nextLine().trim().toLowerCase();
            if (!again.equals("yes")) {
                System.out.println("Thanks for using the calculator!");
                break;
            }
        }
    }
}
