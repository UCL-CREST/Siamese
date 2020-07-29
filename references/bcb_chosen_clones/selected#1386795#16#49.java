    public static void pdf(EID eid, String path) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            Image img1 = Image.getInstance(eid.getPicture());
            System.out.println(img1.getClass().getName());
            document.add(img1);
            PdfPTable table = new PdfPTable(1);
            document.add(new Paragraph(eid.getFullName()));
            table.addCell(new Phrase("Third firstname initial:" + eid.getData().getThirdFirstnameInitial()));
            table.addCell(new Phrase("Sex:" + eid.getData().getSex()));
            table.addCell(new Phrase("Nationality:" + eid.getData().getNationality()));
            table.addCell(new Phrase("National number:" + eid.getData().getNationalNumber()));
            table.addCell(new Phrase("Birth date:" + eid.getData().getBirthDate()));
            table.addCell(new Phrase("Birth location:" + eid.getData().getBirthLocation()));
            table.addCell(new Phrase("Noble condition:" + eid.getData().getNobleCondition()));
            table.addCell(new Phrase("Special status:" + eid.getData().getSpecialStatus()));
            table.addCell(new Phrase("Card number:" + eid.getData().getCardNumber()));
            table.addCell(new Phrase("Document type:" + eid.getData().getDocumentType()));
            table.addCell(new Phrase("Delivery municipality:" + eid.getData().getDeliveryMunicipality()));
            table.addCell(new Phrase("Validity date:" + eid.getData().getValidityBeginDate() + " - " + eid.getData().getValidityEndDate()));
            table.addCell(new Paragraph("Address"));
            table.addCell(new Phrase("Municipality:" + eid.getAddress().getMunicipality()));
            table.addCell(new Phrase("Street and number:" + eid.getAddress().getStreetAndNumber()));
            table.addCell(new Phrase("Zip code:" + eid.getAddress().getZipCode()));
            document.add(table);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
