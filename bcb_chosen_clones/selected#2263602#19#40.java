    public static void main(String[] args) {
        System.out.println("Alignment of images");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("alignment.pdf"));
            document.open();
            Image gif = Image.getInstance("vonnegut.gif");
            gif.setAlignment(Image.RIGHT);
            Image jpeg = Image.getInstance("otsoe.jpg");
            jpeg.setAlignment(Image.MIDDLE);
            Image png = Image.getInstance("hitchcock.png");
            png.setAlignment(Image.LEFT);
            document.add(gif);
            document.add(jpeg);
            document.add(png);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
