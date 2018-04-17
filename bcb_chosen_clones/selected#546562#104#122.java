    public InputStream getInputStream() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(bos);
        for (Object oEntryName : entries.keySet()) {
            String entryName = (String) oEntryName;
            byte[] content = (byte[]) entries.get(entryName);
            out.putNextEntry(new ZipEntry(entryName));
            if (content.length > 0) {
                out.write(content);
            }
            out.closeEntry();
        }
        out.flush();
        out.close();
        bos.flush();
        InputStream retIn = new ByteArrayInputStream(bos.toByteArray());
        bos.close();
        return retIn;
    }
