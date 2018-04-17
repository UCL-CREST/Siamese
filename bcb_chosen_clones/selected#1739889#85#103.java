    public static void createZip(ListResource resources, String destinationJar) throws Exception {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destinationJar));
            for (int index = 0; index < resources.size(); index++) {
                Resource resource = resources.get(index);
                out.putNextEntry(new ZipEntry(resource.getPathNewExtension()));
                BufferedReader reader = new BufferedReader(new StringReader(resource.getExportedContent()));
                String line;
                while ((line = reader.readLine()) != null) {
                    out.write(line.getBytes());
                }
                out.closeEntry();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
