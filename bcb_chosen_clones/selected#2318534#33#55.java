    public static void createPdf(int compressionLevel) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT[compressionLevel + 1]));
            writer.setCompressionLevel(compressionLevel);
            document.open();
            BufferedReader reader = new BufferedReader(new FileReader(RESOURCE));
            String line;
            Paragraph p;
            while ((line = reader.readLine()) != null) {
                p = new Paragraph(line);
                p.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(p);
                document.add(Chunk.NEWLINE);
            }
            reader.close();
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
