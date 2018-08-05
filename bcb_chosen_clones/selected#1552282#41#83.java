    public static void main(String[] args) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            texts[3] = convertCid(texts[0]);
            texts[4] = convertCid(texts[1]);
            texts[5] = convertCid(texts[2]);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("vertical.pdf"));
            int idx = 0;
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            for (int j = 0; j < 2; ++j) {
                BaseFont bf = BaseFont.createFont("KozMinPro-Regular", encs[j], false);
                cb.setRGBColorStroke(255, 0, 0);
                cb.setLineWidth(0);
                float x = 400;
                float y = 700;
                float height = 400;
                float leading = 30;
                int maxLines = 6;
                for (int k = 0; k < maxLines; ++k) {
                    cb.moveTo(x - k * leading, y);
                    cb.lineTo(x - k * leading, y - height);
                }
                cb.rectangle(x, y, -leading * (maxLines - 1), -height);
                cb.stroke();
                int status;
                VerticalText vt = new VerticalText(cb);
                vt.setVerticalLayout(x, y, height, maxLines, leading);
                vt.addText(new Chunk(texts[idx++], new Font(bf, 20)));
                vt.addText(new Chunk(texts[idx++], new Font(bf, 20, 0, Color.blue)));
                status = vt.go();
                System.out.println(status);
                vt.setAlignment(Element.ALIGN_RIGHT);
                vt.addText(new Chunk(texts[idx++], new Font(bf, 20, 0, Color.orange)));
                status = vt.go();
                System.out.println(status);
                document.newPage();
            }
            document.close();
        } catch (Exception de) {
            de.printStackTrace();
        }
    }
