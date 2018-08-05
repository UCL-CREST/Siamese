    public static void main(String[] args) {
        System.out.println("Chapter 10: example PdfPTableEvents1");
        System.out.println("-> Creates a PDF file with a PdfPTable with table events.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resulting PDF: pdfptable_events.pdf");
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter10/pdfptable_events.pdf"));
            document.open();
            PdfPTable table = new PdfPTable(4);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            for (int k = 0; k < 24; ++k) {
                if (k != 0) table.addCell(String.valueOf(k)); else table.addCell("This is an URL");
            }
            PdfPTableEvents event = new PdfPTableEvents();
            table.setTableEvent(event);
            document.add(table);
            table.setTotalWidth(300);
            table.writeSelectedRows(0, -1, 100, 600, writer.getDirectContent());
            document.newPage();
            table = new PdfPTable(4);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            for (int k = 0; k < 500 * 4; ++k) {
                if (k == 0) {
                    table.getDefaultCell().setColspan(4);
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(new Phrase("This is an URL"));
                    table.getDefaultCell().setColspan(1);
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    k += 3;
                } else table.addCell(new Phrase(String.valueOf(k)));
            }
            table.setTableEvent(event);
            table.setHeaderRows(3);
            document.add(table);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
