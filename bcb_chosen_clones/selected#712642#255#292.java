    private void fnPrintLabelItemcodeEanSerialUnique(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "filename=label.pdf");
            Integer numberOfLabel = new Integer(req.getParameter("numberOfLabel"));
            String itemCode = req.getParameter("itemCode");
            String itemEAN = req.getParameter("itemEAN");
            String itemSerial = req.getParameter("itemSerial");
            BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            table.getDefaultCell().setFixedHeight(76);
            String labelString = "ITEMCODE:" + itemCode;
            labelString += "\nEAN/UPC:" + itemEAN;
            if (itemSerial != null && itemSerial.length() > 3) {
                labelString += "\nSN#:" + itemSerial;
            }
            for (int cnt1 = 0; cnt1 < numberOfLabel.intValue(); cnt1++) {
                try {
                    PdfPCell theCell = new PdfPCell();
                    table.addCell(new Phrase(labelString, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            document.add(table);
            document.close();
            outputStream.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
