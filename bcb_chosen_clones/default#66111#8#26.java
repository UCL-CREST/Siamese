    public static void main(String[] args) {
        System.out.println("Chapter 10 example 1: Simple Graphic");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap1001.pdf"));
            document.open();
            Graphic grx = new Graphic();
            grx.rectangle(100, 700, 100, 100);
            grx.moveTo(100, 700);
            grx.lineTo(200, 800);
            grx.stroke();
            document.add(grx);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
