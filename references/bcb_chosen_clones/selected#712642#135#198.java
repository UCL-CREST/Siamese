    private void fnPrintLabelCode128ItemnamePriceUnique(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "filename=label.pdf");
            Integer numberOfLabel = new Integer(req.getParameter("numberOfLabel"));
            String itemCode = req.getParameter("itemCode");
            String itemName = req.getParameter("itemName");
            String itemCurrency = req.getParameter("itemCurrency");
            String itemPrice = req.getParameter("itemPrice");
            BufferedOutputStream outputStream = new BufferedOutputStream(res.getOutputStream());
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfContentByte contentByte = writer.getDirectContent();
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            table.getDefaultCell().setFixedHeight(76);
            String labelString = itemName;
            labelString += "\n" + itemCurrency + " " + itemPrice;
            for (int cnt1 = 0; cnt1 < numberOfLabel.intValue(); cnt1++) {
                try {
                    PdfPTable table2 = new PdfPTable(1);
                    table2.setWidthPercentage(100);
                    table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                    table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    table2.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
                    Barcode128 shipBarCode = new Barcode128();
                    shipBarCode.setX(0.75f);
                    shipBarCode.setN(1.5f);
                    shipBarCode.setChecksumText(true);
                    shipBarCode.setGenerateChecksum(true);
                    shipBarCode.setSize(10f);
                    shipBarCode.setTextAlignment(Element.ALIGN_LEFT);
                    shipBarCode.setBaseline(10f);
                    shipBarCode.setCode(itemCode);
                    shipBarCode.setBarHeight(50f);
                    Image imgShipBarCode = shipBarCode.createImageWithBarcode(contentByte, null, null);
                    PdfPCell shipment = new PdfPCell(new Phrase(new Chunk(imgShipBarCode, 0, 0)));
                    shipment.setFixedHeight(shipBarCode.getBarcodeSize().height() + 16f);
                    shipment.setPaddingTop(5f);
                    shipment.setPaddingBottom(10f);
                    shipment.setBorder(Rectangle.BOX);
                    shipment.setVerticalAlignment(Element.ALIGN_TOP);
                    shipment.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table2.addCell(new Phrase("All IT Hypermarket Sdn Bhd", FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
                    table2.addCell(shipment);
                    table2.addCell(new Phrase(itemName, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
                    table2.addCell(new Phrase(itemCurrency + " " + itemPrice, FontFactory.getFont(FontFactory.COURIER, 15, Font.NORMAL)));
                    PdfPCell theCell = new PdfPCell(table2);
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
