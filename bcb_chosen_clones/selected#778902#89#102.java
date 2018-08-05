    public static void createPdf(String filename, BaseFont bf) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            Font font = new Font(bf, 12);
            document.add(new Paragraph("abcdefghijklmnopqrstuvwxyz", font));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
