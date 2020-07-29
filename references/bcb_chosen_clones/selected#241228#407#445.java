    public void writeJar(JarOutputStream out) throws IOException, Pack200Exception {
        String[] fileName = fileBands.getFileName();
        int[] fileModtime = fileBands.getFileModtime();
        long[] fileSize = fileBands.getFileSize();
        byte[][] fileBits = fileBands.getFileBits();
        int classNum = 0;
        int numberOfFiles = header.getNumberOfFiles();
        long archiveModtime = header.getArchiveModtime();
        for (int i = 0; i < numberOfFiles; i++) {
            String name = fileName[i];
            long modtime = 1000 * (archiveModtime + fileModtime[i]);
            boolean deflate = fileDeflate[i];
            JarEntry entry = new JarEntry(name);
            if (deflate) {
                entry.setMethod(ZipEntry.DEFLATED);
            } else {
                entry.setMethod(ZipEntry.STORED);
                CRC32 crc = new CRC32();
                if (fileIsClass[i]) {
                    crc.update(classFilesContents[classNum]);
                    entry.setSize(classFilesContents[classNum].length);
                } else {
                    crc.update(fileBits[i]);
                    entry.setSize(fileSize[i]);
                }
                entry.setCrc(crc.getValue());
            }
            entry.setTime(modtime - TimeZone.getDefault().getRawOffset());
            out.putNextEntry(entry);
            if (fileIsClass[i]) {
                entry.setSize(classFilesContents[classNum].length);
                out.write(classFilesContents[classNum]);
                classNum++;
            } else {
                entry.setSize(fileSize[i]);
                out.write(fileBits[i]);
            }
        }
    }
