    private static void copyFile(String from, String to) throws IOException {
        FileReader in = new FileReader(from);
        FileWriter out = new FileWriter(to);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
    }
