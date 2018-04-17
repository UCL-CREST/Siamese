    public boolean writeSlicesToStream(OutputStream outputStream, String chromosome, int beginningBP, int endingBP, boolean closeStream) {
        ArrayList<ZipEntry> entries = fetchZipEntries(chromosome, beginningBP, endingBP);
        if (entries == null) return false;
        entries.add(0, archiveReadMeEntry);
        ZipOutputStream out = new ZipOutputStream(outputStream);
        DataOutputStream dos = new DataOutputStream(out);
        BufferedInputStream bis = null;
        try {
            int count;
            byte data[] = new byte[2048];
            int numEntries = entries.size();
            SliceInfo sliceInfo = null;
            for (int i = 0; i < numEntries; i++) {
                ZipEntry entry = entries.get(i);
                bis = new BufferedInputStream(zipArchive.getInputStream(entry));
                if (i != 0) sliceInfo = new SliceInfo(entry.getName());
                if (i == 0 || sliceInfo.isContainedBy(beginningBP, endingBP)) {
                    out.putNextEntry(entry);
                    while ((count = bis.read(data, 0, 2048)) != -1) out.write(data, 0, count);
                    out.closeEntry();
                } else sliceAndWriteEntry(beginningBP, endingBP, sliceInfo, bis, out, dos);
                bis.close();
            }
            if (closeStream) {
                out.close();
                outputStream.close();
                dos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(out);
            USeqUtilities.safeClose(outputStream);
            USeqUtilities.safeClose(bis);
            USeqUtilities.safeClose(dos);
            return false;
        }
        return true;
    }
