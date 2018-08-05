    public void saveSettings() {
        if (m_strFilename == null || "".equals(m_strFilename)) return;
        if (!m_settings.isDirty()) return;
        try {
            ZipFile fileIn = new ZipFile(new File(m_strFilename));
            File fileTmp = File.createTempFile("librarian-save", FileStorageUtil.LIBRARIAN_FILE_EXTENSION);
            File file = new File(m_strFilename);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileTmp));
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            out.putNextEntry(new ZipEntry("settings.xml"));
            m_settings.toStream(out);
            out.closeEntry();
            Enumeration<? extends ZipEntry> enumZipEntries = fileIn.entries();
            while (enumZipEntries.hasMoreElements()) {
                ZipEntry zeIn = enumZipEntries.nextElement();
                if (zeIn.getName().equals("settings.xml")) continue;
                out.putNextEntry(new ZipEntry(zeIn.getName()));
                BufferedInputStream bis = new BufferedInputStream(fileIn.getInputStream(zeIn));
                byte[] rgBuf = new byte[4096];
                for (; ; ) {
                    int nBytesRead = bis.read(rgBuf);
                    if (nBytesRead == -1) break;
                    out.write(rgBuf, 0, nBytesRead);
                }
                out.closeEntry();
            }
            fileIn.close();
            out.close();
            FileUtil.copyFile(fileTmp, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
