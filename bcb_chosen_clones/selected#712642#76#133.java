    private void fnPrintLabelCode39ItemnamePriceUnique(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "filename=label.pdf");
            Integer numberOfLabel = new Integer(req.getParameter("numberOfLabel"));
            String itemCode = req.getParameter("itemCode");
            String itemName = req.getParameter("itemName");
            String itemCurrency = req.getParameter("itemCurrency");
            String itemPrice = req.getParameter("itemPrice");
            BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
            Rectangle pageSize = new Rectangle(400, 200);
            Document document = new Document(pageSize, 0, 0, 0, 0);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            table.getDefaultCell().setFixedHeight(90);
            String labelString = itemName;
            labelString += "\n" + itemCurrency + " " + itemPrice;
            for (int cnt1 = 0; cnt1 < numberOfLabel.intValue(); cnt1++) {
                try {
                    PdfPTable table2 = new PdfPTable(1);
                    table2.setWidthPercentage(100);
                    table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                    table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
                    Barcode39 code39 = new Barcode39();
                    code39.setCode(itemCode);
                    code39.setStartStopText(false);
                    Image image39 = code39.createImageWithBarcode(contentByte, null, null);
                    PdfPCell shipment = new PdfPCell(new Phrase(new Chunk(image39, 0, 0)));
                    shipment.setFixedHeight(code39.getBarcodeSize().height() + 2f);
                    shipment.setPaddingTop(1f);
                    shipment.setPaddingBottom(1f);
                    shipment.setVerticalAlignment(Element.ALIGN_TOP);
                    shipment.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table2.addCell(new Phrase("All IT Hypermarket Sdn Bhd", FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
                    table2.addCell(shipment);
                    table2.addCell(new Phrase(itemName, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
                    table2.addCell(new Phrase(itemCurrency + " " + itemPrice, FontFactory.getFont(FontFactory.COURIER, 12, Font.NORMAL)));
                    PdfPCell theCell = new PdfPCell(table2);
                    theCell.setBorder(Rectangle.BOX);
                    table.addCell(theCell);
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
