    @SuppressWarnings("unused")
    public static void main(String[] args) {
        int angleCount = 10800;
        float[] angles = new float[angleCount];
        float[] sinerror = new float[angleCount];
        float[] coserror = new float[angleCount];
        float[] atanerror = new float[angleCount];
        for (int i = 0; i < angleCount; i++) {
            float angle = (float) (Math.PI * 2 * i / angleCount);
            angles[i] = angle;
            float rs = (float) Math.sin(angle);
            float ls = sin(angle);
            sinerror[i] = Math.abs(rs - ls);
            float rc = (float) Math.cos(angle);
            float lc = cos(angle);
            coserror[i] = Math.abs(rc - lc);
            float ratan = (float) Math.atan2(100 * rs, 100 * rc);
            float latan = atan2(100 * rs, 100 * rc);
            atanerror[i] = MathUtils.angleDiff(ratan, latan);
        }
        float maxSin = -1, minSin = Float.MAX_VALUE, meanSin = 0;
        float maxCos = -1, minCos = Float.MAX_VALUE, meanCos = 0;
        float maxAtan = -1, minAtan = Float.MAX_VALUE, meanAtan = 0;
        for (int i = 0; i < angleCount; i++) {
            maxSin = Math.max(maxSin, sinerror[i]);
            minSin = Math.min(minSin, sinerror[i]);
            maxCos = Math.max(maxCos, coserror[i]);
            minCos = Math.min(minCos, coserror[i]);
            maxAtan = Math.max(maxAtan, atanerror[i]);
            minAtan = Math.min(minAtan, atanerror[i]);
            meanSin += sinerror[i];
            meanCos += coserror[i];
            meanAtan += atanerror[i];
        }
        meanSin /= angleCount;
        meanCos /= angleCount;
        meanAtan /= angleCount;
        System.out.println("Accuracy:");
        System.out.println("sin/cos table size = " + sin.length);
        System.out.println("Sin\tmin\t\tmax\t\tmean");
        System.out.println("\t" + minSin + "\t" + maxSin + "\t" + meanSin);
        System.out.println("Cos\tmin\t\tmax\t\tmean");
        System.out.println("\t" + minCos + "\t" + maxCos + "\t" + meanCos);
        System.out.println("atan2 table size = " + atan2.length);
        System.out.println("Atan2\tmin\t\tmax\t\tmean (in degrees)");
        System.out.println("\t" + Math.toDegrees(minAtan) + "\t" + Math.toDegrees(maxAtan) + "\t" + Math.toDegrees(meanAtan));
        System.out.println("Performance:");
        Random rng = new Random();
        for (int i = angles.length - 1; i >= 0; i--) {
            int index = rng.nextInt(i + 1);
            float a = angles[index];
            angles[index] = angles[i];
            angles[i] = a;
        }
        System.out.println("testing");
        int tests = (int) 2E7;
        for (int i = 0; i < 10; i++) {
            long t = System.currentTimeMillis();
            float jm = testMathSin(tests, angles);
            long duration = System.currentTimeMillis() - t;
            double jp = (double) tests / duration;
            t = System.currentTimeMillis();
            float fm = testFastSin(tests, angles);
            duration = System.currentTimeMillis() - t;
            double fp = (double) tests / duration;
            System.out.println("Fast sin is " + fp / jp + " times faster than java sin");
        }
        float[] coords = new float[100];
        float r = 100;
        for (int i = 0; i < coords.length; i++) {
            coords[i] = rng.nextFloat() * 2 * r - r;
        }
        for (int i = 0; i < 10; i++) {
            long t = System.currentTimeMillis();
            float jm = testMathAtan(tests, coords);
            long duration = System.currentTimeMillis() - t;
            double jp = (double) tests / duration;
            t = System.currentTimeMillis();
            float fm = testFastAtan(tests, coords);
            duration = System.currentTimeMillis() - t;
            double fp = (double) tests / duration;
            System.out.println("Fast atan is " + fp / jp + " times faster than java atan");
        }
    }
