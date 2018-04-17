    public static SimpleDataTable loadDataFromFile(URL urlMetadata, URL urlData) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(urlMetadata.openStream()));
        List<String> columnNamesList = new ArrayList<String>();
        String[] lineParts = null;
        String line;
        in.readLine();
        while ((line = in.readLine()) != null) {
            lineParts = line.split(",");
            columnNamesList.add(lineParts[0]);
        }
        String[] columnNamesArray = new String[columnNamesList.size()];
        int index = 0;
        for (String s : columnNamesList) {
            columnNamesArray[index] = s;
            index++;
        }
        SimpleDataTable table = new SimpleDataTable("tabulka s daty", columnNamesArray);
        in = new BufferedReader(new InputStreamReader(urlData.openStream()));
        lineParts = null;
        line = null;
        SimpleDataTableRow tableRow;
        double[] rowData;
        while ((line = in.readLine()) != null) {
            lineParts = line.split(",");
            rowData = new double[columnNamesList.size()];
            for (int i = 0; i < columnNamesArray.length; i++) {
                rowData[i] = Double.parseDouble(lineParts[i + 1]);
            }
            tableRow = new SimpleDataTableRow(rowData, lineParts[0]);
            table.add(tableRow);
        }
        return table;
    }
