    boolean do_which(String className, File f) throws Exception {
        Runtime rt = Runtime.getRuntime();
        if (verbose) System.out.println(f);
        Process proc = rt.exec(JAR_CMD + " -tvf " + f.getAbsolutePath());
        InputStream proc_out = proc.getInputStream();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(proc_out));
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf(className) != -1) {
                    System.out.println();
                    System.out.println("Match found: ");
                    System.out.println("\t" + line.trim());
                    System.out.println("\t" + f.getAbsolutePath());
                    System.out.println();
                    return true;
                }
            }
            return false;
        } finally {
            if (reader != null) reader.close();
        }
    }
