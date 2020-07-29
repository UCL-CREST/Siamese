    public static boolean copyFile(String fromfile, String tofile) {
        File from = new File(fromfile);
        File to = new File(tofile);
        if (!from.exists()) return false;
        if (to.exists()) {
            log.error(tofile + "exists already");
            return false;
        }
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        FileInputStream fis = null;
        FileOutputStream ois = null;
        boolean flag = true;
        try {
            to.createNewFile();
            fis = new FileInputStream(from);
            ois = new FileOutputStream(to);
            in = new BufferedInputStream(fis);
            out = new BufferedOutputStream(ois);
            byte[] buf = new byte[2048];
            int readBytes = 0;
            while ((readBytes = in.read(buf, 0, buf.length)) != -1) {
                out.write(buf, 0, readBytes);
            }
        } catch (IOException e) {
            log.error(e);
            flag = false;
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (IOException e) {
                log.error(e);
                flag = false;
            }
        }
        return flag;
    }
