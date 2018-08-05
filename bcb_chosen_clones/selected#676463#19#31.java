    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 2) throw new IllegalArgumentException();
        String fnOut = args[args.length - 1];
        PrintWriter writer = new PrintWriter(fnOut);
        for (int i = 0; i < args.length - 1; i++) {
            File fInput = new File(args[i]);
            Scanner in = new Scanner(fInput);
            while (in.hasNext()) {
                writer.println(in.nextLine());
            }
        }
        writer.close();
    }
