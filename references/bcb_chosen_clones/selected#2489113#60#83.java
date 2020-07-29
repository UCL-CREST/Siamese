    private static void zipRecursively(ZipOutputStream out, File dir, String zippath, String[] filenames) {
        byte[] buf = new byte[1024];
        try {
            for (int i = 0; i < filenames.length; i++) {
                File f = new File(dir, filenames[i]);
                if (f.isDirectory()) {
                    String[] subfiles = f.list();
                    zipRecursively(out, f, zippath + f.getName() + "/", subfiles);
                } else {
                    FileInputStream in = new FileInputStream(new File(dir, filenames[i]));
                    String entrypath = zippath + filenames[i];
                    out.putNextEntry(new ZipEntry(entrypath));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
