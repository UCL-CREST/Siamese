    public void convert(CSVReader reader, Writer writer, int nbTotalRows) throws IOException, InterruptedException {
        Validate.notNull(reader, "CSVReader");
        Validate.notNull(writer, "Writer");
        Writer bufferedWriter = new BufferedWriter(writer);
        File fileForColsDef = createTempFileForCss();
        BufferedWriter colsDefWriter = new BufferedWriter(new FileWriter(fileForColsDef));
        File fileForTable = createTempFileForTable();
        BufferedWriter tableWriter = new BufferedWriter(new FileWriter(fileForTable));
        try {
            int currentRow = 0;
            String[] nextLine = reader.readNext();
            if (nextLine != null) {
                int[] colsCharCount = new int[nextLine.length];
                writeTableRowHeader(tableWriter, nextLine);
                while ((nextLine = reader.readNext()) != null) {
                    currentRow++;
                    if (progress != null) {
                        float percent = ((float) currentRow / (float) nbTotalRows) * 100f;
                        progress.updateProgress(ConvertionStepEnum.PROCESSING_ROWS, percent);
                    }
                    writeTableRow(tableWriter, nextLine, colsCharCount);
                }
                writeTableStart(colsDefWriter, colsCharCount);
                writeColsDefinitions(colsDefWriter, colsCharCount);
            }
            writeConverterInfos(bufferedWriter);
            writeTableEnd(tableWriter);
            flushAndClose(tableWriter);
            flushAndClose(colsDefWriter);
            BufferedReader colsDefReader = new BufferedReader(new FileReader(fileForColsDef));
            BufferedReader tableReader = new BufferedReader(new FileReader(fileForTable));
            mergeFiles(bufferedWriter, colsDefReader, tableReader);
        } finally {
            closeQuietly(tableWriter);
            closeQuietly(colsDefWriter);
            fileForTable.delete();
            fileForColsDef.delete();
        }
    }
