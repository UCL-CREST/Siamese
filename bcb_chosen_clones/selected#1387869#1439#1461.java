    public static int copyDirectory(File srcDir, File destDir) {
        if (!srcDir.isDirectory()) return common_h.ERROR;
        if (!destDir.canWrite()) return common_h.ERROR;
        String[] contents = srcDir.list();
        for (int i = 0; i < contents.length; i++) {
            if (new File(srcDir.getAbsolutePath(), contents[i]).isDirectory()) copyDirectory(new File(srcDir.getAbsolutePath(), contents[i]), new File(destDir.getAbsolutePath(), contents[i])); else {
                try {
                    InputStream in = new FileInputStream(srcDir.getAbsolutePath() + "/" + contents[i]);
                    OutputStream out = new FileOutputStream(destDir.getAbsolutePath() + "/" + contents[i]);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                } catch (Exception e) {
                    return common_h.ERROR;
                }
            }
        }
        return common_h.OK;
    }
