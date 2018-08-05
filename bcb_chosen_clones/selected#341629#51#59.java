    private void copy(File from, File to) throws IOException {
        InputStream in = new FileInputStream(from);
        OutputStream out = new FileOutputStream(to);
        byte[] line = new byte[16384];
        int bytes = -1;
        while ((bytes = in.read(line)) != -1) out.write(line, 0, bytes);
        in.close();
        out.close();
    }
