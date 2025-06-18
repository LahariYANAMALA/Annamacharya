public class Armstrong {
    public static boolean isArmstrong(int n) {
        int sum = 0, temp = n;
        while (temp > 0) {
            int d = temp % 10;
            sum += d * d * d;
            temp /= 10;
        }
        return sum == n;
    }

    public static void main(String[] args) {
        int num = 153;
        System.out.println(num + " is Armstrong? " + isArmstrong(num));
    }
}
