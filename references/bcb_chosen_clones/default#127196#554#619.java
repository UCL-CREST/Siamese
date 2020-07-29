    static void runCommand(String command, String outfile, String errorstring) throws java.lang.Exception {
        System.out.print(command);
        if (outfile != null) System.out.print(" > " + outfile);
        System.out.println();
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(command);
        PrintWriter outf;
        if (outfile != null) {
            outf = new PrintWriter(new FileOutputStream(outfile));
        } else {
            outf = new PrintWriter(System.out);
        }
        InputStream procStdout = p.getInputStream();
        InputStream procStderr = p.getErrorStream();
        int exit = 0, n1, n2;
        boolean processEnded = false;
        while (!processEnded) {
            try {
                exit = p.exitValue();
                processEnded = true;
            } catch (IllegalThreadStateException e) {
            }
            n1 = procStdout.available();
            if (n1 > 0) {
                for (int i = 0; i < n1; i++) {
                    int b = procStdout.read();
                    if (b < 0) break;
                    outf.write(b);
                }
            }
            n2 = procStderr.available();
            if (n2 > 0) {
                byte[] pbytes = new byte[n2];
                procStderr.read(pbytes);
                System.err.print(new String(pbytes));
                System.err.flush();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
        do {
            n1 = procStdout.available();
            if (n1 > 0) {
                for (int i = 0; i < n1; i++) {
                    int b = procStdout.read();
                    if (b < 0) break;
                    outf.write(b);
                }
            }
            n2 = procStderr.available();
            if (n2 > 0) {
                byte[] pbytes = new byte[n2];
                procStderr.read(pbytes);
                System.err.print(new String(pbytes));
                System.err.flush();
            }
        } while (n1 > 0 || n2 > 0);
        if (outfile != null) outf.close(); else outf.flush();
        if (exit != 0) {
            System.err.println("Error " + exit + " during subprocess execution");
            if (errorstring != null) System.err.println(errorstring);
            System.exit(-1);
        }
    }
