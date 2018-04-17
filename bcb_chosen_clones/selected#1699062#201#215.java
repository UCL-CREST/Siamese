    public static List<String> unTar(File tarFile, File directory) throws IOException {
        List<String> result = new ArrayList<String>();
        InputStream inputStream = new FileInputStream(tarFile);
        TarArchiveInputStream in = new TarArchiveInputStream(inputStream);
        TarArchiveEntry entry = in.getNextTarEntry();
        while (entry != null) {
            OutputStream out = new FileOutputStream(new File(directory, entry.getName()));
            IOUtils.copy(in, out);
            out.close();
            result.add(entry.getName());
            entry = in.getNextTarEntry();
        }
        in.close();
        return result;
    }
