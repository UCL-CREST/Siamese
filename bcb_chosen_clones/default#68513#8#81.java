    public static void main(String[] args) {
        boolean doInterpRun = false;
        boolean doRun = true;
        Class runAllClass;
        Method runAllMain = null;
        String[] argsToPass;
        String[] kbItems = { "-f", "kBenchItems.txt" };
        if (args.length > 0) {
            int i;
            int numExtras = 0;
            for (i = 0; i < args.length; i++) {
                if (args[i].equals("-i")) {
                    doInterpRun = true;
                    numExtras++;
                } else if (args[i].equals("-n")) {
                    doRun = false;
                    numExtras++;
                }
            }
            if (numExtras > 0) {
                argsToPass = new String[args.length - numExtras];
                for (int j = numExtras; j < args.length; j++) {
                    argsToPass[j - numExtras] = args[j];
                }
            } else {
                argsToPass = args;
            }
        } else {
            argsToPass = args;
        }
        try {
            runAllClass = Class.forName("MyRunAll");
            runAllMain = runAllClass.getDeclaredMethod("main", new Class[] { kbItems.getClass() });
        } catch (Exception e) {
            System.err.println("Lookup of MyRunAll.main got an exception:");
            e.printStackTrace();
            System.exit(1);
        }
        if (doInterpRun) {
            System.err.println("Running MyRunAll interpreted\n");
            try {
                runAllMain.invoke(null, new Object[] { argsToPass });
            } catch (Exception e) {
                System.err.println("MyRunAll.main got an exception:");
                e.printStackTrace();
                System.exit(1);
            }
        } else {
            System.err.println("Running static initializers\n");
            for (int i = 0; i < clinitClassNames.length; i++) {
                try {
                    Class c = Class.forName(clinitClassNames[i]);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        System.err.println("Running GC\n");
        System.gc();
        System.err.println("Compiling named classes...\n");
        CompilerTest.main(kbItems);
        System.err.println("Running GC\n");
        System.gc();
        if (doRun) {
            System.err.println("Running kBench compiled\n");
            try {
                runAllMain.invoke(null, new Object[] { argsToPass });
            } catch (Exception e) {
                System.err.println("RunAll.main got an exception:");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
