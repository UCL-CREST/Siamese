    public static void copy(URL url, String outPath) throws IOException {
        System.out.println("copying from: " + url + " to " + outPath);
        InputStream in = url.openStream();
        FileOutputStream fout = new FileOutputStream(outPath);
        byte[] data = new byte[8192];
        int read = -1;
        while ((read = in.read(data)) != -1) {
            fout.write(data, 0, read);
        }
        fout.close();
    }
