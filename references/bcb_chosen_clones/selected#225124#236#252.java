    public static void zipToOut(InputStream[] inputStreams, OutputStream out, String fileNamePrex) throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(out);
        for (int i = 0; i < inputStreams.length; i++) {
            zipout.putNextEntry(new ZipEntry(fileNamePrex + (i + 1) + ".xls"));
            byte[] buf = new byte[2048];
            BufferedInputStream origin = new BufferedInputStream(inputStreams[i], 2048);
            int len;
            while ((len = origin.read(buf, 0, 2048)) != -1) {
                zipout.write(buf, 0, len);
            }
            zipout.flush();
            origin.close();
            inputStreams[i].close();
        }
        zipout.flush();
        zipout.close();
    }
