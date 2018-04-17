    public static void main(String[] args) {
        System.out.println("Chapter 6 example 8: Rotating an Image");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0608.pdf"));
            document.open();
            Image jpg = Image.getInstance("myKids.jpg");
            jpg.setAlignment(Image.MIDDLE);
            jpg.setRotation(Math.PI / 6);
            document.add(new Paragraph("rotate 30 degrees"));
            document.add(jpg);
            document.newPage();
            jpg.setRotation(Math.PI / 4);
            document.add(new Paragraph("rotate 45 degrees"));
            document.add(jpg);
            document.newPage();
            jpg.setRotation(Math.PI / 2);
            document.add(new Paragraph("rotate pi/2 radians"));
            document.add(jpg);
            document.newPage();
            jpg.setRotation(Math.PI * 0.75);
            document.add(new Paragraph("rotate 135 degrees"));
            document.add(jpg);
            document.newPage();
            jpg.setRotation(Math.PI);
            document.add(new Paragraph("rotate pi radians"));
            document.add(jpg);
            document.newPage();
            jpg.setRotation(2.0 * Math.PI);
            document.add(new Paragraph("rotate 2 x pi radians"));
            document.add(jpg);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
