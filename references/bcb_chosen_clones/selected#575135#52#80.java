    static void cleanFile(File file) {
        final Counter cnt = new Counter();
        final File out = new File(FileUtils.appendToFileName(file.getAbsolutePath(), ".cleaned"));
        final SAMFileReader reader = new SAMFileReader(file);
        final SAMRecordIterator it = reader.iterator();
        final SAMFileWriter writer = new SAMFileWriterFactory().makeSAMOrBAMWriter(reader.getFileHeader(), true, out);
        if (!it.hasNext()) return;
        log.info("Cleaning file " + file + " to " + out.getName());
        SAMRecord last = it.next();
        writer.addAlignment(last);
        while (it.hasNext()) {
            final SAMRecord now = it.next();
            final int start1 = last.getAlignmentStart();
            final int start2 = now.getAlignmentStart();
            final int end1 = last.getAlignmentEnd();
            final int end2 = now.getAlignmentEnd();
            if (start1 == start2 && end1 == end2) {
                log.debug("Discarding record " + now.toString());
                cnt.count();
                continue;
            }
            writer.addAlignment(now);
            last = now;
        }
        writer.close();
        reader.close();
        log.info(file + " done, discarded " + cnt.getCount() + " reads");
        exe.shutdown();
    }
