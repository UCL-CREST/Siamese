    public static synchronized void repartition(File[] sourceFiles, File targetDirectory, String prefix, long maxUnitBases, long maxUnitEntries) throws Exception {
        if (!targetDirectory.exists()) {
            if (!targetDirectory.mkdirs()) throw new Exception("Could not create directory " + targetDirectory.getAbsolutePath());
        }
        File tmpFile = new File(targetDirectory, "tmp.fasta");
        FileOutputStream fos = new FileOutputStream(tmpFile);
        FileChannel fco = fos.getChannel();
        for (File file : sourceFiles) {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fci = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(64000);
            while (fci.read(buffer) > 0) {
                buffer.flip();
                fco.write(buffer);
                buffer.clear();
            }
            fci.close();
        }
        fco.close();
        FastaFile fastaFile = new FastaFile(tmpFile);
        fastaFile.split(targetDirectory, prefix, maxUnitBases, maxUnitEntries);
        tmpFile.delete();
    }
