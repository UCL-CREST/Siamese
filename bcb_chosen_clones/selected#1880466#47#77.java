    public static final void parse(String infile, String outfile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(infile));
        DataOutputStream output = new DataOutputStream(new FileOutputStream(outfile));
        int w = Integer.parseInt(reader.readLine());
        int h = Integer.parseInt(reader.readLine());
        output.writeByte(w);
        output.writeByte(h);
        int lineCount = 2;
        try {
            do {
                for (int i = 0; i < h; i++) {
                    lineCount++;
                    String line = reader.readLine();
                    if (line == null) {
                        throw new RuntimeException("Unexpected end of file at line " + lineCount);
                    }
                    for (int j = 0; j < w; j++) {
                        char c = line.charAt(j);
                        System.out.print(c);
                        output.writeByte(c);
                    }
                    System.out.println("");
                }
                lineCount++;
                output.writeShort(Short.parseShort(reader.readLine()));
            } while (reader.readLine() != null);
        } finally {
            reader.close();
            output.close();
        }
    }
