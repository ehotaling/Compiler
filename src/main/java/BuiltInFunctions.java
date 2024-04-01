public class BuiltInFunctions {

    public static int RANDOM() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    public static String LEFT$(String str, int n) {
        return str.substring(0, Math.min(n, str.length()));
    }

    public static String RIGHT$(String str, int n) {
        return str.substring(Math.max(0, str.length() - n));
    }

    public static String MID$(String str, int start, int count) {
        return str.substring(start, Math.min(start + count, str.length()));
    }

    public static String NUM$(Number num) {
        return num.toString();
    }

    public static int VAL(String str) {
        return Integer.parseInt(str);
    }

    public static float VAL$(String str) {
        return Float.parseFloat(str);
    }
}