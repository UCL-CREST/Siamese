    public static void main(String[] args) {
        System.out.println("Input any text with Unicode symbols: \\u**** (or &#****; if the program started with &#; parameter). Type 'stop' to exit");
        System.out.println("If you want to read from and save to file, use < and > command line syntax");
        String unicode = "\\u";
        if (args.length > 0) unicode = args[0];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Writer writer = new OutputStreamWriter(System.out);
            String delim = " ";
            Pattern pattern = Pattern.compile(delim);
            while (true) {
                String ss = reader.readLine();
                if (ss == null || "stop".equalsIgnoreCase(ss)) break;
                Matcher m = pattern.matcher(ss);
                int i = 0;
                while (m.find()) {
                    String s = ss.substring(i, m.start());
                    i = m.end();
                    decode(writer, s, unicode);
                    writer.write(delim);
                }
                if (i < ss.length()) decode(writer, ss.substring(i), unicode);
                writer.write("\r\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
