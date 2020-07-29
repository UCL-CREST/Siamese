        private void archiveFile() throws IOException {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            FileUtils.copyFile(sourceFile, buf);
            ZipEntry e = new ZipEntry(zipEntryPath);
            if (keepFileModificationTimes) {
                e.setTime(sourceFile.lastModified());
            }
            synchronized (out) {
                out.putNextEntry(e);
                buf.writeTo(out);
                out.closeEntry();
            }
        }
