    private ParserFileReader createParserFileReader(final FromNetRecord record) throws IOException {
        final String strUrl = record.getStrUrl();
        ParserFileReader parserFileReader;
        try {
            parserFileReader = parserFileReaderFactory.create(strUrl);
        } catch (Exception exception) {
            _log.error("can not create reader for \"" + strUrl + "\"", exception);
            parserFileReader = null;
        }
        url = parserFileReaderFactory.getUrl();
        if (parserFileReader != null) {
            parserFileReader.mark();
            final String outFileName = urlToFile("runtime/tests", url, "");
            final File outFile = new File(outFileName);
            outFile.getParentFile().mkdirs();
            final Writer writer = new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8");
            int readed;
            while ((readed = parserFileReader.read()) != -1) {
                writer.write(readed);
            }
            writer.close();
            parserFileReader.reset();
        }
        return parserFileReader;
    }
