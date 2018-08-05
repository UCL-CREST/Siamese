    public static void convertEncoding(File infile, File outfile, String from, String to) throws IOException, UnsupportedEncodingException {
        InputStream in;
        if (infile != null) in = new FileInputStream(infile); else in = System.in;
        OutputStream out;
        outfile.createNewFile();
        if (outfile != null) out = new FileOutputStream(outfile); else out = System.out;
        if (from == null) from = System.getProperty("file.encoding");
        if (to == null) to = "Unicode";
        Reader r = new BufferedReader(new InputStreamReader(in, from));
        Writer w = new BufferedWriter(new OutputStreamWriter(out, to));
        char[] buffer = new char[4096];
        int len;
        while ((len = r.read(buffer)) != -1) w.write(buffer, 0, len);
        r.close();
        w.close();
    }
