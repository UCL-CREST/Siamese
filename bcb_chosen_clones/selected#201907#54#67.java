    public static void copy(final String source, final String dest) {
        final File s = new File(source);
        final File w = new File(dest);
        try {
            final FileInputStream in = new FileInputStream(s);
            final FileOutputStream out = new FileOutputStream(w);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (IOException ioe) {
            System.out.println("Error reading/writing files!");
        }
    }
