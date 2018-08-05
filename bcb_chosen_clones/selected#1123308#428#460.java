    private boolean saveLOBDataToDB() {
        if (m_items == null || m_items.size() == 0) {
            setBinaryData(null);
            return true;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(out);
        zip.setMethod(ZipOutputStream.DEFLATED);
        zip.setLevel(Deflater.BEST_COMPRESSION);
        zip.setComment("adempiere");
        try {
            for (int i = 0; i < m_items.size(); i++) {
                MAttachmentEntry item = getEntry(i);
                ZipEntry entry = new ZipEntry(item.getName());
                entry.setTime(System.currentTimeMillis());
                entry.setMethod(ZipEntry.DEFLATED);
                zip.putNextEntry(entry);
                byte[] data = item.getData();
                zip.write(data, 0, data.length);
                zip.closeEntry();
                log.fine(entry.getName() + " - " + entry.getCompressedSize() + " (" + entry.getSize() + ") " + (entry.getCompressedSize() * 100 / entry.getSize()) + "%");
            }
            zip.close();
            byte[] zipData = out.toByteArray();
            log.fine("Length=" + zipData.length);
            setBinaryData(zipData);
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, "saveLOBData", e);
        }
        setBinaryData(null);
        return false;
    }
