    private void fnPrintLabelCode128ItemnamePriceByGRN(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Log.printVerbose("printing label by grn...2");
        try {
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "filename=label.pdf");
            String strGRN = req.getParameter("grnId");
            Log.printVerbose("... GRN NO:" + strGRN);
            GoodsReceivedNoteObject grnObj = GoodsReceivedNoteNut.getObject(new Long(strGRN));
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
            for (int cnt1 = 0; cnt1 < grnObj.vecGRNItems.size(); cnt1++) {
                try {
                    GoodsReceivedNoteItemObject grnItmObj = (GoodsReceivedNoteItemObject) grnObj.vecGRNItems.get(cnt1);
                    ItemObject itemObj = ItemNut.getObject(grnItmObj.mItemId);
                    Log.printVerbose(" ITEM " + cnt1 + 1 + "... inside GRN..");
                    Log.printVerbose(" ITEM CODE/EAN:" + itemObj.code + " , " + itemObj.eanCode);
                    String labelString = "ITEMCODE:" + itemObj.code;
                    labelString += "\nEAN/UPC:" + itemObj.eanCode;
                    PdfPCell theCell = new PdfPCell();
                    if (itemObj.serialized == false) {
                        Integer nQty = new Integer(CurrencyFormat.strInt(grnItmObj.mTotalQty));
                        for (int cnt2 = 0; cnt2 < nQty.intValue(); cnt2++) {
                            table.addCell(new Phrase(labelString, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
                        }
                    } else {
                        QueryObject query = new QueryObject(new String[] { SerialNumberDeltaBean.DOC_KEY + " = '" + grnItmObj.mPkid.toString() + "' ", SerialNumberDeltaBean.DOC_TABLE + " = '" + GoodsReceivedNoteItemBean.TABLENAME + "' " });
                        query.setOrder(" ORDER BY " + SerialNumberDeltaBean.SERIAL + " ");
                        Vector vecSN = new Vector(SerialNumberDeltaNut.getObjects(query));
                        for (int cnt5 = 0; cnt5 < vecSN.size(); cnt5++) {
                            SerialNumberDeltaObject sndObj = (SerialNumberDeltaObject) vecSN.get(cnt5);
                            Log.printVerbose(" SERIAL :" + sndObj.serialNumber);
                            String buffer = labelString + "\nSN:" + sndObj.serialNumber;
                            table.addCell(new Phrase(buffer, FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL)));
                        }
                    }
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
