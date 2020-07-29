    public static void main(String[] args) {
        System.out.println("Chapter 6: example PdfPTableCellAlignment");
        System.out.println("-> Creates a PDF file with a PdfPTable.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resulting PDF: pdfptable_cell_alignment.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter06/pdfptable_cell_alignment.pdf"));
            document.open();
            PdfPTable table = new PdfPTable(2);
            PdfPCell cell;
            Paragraph p = new Paragraph("Quick brown fox jumps over the lazy dog. Quick brown fox jumps over the lazy dog.");
            table.addCell("default alignment");
            cell = new PdfPCell(p);
            table.addCell(cell);
            table.addCell("centered alignment");
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            table.addCell("right alignment");
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell("justified alignment");
            cell = new PdfPCell(p);
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            table.addCell(cell);
            table.addCell("paragraph alignment");
            Paragraph p1 = new Paragraph("Quick brown fox");
            Paragraph p2 = new Paragraph("jumps over");
            p2.setAlignment(Element.ALIGN_CENTER);
            Paragraph p3 = new Paragraph("the lazy dog.");
            p3.setAlignment(Element.ALIGN_RIGHT);
            cell = new PdfPCell();
            cell.addElement(p1);
            cell.addElement(p2);
            cell.addElement(p3);
            table.addCell(cell);
            table.addCell("extra indentation (cell)");
            cell = new PdfPCell(p);
            cell.setIndent(20);
            table.addCell(cell);
            table.addCell("extra indentation (paragraph)");
            p.setFirstLineIndent(10);
            cell = new PdfPCell();
            cell.addElement(p);
            table.addCell(cell);
            table.addCell("blah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\n");
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
            table.addCell("bottom");
            table.addCell("blah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\n");
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell("middle");
            table.addCell("blah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\nblah\n");
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            table.addCell("top");
            document.add(table);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
