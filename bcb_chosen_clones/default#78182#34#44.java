    private static void run(String alg, int[] array, Sorter s) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InterruptedException {
        Thread.sleep(3000);
        System.out.print("running " + alg + " ... ");
        if (printArrays) System.out.print(arrayToString(array, true));
        Method m = s.getClass().getMethod(alg, int[].class);
        long startTime = System.currentTimeMillis();
        m.invoke(s, array);
        double elapsed = ((double) (System.currentTimeMillis() - startTime) / 1000);
        System.out.println("took " + elapsed + " sec. ");
        if (printArrays) System.out.println(" -> " + arrayToString(array, true));
    }
