    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        PdfPTable table = createFirstTable();
        document.add(new Paragraph(String.format("Table height before document.add(): %f", table.getTotalHeight())));
        document.add(new Paragraph(String.format("Height of the first row: %f", table.getRowHeight(0))));
        document.add(table);
        document.add(new Paragraph(String.format("Table height after document.add(): %f", table.getTotalHeight())));
        document.add(new Paragraph(String.format("Height of the first row: %f", table.getRowHeight(0))));
        table = createFirstTable();
        document.add(new Paragraph(String.format("Table height before setTotalWidth(): %f", table.getTotalHeight())));
        document.add(new Paragraph(String.format("Height of the first row: %f", table.getRowHeight(0))));
        table.setTotalWidth(50);
        table.setLockedWidth(true);
        document.add(new Paragraph(String.format("Table height after setTotalWidth(): %f", table.getTotalHeight())));
        document.add(new Paragraph(String.format("Height of the first row: %f", table.getRowHeight(0))));
        document.add(table);
        document.close();
    }
