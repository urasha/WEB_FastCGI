public class HitChecker {

    public static boolean checkHit(double r, double x, double y) {
        boolean hitRect = checkRectangle(r, x, y);
        boolean hitCircle = checkCircle(r, x, y);
        boolean hitTriangle = checkTriangle(r, x, y);

        return hitRect || hitTriangle || hitCircle;
    }

    private static boolean checkRectangle(double r, double x, double y) {
        return (x >= -r && x <= 0) && (y <= r && y >= 0);
    }

    private static boolean checkCircle(double r, double x, double y) {
        return ((x * x + y * y) <= r * r) && (x <= 0 && y <= 0);
    }

    private static boolean checkTriangle(double r, double x, double y) {
        if (!(x >= 0 && x <= (r / 2)) || !(y <= 0 && y >= (-r / 2))) {
            return false;
        }

        double xLength = (r / 2) - x;
        double tan = y / xLength;

        return tan <= 1;
    }
}
