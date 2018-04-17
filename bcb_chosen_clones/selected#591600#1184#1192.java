    private void copy(File from, File to) throws FileNotFoundException, IOException {
        FileReader in;
        in = new FileReader(from);
        FileWriter out = new FileWriter(to);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
    }
