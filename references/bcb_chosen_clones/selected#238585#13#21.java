    public static void copy(File inputFile, File target) throws IOException {
        if (!inputFile.exists()) return;
        OutputStream output = new FileOutputStream(target);
        InputStream input = new BufferedInputStream(new FileInputStream(inputFile));
        int b;
        while ((b = input.read()) != -1) output.write(b);
        output.close();
        input.close();
    }
