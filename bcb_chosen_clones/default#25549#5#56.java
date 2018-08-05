    public static void main(String[] args) throws Exception {
        boolean external = false;
        if (args.length == 1) {
            if (args[0].equals("-h")) {
                printZMERTUsage(args.length, true);
                System.exit(2);
            } else {
                external = false;
            }
        } else if (args.length == 3) {
            external = true;
        } else {
            printZMERTUsage(args.length, false);
            System.exit(1);
        }
        if (!external) {
            MertCore myMert = new MertCore(args[0]);
            myMert.run_MERT();
            myMert.finish();
        } else {
            int maxMem = Integer.parseInt(args[1]);
            String configFileName = args[2];
            String stateFileName = "ZMERT.temp.state";
            String cp = System.getProperty("java.class.path");
            boolean done = false;
            int iteration = 0;
            while (!done) {
                ++iteration;
                Runtime rt = Runtime.getRuntime();
                Process p = rt.exec("java -Xmx" + maxMem + "m -cp " + cp + " joshua.zmert.MertCore " + configFileName + " " + stateFileName + " " + iteration);
                BufferedReader br_i = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader br_e = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String dummy_line = null;
                while ((dummy_line = br_i.readLine()) != null) {
                    System.out.println(dummy_line);
                }
                while ((dummy_line = br_e.readLine()) != null) {
                    System.out.println(dummy_line);
                }
                int status = p.waitFor();
                if (status == 90) {
                    done = true;
                } else if (status == 91) {
                    done = false;
                } else {
                    System.out.println("Z-MERT exiting prematurely (MertCore returned " + status + ")...");
                    break;
                }
            }
        }
        System.exit(0);
    }
