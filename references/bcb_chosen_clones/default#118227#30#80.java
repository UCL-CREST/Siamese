    public static void main(String[] args) throws Exception {
        int[] switches = new int[7];
        int switchSource = 0;
        if (args.length == 0) {
            for (int x = 0; x < 100; x++) {
                int temp = generator.nextInt(2187);
                for (int i = 0; i < 7; i++) {
                    switches[i] = temp % 3;
                    temp = temp / 3;
                }
                String command = System.getProperty("java.home") + File.separator + "bin" + File.separator + "cvm Assert";
                System.out.println("Command = " + command);
                StringBuffer commandString = new StringBuffer(command);
                for (int j = 0; j < 7; j++) commandString.append(" " + switches[j]);
                Process p = null;
                p = Runtime.getRuntime().exec(commandString.toString());
                if (debug) {
                    BufferedReader blah = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String outString = blah.readLine();
                    while (outString != null) {
                        System.out.println("from slave:" + outString);
                        outString = blah.readLine();
                    }
                }
                p.waitFor();
                int result = p.exitValue();
                if (debug) {
                    if (result == 0) {
                        for (int k = 6; k >= 0; k--) System.out.print(switches[k]);
                        System.out.println();
                    } else {
                        System.out.print("Nonzero Exit: ");
                        for (int k = 6; k >= 0; k--) System.out.print(switches[k]);
                        System.out.println();
                    }
                } else {
                    if (result != 0) {
                        System.err.print("Nonzero Exit: ");
                        for (int k = 6; k >= 0; k--) System.err.print(switches[k]);
                        System.err.println();
                        throw new RuntimeException("Assertion test failure.");
                    }
                }
            }
        } else {
            for (int i = 0; i < 7; i++) switches[i] = Integer.parseInt(args[i]);
            SetAssertionSwitches(switches);
            ConstructClassTree();
            TestClassTree(switches);
        }
    }
