    public static void copy(String from, String to) throws Exception {
        File inputFile = new File(from);
        File outputFile = new File(to);
        FileInputStream in = new FileInputStream(inputFile);
        FileOutputStream out = new FileOutputStream(outputFile);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) out.write(buffer, 0, len);
        in.close();
        out.close();
    }
