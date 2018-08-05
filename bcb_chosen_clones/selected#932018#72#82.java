    private static void delete(File file) throws IOException {
        if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                delete(files[i]);
            }
        }
        if (!file.delete()) {
            throw new IOException(NLS.bind(HelpBaseResources.IndexToolApplication_cannotDelete, file.getAbsolutePath()));
        }
    }
