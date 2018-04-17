    public static void copy(File in, File out) throws IOException {
        FileChannel ic = new FileInputStream(in).getChannel();
        FileChannel oc = new FileOutputStream(out).getChannel();
        ic.transferTo(0, ic.size(), oc);
        ic.close();
        oc.close();
    }
