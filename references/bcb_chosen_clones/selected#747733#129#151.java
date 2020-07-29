    public void add(File f, String archivePath) {
        if (out == null) init();
        empty = false;
        m_logCat.info("Adding " + f + " in " + archivePath);
        int b = 0;
        File currentFilePath = new File(archivePath);
        try {
            out.putNextEntry(new ZipEntry(archivePath));
            BufferedInputStream cacheIn = new BufferedInputStream(new FileInputStream(f));
            while ((b = cacheIn.read()) != -1) out.write(b);
            cacheIn.close();
            out.closeEntry();
        } catch (ZipException ze) {
            String s = ze.getMessage();
            if (s.indexOf("duplicate entry") == -1) {
                m_logCat.error("I/O error ", ze);
            }
        } catch (FileNotFoundException fe) {
            m_logCat.error("File not found ", fe);
        } catch (IOException e) {
            m_logCat.error("I/O error " + e.getMessage());
        }
    }
