    public static void main(String[] args) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapterX/tooltip3.pdf"));
            document.open();
            writer.setPageEvent(new TooltipExample3());
            Paragraph p = new Paragraph("Hello World ");
            Chunk c = new Chunk("tooltip");
            c.setGenericTag("This is my tooltip.");
            p.add(c);
            document.add(p);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
