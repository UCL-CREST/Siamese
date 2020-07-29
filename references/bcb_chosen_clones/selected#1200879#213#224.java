    public static void copyFile(File src, File dest) {
        try {
            FileInputStream in = new FileInputStream(src);
            FileOutputStream out = new FileOutputStream(dest);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
