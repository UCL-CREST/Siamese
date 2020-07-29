    private void rewriteFile(String folder, ZipOutputStream out, String zipPath, byte[] buf) throws IOException {
        File f = new File(folder);
        File[] content;
        FileInputStream in;
        if (f.isDirectory()) {
            content = f.listFiles();
            for (int i = 0; i < content.length; i++) {
                rewriteFile(folder + File.separator + content[i].getName(), out, zipPath + File.separator + content[i].getName(), buf);
            }
            return;
        }
        in = new FileInputStream(f);
        out.putNextEntry(new ZipEntry(zipPath));
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.closeEntry();
        in.close();
    }
