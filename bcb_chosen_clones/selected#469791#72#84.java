    public void _testConvertIntoOneFile() {
        File csvFile = new File("C:/DE311/solution_workspace/WorkbookTaglib/WorkbookTagDemoWebapp/src/main/resources/csv/google.csv");
        try {
            Charset guessedCharset = com.glaforge.i18n.io.CharsetToolkit.guessEncoding(csvFile, 4096);
            CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), guessedCharset)));
            Writer writer = new FileWriter("/temp/test.html");
            int nbLines = CsvConverterUtils.countLines(new BufferedReader(new FileReader(csvFile)));
            HtmlConverter conv = new HtmlConverter();
            conv.convert(reader, writer, nbLines);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
