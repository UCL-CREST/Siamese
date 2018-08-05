    private void load() {
        File backFile = null;
        ZipFile zipFile = null;
        Enumeration zippedFiles = null;
        ZipEntry currEntry = null;
        ZipEntry entry = null;
        try {
            String oldName = archiveFile.toString() + ".bak";
            archiveFile.renameTo(new File(oldName));
            backFile = new File(archiveFile.toString() + ".bak");
            zipFile = new ZipFile(backFile.getAbsolutePath());
            zippedFiles = zipFile.entries();
            out = new ZipOutputStream(new FileOutputStream(archiveFile));
            long presentTime = Calendar.getInstance().getTime().getTime();
            out.setMethod(out.DEFLATED);
            while (zippedFiles.hasMoreElements()) {
                currEntry = (ZipEntry) zippedFiles.nextElement();
                BufferedInputStream reader = new BufferedInputStream(zipFile.getInputStream(currEntry));
                int b;
                out.putNextEntry(new ZipEntry(currEntry.getName()));
                while ((b = reader.read()) != -1) out.write(b);
                reader.close();
                out.flush();
                out.closeEntry();
            }
            zipFile.close();
        } catch (Exception e) {
            m_logCat.error("Cannot load zip file", e);
        }
    }
