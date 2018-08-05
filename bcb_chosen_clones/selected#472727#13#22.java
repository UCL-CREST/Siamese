    public static void main(String args[]) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(args[0]));
        Writer out = new FileWriter(args[1]);
        out = new WrapFilter(new BufferedWriter(out), 40);
        out = new TitleCaseFilter(out);
        String line;
        while ((line = in.readLine()) != null) out.write(line + "\n");
        out.close();
        in.close();
    }
