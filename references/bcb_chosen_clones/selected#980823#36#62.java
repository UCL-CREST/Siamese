    public static void main(String[] args) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();
            Paragraph stars = new Paragraph(20);
            stars.add(new Chunk(new StarSeparators()));
            stars.setSpacingAfter(30);
            ColumnText column = new ColumnText(writer.getDirectContent());
            for (int i = 0; i < 5; i++) {
                column.addElement(TEXT);
                column.addElement(stars);
            }
            column.setSimpleColumn(36, 36, 295, 806);
            column.go();
            column.setSimpleColumn(300, 36, 559, 806);
            column.go();
            document.newPage();
            for (int i = 0; i < 50; i++) {
                document.add(TEXT);
                document.add(stars);
            }
            document.close();
        } catch (Exception de) {
            de.printStackTrace();
        }
    }
