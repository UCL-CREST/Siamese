    public static boolean cpy(File a, File b) {
        try {
            FileInputStream astream = null;
            FileOutputStream bstream = null;
            try {
                astream = new FileInputStream(a);
                bstream = new FileOutputStream(b);
                long flength = a.length();
                int bufsize = (int) Math.min(flength, 1024);
                byte buf[] = new byte[bufsize];
                long n = 0;
                while (n < flength) {
                    int naread = astream.read(buf);
                    bstream.write(buf, 0, naread);
                    n += naread;
                }
            } finally {
                if (astream != null) astream.close();
                if (bstream != null) bstream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
