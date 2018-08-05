    public void execute() {
        try {
            String line = null;
            Document document;
            Font f;
            Rectangle pagesize = (Rectangle) getValue("pagesize");
            if ("LANDSCAPE".equals(getValue("orientation"))) {
                f = FontFactory.getFont(FontFactory.COURIER, 10);
                document = new Document(pagesize.rotate(), 36, 9, 36, 36);
            } else {
                f = FontFactory.getFont(FontFactory.COURIER, 11);
                document = new Document(pagesize, 72, 36, 36, 36);
            }
            BufferedReader in = new BufferedReader(new FileReader((File) getValue("srcfile")));
            PdfWriter.getInstance(document, new FileOutputStream((File) getValue("destfile")));
            document.open();
            while ((line = in.readLine()) != null) {
                document.add(new Paragraph(12, line, f));
            }
            document.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(internalFrame, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            System.err.println(e.getMessage());
        }
    }
