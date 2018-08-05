    public void test2() throws Exception {
        SpreadsheetDocument document = new SpreadsheetDocument();
        Sheet sheet = new Sheet("Planilha 1");
        sheet.setLandscape(true);
        Row row = new Row();
        row.setHeight(20);
        sheet.getMerges().add(new IntegerCellMerge(0, 0, 0, 5));
        sheet.getImages().add(new Image(new FileInputStream("D:/image001.jpg"), 0, 0, ImageType.JPEG, 80, 60));
        for (int i = 0; i < 10; i++) {
            Cell cell = new Cell();
            cell.setValue("Celula " + i);
            cell.setBackgroundColor(new Color(192, 192, 192));
            cell.setUnderline(true);
            cell.setBold(true);
            cell.setItalic(true);
            cell.setFont("Times New Roman");
            cell.setFontSize(10);
            cell.setFontColor(new Color(255, 0, 0));
            Border border = new Border();
            border.setWidth(1);
            border.setColor(new Color(0, 0, 0));
            cell.setLeftBorder(border);
            cell.setTopBorder(border);
            cell.setRightBorder(border);
            cell.setBottomBorder(border);
            row.getCells().add(cell);
            sheet.getColumnsWith().put(new Integer(i), new Integer(25));
        }
        sheet.getRows().add(row);
        document.getSheets().add(sheet);
        FileOutputStream fos = new FileOutputStream("D:/teste2.xls");
        SpreadsheetDocumentWriter writer = HSSFSpreadsheetDocumentWriter.getInstance();
        writer.write(document, fos);
        fos.close();
    }
