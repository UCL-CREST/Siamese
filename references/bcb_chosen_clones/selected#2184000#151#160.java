    protected void writeMeta(MetaFile metafile, ZipOutputStream zipout) throws IOException {
        String smeta = metafile.toXMLString();
        byte[] bmeta = smeta != null ? smeta.getBytes(TextUtil.UTF8()) : null;
        if (bmeta != null) {
            ZipEntry zeFile = new ZipEntry(metaFileName());
            zipout.putNextEntry(zeFile);
            zipout.write(bmeta);
            zipout.closeEntry();
        }
    }
