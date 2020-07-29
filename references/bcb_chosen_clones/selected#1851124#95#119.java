    public InputStream getInputStream() {
        InputStream retIn = null;
        try {
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
            retIn = new ByteArrayInputStream(bos.toByteArray());
            bos.close();
        } catch (Exception ex) {
            String msg = "Unable to retrieve the content of ODT document.";
            throw new DocumentException(msg, ex);
        }
        return retIn;
    }
