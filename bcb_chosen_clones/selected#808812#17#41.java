    public static void jar(File folder, String outputname, FileFilter filter) {
        File files[] = null;
        if (filter != null) {
            files = folder.listFiles(filter);
        } else {
            files = folder.listFiles();
        }
        byte[] buf = new byte[1024];
        try {
            String outFilename = outputname;
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
            for (int i = 0; i < files.length; i++) {
                FileInputStream in = new FileInputStream(files[i]);
                out.putNextEntry(new ZipEntry(files[i].getCanonicalPath()));
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
