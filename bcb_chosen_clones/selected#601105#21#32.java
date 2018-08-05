    public void writeModule(Bundle module) throws IOException {
        ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(jarFile));
        try {
            for (Bundle.Entry entry : module.getEntries()) {
                ZipEntry zipEntry = new ZipEntry(entry.getName());
                stream.putNextEntry(zipEntry);
                stream.write(entry.getData());
            }
        } finally {
            stream.close();
        }
    }
