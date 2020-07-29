    public static void main(String[] args) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();
            Paragraph separator = new Paragraph(0);
            separator.add(new Chunk(new LineSeparator(1, 80, Color.RED, Element.ALIGN_LEFT, -2)));
            ColumnText column = new ColumnText(writer.getDirectContent());
            for (int i = 0; i < 5; i++) {
                column.addElement(StarSeparators.TEXT);
                column.addElement(separator);
            }
            column.setSimpleColumn(36, 36, 295, 806);
            column.go();
            column.setSimpleColumn(300, 36, 559, 806);
            column.go();
            document.newPage();
            for (int i = 0; i < 10; i++) {
                document.add(StarSeparators.TEXT);
                document.add(separator);
            }
            document.close();
        } catch (Exception de) {
            de.printStackTrace();
        }
    }
