    public static void main(String[] args) {
        System.out.println("Chapter 1 example 8: pause() and resume()");
        Document document = new Document();
        try {
            PdfWriter writerA = PdfWriter.getInstance(document, new FileOutputStream("Chap0108a.pdf"));
            PdfWriter writerB = PdfWriter.getInstance(document, new FileOutputStream("Chap0108b.pdf"));
            writerB.pause();
            try {
                Watermark watermark = new Watermark(Image.getInstance("watermark.jpg"), 200, 420);
                document.add(watermark);
            } catch (Exception e) {
                System.err.println("Are you sure you have the file 'watermark.jpg' in the right path?");
            }
            writerB.resume();
            HeaderFooter header = new HeaderFooter(new Phrase("This is a header"), false);
            document.setHeader(header);
            document.open();
            document.setPageSize(PageSize.A4.rotate());
            try {
                Watermark watermark = new Watermark(Image.getInstance("watermark.jpg"), 320, 200);
                document.add(watermark);
            } catch (Exception e) {
                System.err.println("Are you sure you have the file 'watermark.jpg' in the right path?");
            }
            HeaderFooter footer = new HeaderFooter(new Phrase("This is page: "), true);
            document.setFooter(footer);
            document.add(new Paragraph("Hello World"));
            document.newPage();
            document.add(new Paragraph("Hello Earth"));
            writerA.pause();
            document.resetHeader();
            writerA.resume();
            document.newPage();
            document.add(new Paragraph("Hello Sun"));
            writerA.pause();
            document.add(new Paragraph("Remark: the header has vanished!"));
            writerA.resume();
            writerB.pause();
            document.resetPageCount();
            writerB.resume();
            document.newPage();
            document.add(new Paragraph("Hello Moon"));
            writerB.pause();
            document.add(new Paragraph("Remark: the pagenumber has been reset!"));
            writerB.resume();
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
