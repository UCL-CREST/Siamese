    public static void main(String[] args) {
        final String mN = CLASSNAME + ".main() -> ";
        if (args.length == 0) {
            byte[] sampledata = { 1, 1, 1, 0, 0 };
            try {
                writeOutputFile("/home/mdtrl/inputfile.dat", sampledata);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        if (args.length != 4) {
            String error = "Must specify command line arguments properly, as follows:\n";
            error += "<AlgorithmClassNameToRun, no package> <path to file to use as input bytes> " + "<path to file to use as output bytes> <'R' or 'S' for RECV or SEND>";
            throw new IllegalArgumentException(error);
        }
        String algClass = args[0];
        String inputFile = args[1];
        String outputFile = args[2];
        String mode = args[3];
        boolean recv = false;
        if (mode.equalsIgnoreCase("R")) {
            recv = true;
        }
        try {
            byte[] inputdata = parseFile(inputFile);
            LogIt.outNormalAsLog(mN + "Attempting to get Class object for '" + algClass + "'...");
            Class clazz = Class.forName("edu.smu.cse8377.algs." + algClass);
            LogIt.outNormalAsLog(mN + "Attempting to get Constructor with parameter of byte[]...");
            Constructor ctor = clazz.getConstructor(byte[].class);
            AbsECDAlgorithm algorithmObject = (AbsECDAlgorithm) ctor.newInstance(inputdata);
            algorithmObject.enableTestMode();
            LogIt.outNormalAsLog(mN + "Got object OK.");
            if (recv) {
                LogIt.outNormalAsLog(mN + "Invoking for RECV since mode is '" + mode + "'...");
                algorithmObject.prepareToRecv();
            } else {
                LogIt.outNormalAsLog(mN + "Invoking for SEND since mode is '" + mode + "'...");
                algorithmObject.prepareToSend();
            }
            byte[] finalBytes = algorithmObject.getData();
            LogIt.outNormalAsLog(mN + "Final byte[] obtained - attempting to write output file...");
            writeOutputFile(outputFile, finalBytes);
            LogIt.outNormalAsLog(mN + "EXECUTION COMPLETE!");
            System.exit(0);
        } catch (Throwable t) {
            LogIt.errErrorAsLog(mN + "An exception occurred during execution!");
            LogIt.errExAsLog(t);
            LogIt.errErrorAsLog(mN + "EXECUTION FAILED!");
            System.exit(1);
        }
    }
