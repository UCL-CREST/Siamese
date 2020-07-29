    public static void copyAkteraFile(String src, String dst) throws FileNotFoundException, IOException {
        IOUtils.copy(new FileInputStream(newAkteraFile(src)), new FileOutputStream(newAkteraFile(dst)));
    }
