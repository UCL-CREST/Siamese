    private static void loadFromZip() {
        InputStream in = Resources.class.getResourceAsStream("data.zip");
        if (in == null) {
            return;
        }
        ZipInputStream zipIn = new ZipInputStream(in);
        try {
            while (true) {
                ZipEntry entry = zipIn.getNextEntry();
                if (entry == null) {
                    break;
                }
                String entryName = entry.getName();
                if (!entryName.startsWith("/")) {
                    entryName = "/" + entryName;
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                IOUtils.copy(zipIn, out);
                zipIn.closeEntry();
                FILES.put(entryName, out.toByteArray());
            }
            zipIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
