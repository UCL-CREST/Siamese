    public static void importFromCSV(File csvFile, String tableName, boolean hasColumnNames) throws FileNotFoundException {
        final Scanner scanner = new Scanner(csvFile);
        final List<String> columns = new ArrayList<String>();
        int lineNumber = 1;
        final String columnLine = scanner.nextLine() + "\n";
        final Scanner columnScanner = new Scanner(columnLine);
        columnScanner.useDelimiter(Pattern.compile(",|\n"));
        if (hasColumnNames) {
            while (columnScanner.hasNext()) {
                columns.add(columnScanner.next().trim());
            }
            lineNumber++;
        } else {
            int columnIndex = 0;
            while (columnScanner.hasNext()) {
                columnScanner.next();
                columns.add("column" + columnIndex++);
            }
        }
        columnScanner.close();
        for (int i = 0; i < columns.size(); i++) {
            System.out.println("column" + i + ": \"" + columns.get(i) + "\"");
        }
        while (scanner.hasNext()) {
            String recordLine = null;
            if (lineNumber == 1 && !hasColumnNames) {
                recordLine = columnLine;
            } else {
                recordLine = scanner.nextLine() + "\n";
            }
            lineNumber++;
            final Scanner recordScanner = new Scanner(recordLine);
            recordScanner.useDelimiter(Pattern.compile(",|\n"));
            int columnCount = 0;
            while (recordScanner.hasNext()) {
                System.out.print("\"");
                System.out.print(recordScanner.next().trim());
                System.out.print("\"");
                System.out.print(" ");
                columnCount++;
            }
            recordScanner.close();
            if (columnCount != columns.size()) {
                System.out.print("<<< unmatched record at line " + lineNumber);
            }
            System.out.println();
        }
    }
