    public void setBinaryData(byte[] inflatedData) {
        if ((inflatedData == null) || (inflatedData.length == 0)) {
            throw new IllegalArgumentException("InflatedData is NULL");
        }
        m_inflated = new Integer(inflatedData.length);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(out);
        zip.setMethod(ZipOutputStream.DEFLATED);
        zip.setLevel(Deflater.BEST_COMPRESSION);
        zip.setComment("openxpertya");
        byte[] deflatedData = null;
        try {
            ZipEntry entry = new ZipEntry("OpenxpertyaArchive");
            entry.setTime(System.currentTimeMillis());
            entry.setMethod(ZipEntry.DEFLATED);
            zip.putNextEntry(entry);
            zip.write(inflatedData, 0, inflatedData.length);
            zip.closeEntry();
            log.fine(entry.getCompressedSize() + " (" + entry.getSize() + ") " + (entry.getCompressedSize() * 100 / entry.getSize()) + "%");
            zip.close();
            deflatedData = out.toByteArray();
            log.fine("Length=" + inflatedData.length);
            m_deflated = new Integer(deflatedData.length);
        } catch (Exception e) {
            log.log(Level.SEVERE, "saveLOBData", e);
            deflatedData = null;
            m_deflated = null;
        }
        super.setBinaryData(deflatedData);
    }
