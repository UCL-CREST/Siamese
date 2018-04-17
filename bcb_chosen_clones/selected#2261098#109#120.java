    public void writeCache() {
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(m_cachefile));
            zos.putNextEntry(new ZipEntry("FileNameMapper.cache"));
            ObjectOutputStream os = new ObjectOutputStream(zos);
            os.writeObject(m_cachecontent);
            os.flush();
            os.close();
        } catch (Exception ex) {
            m_task.log("Impossible to write cache file " + m_cachefile);
        }
    }
