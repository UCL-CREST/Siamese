    public void compress(String fileName, String dir) {
        Files files = new Files();
        String[] filenames = files.directoryList(dir);
        byte[] buf = new byte[1024];
        try {
            String outFilename = fileName;
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
            for (int i = 0; i < filenames.length; i++) {
                FileInputStream in = new FileInputStream(filenames[i]);
                String file = filenames[i].substring(dir.length() + 1);
                out.putNextEntry(new ZipEntry(file));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
        }
    }
