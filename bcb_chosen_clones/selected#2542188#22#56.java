    public static void main(String[] args) {
        System.out.println("Height");
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("CellHeights.pdf"));
            document.open();
            PdfPTable table = new PdfPTable(2);
            table.setExtendLastRow(true);
            PdfPCell cell;
            cell = new PdfPCell(new Paragraph("blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah blah"));
            table.addCell("wrap");
            cell.setNoWrap(false);
            table.addCell(cell);
            table.addCell("no wrap");
            cell.setNoWrap(true);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("1. blah blah\n2. blah blah blah\n3. blah blah\n4. blah blah blah\n5. blah blah\n6. blah blah blah\n7. blah blah\n8. blah blah blah"));
            table.addCell("height");
            table.addCell(cell);
            table.addCell("fixed height");
            cell.setFixedHeight(50f);
            table.addCell(cell);
            table.addCell("minimum height");
            cell = new PdfPCell(new Paragraph("x"));
            cell.setMinimumHeight(50f);
            table.addCell(cell);
            table.addCell("extend last row");
            cell = new PdfPCell(new Paragraph("almost no content, but the row is extended"));
            table.addCell(cell);
            document.add(table);
        } catch (Exception de) {
            de.printStackTrace();
        }
        document.close();
    }
