    public static void main(String[] args) {
        if (args.length != 2) throw new IllegalArgumentException();
        String inFileName = args[0];
        String outFileName = args[1];
        File fInput = new File(inFileName);
        Scanner in = null;
        try {
            in = new Scanner(fInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(outFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (in.hasNextLine()) {
            out.println(in.nextLine());
        }
        in.close();
        out.close();
    }
