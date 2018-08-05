    public static boolean copyFile(File src, File des) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(des));
            int b;
            while ((b = in.read()) != -1) out.write(b);
            out.flush();
            out.close();
            in.close();
            return true;
        } catch (IOException ie) {
            m_logCat.error("Copy file + " + src + " to " + des + " failed!", ie);
            return false;
        }
    }
