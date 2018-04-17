    public static void copyFile(String pathOrig, String pathDst) throws FileNotFoundException, IOException {
        InputStream in;
        OutputStream out;
        if (pathOrig == null || pathDst == null) {
            System.err.println("Error en path");
            return;
        }
        File orig = new File(pathOrig);
        if (!orig.exists() || !orig.isFile() || !orig.canRead()) {
            System.err.println("Error en fichero de origen");
            return;
        }
        File dest = new File(pathDst);
        String file = new File(pathOrig).getName();
        if (dest.isDirectory()) pathDst += file;
        in = new FileInputStream(pathOrig);
        out = new FileOutputStream(pathDst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
        in.close();
        out.close();
    }
