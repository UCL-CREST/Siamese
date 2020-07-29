    private String storeProxy(String proxyPath, String userName) throws IOException {
        File inputFile = new File(proxyPath);
        String outfile = slcsFactory.getStoreDirectory() + File.separator + userName + File.separator + PROXYFILENAME;
        File outputFile = new File(outfile);
        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
        Process p = Runtime.getRuntime().exec("/bin/chmod 600 " + outfile);
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return outfile;
    }
