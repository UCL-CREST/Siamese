    public static void main(String[] args) {
        FloatingBoxes floatingBoxes = new FloatingBoxes();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("FloatingBoxes.pdf"));
            document.open();
            PdfPTable table = new PdfPTable(2);
            table.setTableEvent(floatingBoxes);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setCellEvent(floatingBoxes);
            table.getDefaultCell().setPadding(5f);
            table.addCell("value");
            table.addCell("name");
            table.addCell(new Paragraph("dog"));
            table.addCell(new Paragraph("cat"));
            table.addCell(new Paragraph("bird"));
            table.addCell(new Paragraph("horse"));
            document.add(table);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
