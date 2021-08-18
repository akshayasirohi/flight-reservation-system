public class MathematicalOperation {

    public static void main(String[] args) {
        System.out.println(mathFunction(2, 2, 4));
        System.out.println(mathFunction(3, -3, -9));
        System.out.println(mathFunction(1, 2, -1));
        System.out.println(mathFunction(3, 3, 1));
        System.out.println(mathFunction(7, 1, 11));
    }

    public static String mathFunction(int one, int two, int three) {
        String operators = "";
        if (one + two == three)
            operators += "+";
        if (one - two == three)
            operators += "-";
        if (one * two == three)
            operators += "*";
        if (one / two == three)
            operators += "/";
        return operators.length() == 0 ? "None" : operators;
    }
}
