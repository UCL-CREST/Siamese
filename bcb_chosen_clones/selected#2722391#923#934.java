    private void copy(File fin, File fout) throws IOException {
        FileOutputStream out = new FileOutputStream(fout);
        FileInputStream in = new FileInputStream(fin);
        byte[] buf = new byte[2048];
        int read = in.read(buf);
        while (read > 0) {
            out.write(buf, 0, read);
            read = in.read(buf);
        }
        in.close();
        out.close();
    }
