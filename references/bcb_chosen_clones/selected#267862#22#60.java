    public static void main(String[] args) {
        System.out.println("Split rows");
        Document document1 = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
        Document document2 = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
        Document document3 = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
        try {
            PdfWriter.getInstance(document1, new FileOutputStream("SplitRowsBetween.pdf"));
            PdfWriter.getInstance(document2, new FileOutputStream("SplitRowsWithin.pdf"));
            PdfWriter.getInstance(document3, new FileOutputStream("OmitRows.pdf"));
            document1.open();
            document2.open();
            document3.open();
            String text = "Quick brown fox jumps over the lazy dog. ";
            for (int i = 0; i < 5; i++) text += text;
            PdfPTable table = new PdfPTable(2);
            PdfPCell largeCell;
            Phrase phrase;
            for (int i = 0; i < 10; i++) {
                phrase = new Phrase(text);
                for (int j = 0; j < i; j++) {
                    phrase.add(new Phrase(text));
                }
                if (i == 7) phrase = new Phrase(text);
                table.addCell(String.valueOf(i));
                largeCell = new PdfPCell(phrase);
                table.addCell(largeCell);
            }
            document1.add(table);
            table.setSplitLate(false);
            document2.add(table);
            table.setSplitRows(false);
            document3.add(table);
        } catch (Exception de) {
            de.printStackTrace();
        }
        document1.close();
        document2.close();
        document3.close();
    }
