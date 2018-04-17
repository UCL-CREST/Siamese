    public static boolean runTest(String test) {
        trace("  ... " + test);
        try {
            Class test_class = Class.forName(test);
            Method main = test_class.getMethod("main", new Class[] { String[].class });
            PipedInputStream in = new PipedInputStream();
            PrintStream out = new PrintStream(new PipedOutputStream(in));
            System.setOut(out);
            Check checker = new Check(in);
            Thread checker_thread = new Thread(checker);
            checker_thread.setDaemon(true);
            checker_thread.start();
            main.invoke(null, new Object[] { new String[] {} });
            out.close();
            if (checker.passed()) {
                stdout.println(test + " passed");
            } else {
                stdout.println("---v---8< " + test + " output >8---v---");
                checker.dump(stdout);
                stdout.println("---^---8< " + test + " output >8---^---");
                stdout.println(test + " failed");
                return false;
            }
        } catch (Throwable e) {
            stdout.println(test + " failed, " + e);
            return false;
        }
        return true;
    }
