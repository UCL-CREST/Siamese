    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(FileChooserDemo.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                Document document = new Document();
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(file.getName()));
                    document.open();
                    PdfWriter writer = null;
                    PdfReader reader = new PdfReader("hello.pdf");
                    PdfImportedPage page1 = writer.getImportedPage(reader, 1);
                    document.add(new Paragraph("Hello World"));
                } catch (Exception ee) {
                }
                document.close();
                log.append("Opening: " + file.getName() + "." + newline);
            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        } else if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(FileChooserDemo.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                log.append("Saving: " + file.getName() + "." + newline);
            } else {
                log.append("Save command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
    }
