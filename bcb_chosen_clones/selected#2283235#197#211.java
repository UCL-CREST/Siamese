    public static List<String> unZip(File tarFile, File directory) throws IOException {
        List<String> result = new ArrayList<String>();
        InputStream inputStream = new FileInputStream(tarFile);
        ZipArchiveInputStream in = new ZipArchiveInputStream(inputStream);
        ZipArchiveEntry entry = in.getNextZipEntry();
        while (entry != null) {
            OutputStream out = new FileOutputStream(new File(directory, entry.getName()));
            IOUtils.copy(in, out);
            out.close();
            result.add(entry.getName());
            entry = in.getNextZipEntry();
        }
        in.close();
        return result;
    }
