    private void addResourceToZip(ZipOutputStream out, String resourceName) throws IOException {
        ZipEntry entry = new ZipEntry(resourceName);
        out.putNextEntry(entry);
        String className = getClass().getName();
        String fileName = className.replaceAll("\\x2E", "/");
        fileName = fileName.substring(0, fileName.lastIndexOf("/"));
        fileName = fileName + "/" + resourceName;
        InputStream in = getResourceAsStream(fileName);
        byte[] tmp = new byte[1024];
        int len = in.read(tmp);
        while (len > 0) {
            out.write(tmp, 0, len);
            len = in.read(tmp);
        }
    }
