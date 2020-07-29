    public static boolean copyDirectory(File srcPath, File dstPath) throws IOException {
        if (srcPath.isDirectory()) {
            if (!dstPath.exists()) {
                dstPath.mkdir();
            }
            String files[] = srcPath.list();
            for (String file : files) {
                copyDirectory(new File(srcPath, file), new File(dstPath, file));
            }
        } else {
            if (!srcPath.exists()) {
                logger.error("File or directory does not exist.");
                return false;
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
        logger.info("Directory " + srcPath + " copied to " + dstPath);
        return true;
    }
