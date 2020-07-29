    public void copyFile(File s, File t) {
        try {
            FileChannel in = (new FileInputStream(s)).getChannel();
            FileChannel out = (new FileOutputStream(t)).getChannel();
            in.transferTo(0, s.length(), out);
            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
