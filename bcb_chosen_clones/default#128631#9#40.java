    public static void main(String[] args) {
        System.out.println("Chapter 6 example 9: bytes() / Raw Image");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0609.pdf"));
            document.open();
            RandomAccessFile rf = new RandomAccessFile("myKids.jpg", "r");
            int size = (int) rf.length();
            byte imext[] = new byte[size];
            rf.readFully(imext);
            rf.close();
            Image img1 = Image.getInstance(imext);
            img1.setAbsolutePosition(50, 500);
            document.add(img1);
            byte data[] = new byte[100 * 100 * 3];
            for (int k = 0; k < 100; ++k) {
                for (int j = 0; j < 300; j += 3) {
                    data[k * 300 + j] = (byte) (255 * Math.sin(j * .5 * Math.PI / 300));
                    data[k * 300 + j + 1] = (byte) (256 - j * 256 / 300);
                    data[k * 300 + j + 2] = (byte) (255 * Math.cos(k * .5 * Math.PI / 100));
                }
            }
            Image img2 = Image.getInstance(100, 100, 3, 8, data);
            img2.setAbsolutePosition(200, 200);
            document.add(img2);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
