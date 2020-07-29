    public static void copyFromHDFSMerge(String hdfsDir, String local) throws IOException {
        rmr(local);
        File f = new File(local);
        f.getAbsoluteFile().getParentFile().mkdirs();
        HDFSDirInputStream inp = new HDFSDirInputStream(hdfsDir);
        FileOutputStream oup = new FileOutputStream(local);
        IOUtils.copyBytes(inp, oup, 65 * 1024 * 1024, true);
    }
