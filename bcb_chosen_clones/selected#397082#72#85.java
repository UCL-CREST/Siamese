    public void trimAndWriteNewSff(OutputStream out) throws IOException {
        TrimParser trimmer = new TrimParser();
        SffParser.parseSFF(untrimmedSffFile, trimmer);
        tempOut.close();
        headerBuilder.withNoIndex().numberOfReads(numberOfTrimmedReads);
        SffWriter.writeCommonHeader(headerBuilder.build(), out);
        InputStream in = null;
        try {
            in = new FileInputStream(tempReadDataFile);
            IOUtils.copyLarge(in, out);
        } finally {
            IOUtil.closeAndIgnoreErrors(in);
        }
    }
