    public static void zipDirectory(File directory, String nameZip, String pathExport, boolean pathWithFirstDirectory) throws IOException {
        FileOutputStream f = new FileOutputStream(pathExport + File.separator + nameZip);
        CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(csum));
        Collection<File> listeFiles = getListeFile(directory, FILTER_ALLFILE, true, false);
        Iterator<File> itListeFiles = listeFiles.iterator();
        while (itListeFiles.hasNext()) {
            File fileToZip = (File) itListeFiles.next();
            FileInputStream in = new FileInputStream(fileToZip);
            out.putNextEntry(new ZipEntry(getZipEntryName(directory, fileToZip, pathWithFirstDirectory)));
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            in.close();
        }
        out.close();
    }
