    public static void zipFile(File file, String nameZip, String pathExport) throws IOException {
        FileOutputStream f = new FileOutputStream(pathExport + File.separator + nameZip);
        CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(csum));
        BufferedReader in = new BufferedReader(new FileReader(file));
        out.putNextEntry(new ZipEntry(file.getAbsolutePath()));
        int c;
        while ((c = in.read()) != -1) {
            out.write(c);
        }
        in.close();
        out.close();
    }
