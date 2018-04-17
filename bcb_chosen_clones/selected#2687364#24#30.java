    public static void copyFile(String src, String target) throws IOException {
        FileChannel ic = new FileInputStream(src).getChannel();
        FileChannel oc = new FileOutputStream(target).getChannel();
        ic.transferTo(0, ic.size(), oc);
        ic.close();
        oc.close();
    }
