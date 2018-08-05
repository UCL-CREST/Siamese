    public static void copyDirectory(File srcPath, File dstPath) throws IOException {
        if (srcPath.getName().contains("svn") || srcPath.getName().contains("backup")) return;
        if (srcPath.isDirectory()) {
            if (!dstPath.exists()) {
                dstPath.mkdirs();
            }
            String files[] = srcPath.list();
            for (int i = 0; i < files.length; i++) {
                copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
            }
        } else {
            if (!srcPath.exists()) {
                System.out.println("Arquivo ou diret�rio n�o existe.");
                System.exit(0);
            } else {
                InputStream in = new FileInputStream(srcPath);
                OutputStream out = new FileOutputStream(dstPath);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }
        System.out.println("Directory copied.");
    }
