    public File writeSlicesToFile(File saveDirectory, String chromosome, int beginningBP, int endingBP) {
        ArrayList<ZipEntry> entries = fetchZipEntries(chromosome, beginningBP, endingBP);
        if (entries == null) return null;
        entries.add(0, archiveReadMeEntry);
        File slicedZipArchive = new File(saveDirectory, "USeqDataSlice_" + createRandowWord(7) + "." + USeqUtilities.USEQ_EXTENSION_NO_PERIOD);
        ZipOutputStream out = null;
        BufferedInputStream is = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(slicedZipArchive));
            int count;
            byte data[] = new byte[2048];
            int numEntries = entries.size();
            for (int i = 0; i < numEntries; i++) {
                ZipEntry entry = entries.get(i);
                out.putNextEntry(entry);
                is = new BufferedInputStream(zipArchive.getInputStream(entry));
                while ((count = is.read(data, 0, 2048)) != -1) out.write(data, 0, count);
                out.closeEntry();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            USeqUtilities.safeClose(out);
            USeqUtilities.safeClose(is);
        }
        return slicedZipArchive;
    }
