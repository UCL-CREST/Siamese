    void makePlots() throws IOException {
        Process proc = Runtime.getRuntime().exec("GNUplot");
        if (proc == null) {
            System.out.println("Error opening GNUplot- it may not be installed or else path variable is not set");
            System.out.println("Cannot create sexy graphs");
            return;
        }
        OutputStream os = proc.getOutputStream();
        PrintStream ps = new PrintStream(os);
        makeCutoffPlotFile(ps);
        ps.close();
    }
