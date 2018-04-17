    public static void main(String[] args) {
        System.out.println("MultiColumnText Right to Left");
        try {
            Document document = new Document();
            OutputStream out = new FileOutputStream("multicolumnR2L.pdf");
            PdfWriter.getInstance(document, out);
            document.open();
            MultiColumnText mct = new MultiColumnText();
            mct.setColumnsRightToLeft(true);
            mct.addRegularColumns(document.left(), document.right(), 10f, 3);
            for (int i = 0; i < 30; i++) {
                mct.addElement(new Paragraph(String.valueOf(i + 1)));
                mct.addElement(newPara(randomWord(noun), Element.ALIGN_CENTER, Font.BOLDITALIC));
                for (int j = 0; j < 4; j++) {
                    mct.addElement(newPara(poemLine(), Element.ALIGN_LEFT, Font.NORMAL));
                }
                mct.addElement(newPara(randomWord(adverb), Element.ALIGN_LEFT, Font.NORMAL));
                mct.addElement(newPara("\n\n", Element.ALIGN_LEFT, Font.NORMAL));
            }
            document.add(mct);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
