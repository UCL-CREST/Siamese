    public static void copy(File file1, File file2) throws IOException {
        FileReader in = new FileReader(file1);
        FileWriter out = new FileWriter(file2);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
    }
