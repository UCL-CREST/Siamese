    public static void main(String[] args) {
        System.out.println("Chapter 4: example FoxDogRender");
        System.out.println("-> Creates a PDF file with the text");
        System.out.println("   'Quick brown fox jumps over the lazy dog';");
        System.out.println("   the text is rendered in different ways.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resulting PDF: fox_dog_render.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter04/fox_dog_render.pdf"));
            document.open();
            Font font = new Font(Font.COURIER, 20);
            Chunk chunk = new Chunk("Quick brown fox jumps over the lazy dog.", font);
            chunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL, 0f, new Color(0xFF, 0x00, 0x00));
            document.add(new Paragraph(chunk));
            chunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 0.3f, new Color(0xFF, 0x00, 0x00));
            document.add(new Paragraph(chunk));
            chunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE, 0f, new Color(0x00, 0xFF, 0x00));
            document.add(new Paragraph(chunk));
            chunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_STROKE, 0.3f, new Color(0x00, 0x00, 0xFF));
            document.add(new Paragraph(chunk));
            document.add(Chunk.NEWLINE);
            Chunk bold = new Chunk("This looks like Font.BOLD");
            bold.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 0.5f, null);
            document.add(bold);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
