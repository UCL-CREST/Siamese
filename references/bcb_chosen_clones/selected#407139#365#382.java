    public ByteArrayOutputStream makeZip(String[] filenames) throws IOException {
        byte[] buf = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(baos);
        for (int i = 0; i < filenames.length; i++) {
            FileInputStream in = new FileInputStream(filenames[i]);
            out.putNextEntry(new ZipEntry(new File(filenames[i]).getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.flush();
        out.close();
        return (baos);
    }
