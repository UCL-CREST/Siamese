    public static boolean copyTextFile(File src, File dst) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dst));
            byte[] buf = new byte[1024];
            int readsize = 0;
            while ((readsize = bis.read(buf)) != -1) {
                bos.write(buf, 0, readsize);
            }
            bos.flush();
            bos.close();
            bis.close();
        } catch (IOException e) {
            ServerConsoleServlet.printSystemLog(e.toString() + " " + e.getMessage(), ServerConsoleServlet.LOG_ERROR);
            return false;
        }
        return true;
    }
