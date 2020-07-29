    public boolean copyDirectoryTree(File srcPath, File dstPath) {
        try {
            if (srcPath.isDirectory()) {
                if (!dstPath.exists()) dstPath.mkdir();
                String files[] = srcPath.list();
                for (int i = 0; i < files.length; i++) copyDirectoryTree(new File(srcPath, files[i]), new File(dstPath, files[i]));
            } else {
                if (!srcPath.exists()) {
                    errMsgLog += "copyDirectoryTree I/O error from '" + srcPath + "' does not exist.\n";
                    lastErrMsgLog = errMsgLog;
                    return (false);
                } else {
                    InputStream in = new FileInputStream(srcPath);
                    OutputStream out = new FileOutputStream(dstPath);
                    byte[] buf = new byte[10240];
                    int len;
                    while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
                    in.close();
                    out.close();
                }
            }
            return (true);
        } catch (Exception e) {
            errMsgLog += "copyDirectoryTree I/O error from '" + srcPath.getName() + "' to '" + dstPath.getName() + "\n  " + e + "\n";
            lastErrMsgLog = errMsgLog;
            return (false);
        }
    }
