    protected int doWork() {
        SAMFileReader reader = new SAMFileReader(IoUtil.openFileForReading(INPUT));
        reader.getFileHeader().setSortOrder(SORT_ORDER);
        SAMFileWriter writer = new SAMFileWriterFactory().makeSAMOrBAMWriter(reader.getFileHeader(), false, OUTPUT);
        Iterator<SAMRecord> iterator = reader.iterator();
        while (iterator.hasNext()) writer.addAlignment(iterator.next());
        reader.close();
        writer.close();
        return 0;
    }
