    public synchronized void zip(File file, File dest) {
        try {
            String entryPath = dest.getAbsolutePath().substring(tempDir.getAbsolutePath().length() + 1).replace(File.separatorChar, '/');
            int bytesRead;
            byte[] buffer = new byte[1024];
            CRC32 crc = new CRC32();
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            crc.reset();
            while ((bytesRead = bis.read(buffer)) != -1) {
                crc.update(buffer, 0, bytesRead);
            }
            bis.close();
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(entryPath);
            entry.setMethod(ZipEntry.STORED);
            entry.setCompressedSize(file.length());
            entry.setSize(file.length());
            entry.setCrc(crc.getValue());
            zos.putNextEntry(entry);
            while ((bytesRead = bis.read(buffer)) != -1) {
                zos.write(buffer, 0, bytesRead);
            }
            bis.close();
        } catch (ZipException ex) {
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
