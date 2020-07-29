    private void zipAttachments(List attachments, ZipOutputStream zipStream) throws IOException, FileNotFoundException {
        byte[] buf = new byte[1024];
        Iterator iter = attachments.iterator();
        while (iter.hasNext()) {
            File attachement = (File) iter.next();
            if (attachement.getName().equals(m_fileable.getNameInZipArchive())) continue;
            zipStream.putNextEntry(new ZipEntry(attachement.getName()));
            FileInputStream in = new FileInputStream(attachement);
            int len;
            while ((len = in.read(buf)) > 0) {
                zipStream.write(buf, 0, len);
            }
            zipStream.closeEntry();
            in.close();
        }
    }
