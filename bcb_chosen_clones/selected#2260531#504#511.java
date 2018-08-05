    public static void copyFile(File from, File to) throws IOException {
        FileInputStream in = new FileInputStream(from);
        FileOutputStream out = new FileOutputStream(to);
        int c;
        while ((c = in.read()) != -1) out.write(c);
        in.close();
        out.close();
    }
