    public static void main(String[] args) throws DocumentException, MalformedURLException, IOException {
        Document document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\ITextTest3.pdf"));
        writer.setStrictImageSequence(true);
        document.open();
        MultiColumnText mct = new MultiColumnText();
        mct.addRegularColumns(document.left(), document.right(), 10f, 2);
        Image jpg = Image.getInstance("c:\\lh.jpg");
        jpg.setAlignment(Image.LEFT);
        mct.addElement(jpg);
        Font theFont = createChineseFont(13, Font.NORMAL, true);
        Paragraph p1 = new Paragraph("第六期", theFont);
        p1.setAlignment(Element.ALIGN_CENTER);
        mct.addElement(p1);
        Paragraph pt = new Paragraph("\n", theFont);
        pt.setAlignment(Element.ALIGN_CENTER);
        mct.addElement(pt);
        Paragraph p2 = new Paragraph("中油辽河油田公司勘查处                    二零零八年二月二十二日", theFont);
        p2.setAlignment(Element.ALIGN_LEFT);
        mct.addElement(p2);
        mct.addElement(pt);
        PdfContentByte cb = writer.getDirectContent();
        cb.setLineWidth(1);
        cb.setColorStroke(Color.RED);
        cb.moveTo(document.left(), document.top() - 150);
        cb.lineTo((document.right() - document.left()) / 2, document.top() - 150);
        cb.stroke();
        for (int i = 0; i < 30; i++) {
            mct.addElement(new Paragraph(String.valueOf(i + 1)));
            mct.addElement(newPara(randomWord(noun), Element.ALIGN_CENTER, Font.BOLDITALIC));
            for (int j = 0; j < 4; j++) {
                mct.addElement(newPara(poemLine(), Element.ALIGN_LEFT, Font.NORMAL));
            }
            mct.addElement(newPara(randomWord(adverb), Element.ALIGN_LEFT, Font.NORMAL));
            mct.addElement(newPara("\n\n", Element.ALIGN_LEFT, Font.NORMAL));
        }
        mct.addElement(Chunk.NEWLINE);
        mct.addElement(Chunk.NEWLINE);
        mct.addElement(Chunk.NEWLINE);
        mct.addElement(Chunk.NEWLINE);
        Image zhang = Image.getInstance("c:\\zhang.jpg");
        zhang.setAlignment(Image.UNDERLYING);
        Chunk cc = new Chunk(zhang, 0, -5);
        mct.addElement(cc);
        Phrase qianzhang = new Phrase("二零零八年二月二十二日", theFont);
        mct.addElement(qianzhang);
        document.add(mct);
        document.add(Chunk.NEWLINE);
        Image jpg2 = Image.getInstance("c:\\mgjt.jpg");
        jpg2.setAlignment(Image.ALIGN_CENTER);
        document.add(jpg2);
        document.add(Chunk.NEXTPAGE);
        Font theFontSign = createChineseFont(10, Font.NORMAL, false);
        Phrase ps1 = new Phrase("\n\t                                               编图", theFontSign);
        Image ss1 = Image.getInstance("c:\\ss1.jpg");
        ss1.setAlignment(Image.ALIGN_CENTER);
        Chunk ck = new Chunk(ss1, 0, -5);
        ps1.add(ck);
        ps1.add("        制表：");
        ps1.add(ck);
        document.add(ps1);
        document.close();
        System.out.println("Out Finish!");
    }
