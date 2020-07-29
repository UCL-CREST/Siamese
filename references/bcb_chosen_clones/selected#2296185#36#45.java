    public final double getMiddle() {
        if (is360()) {
            return angle1 + Math.PI;
        }
        double result = (angle1 + angle2) / 2;
        if (angle2 < angle1) {
            result += Math.PI;
        }
        return result;
    }
