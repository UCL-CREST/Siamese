    private static boolean doReport(Dao dao, String session, Group group) {
        if (!dao.isOpen()) {
            return false;
        }
        Document document = new Document();
        try {
            FileOutputStream fos = createOutputFile();
            if (fos == null) {
                return false;
            }
            PdfWriter.getInstance(document, fos);
            document.open();
            Paragraph p1 = new Paragraph(new Paragraph(String.format("%s    %s %s, %s", dao.getName(), dao.getMonth(), dao.getDay(), dao.getYear()), FontFactory.getFont(FontFactory.HELVETICA, 10)));
            document.add(p1);
            Paragraph p2 = new Paragraph(new Paragraph("Award Report", FontFactory.getFont(FontFactory.HELVETICA, 14)));
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(p2);
            Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 12);
            Font detailFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);
            PdfPCell headerCell = new PdfPCell();
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setPadding(3);
            headerCell.setBorderWidth(2);
            List<Group> groups;
            if (group != null) {
                groups = new ArrayList<Group>();
                groups.add(group);
            } else if (session != null) {
                groups = dao.getGroupsBySession(session);
            } else {
                groups = dao.getAllGroups();
            }
            for (Group g : groups) {
                PdfPTable datatable = new PdfPTable(4);
                int colWidths[] = { 30, 30, 30, 10 };
                datatable.setWidths(colWidths);
                datatable.setWidthPercentage(100);
                datatable.getDefaultCell().setPadding(3);
                datatable.getDefaultCell().setBorderWidth(2);
                datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPhrase(new Phrase(g.toString(), headerFont));
                headerCell.setColspan(4);
                datatable.addCell(headerCell);
                datatable.setHeaderRows(1);
                datatable.getDefaultCell().setBorderWidth(1);
                datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                List<Wrestler> wList = getSortedAwardList(g);
                int i = 0;
                for (Wrestler w : wList) {
                    if ((i++ % 2) == 0) {
                        datatable.getDefaultCell().setGrayFill(0.9f);
                    } else {
                        datatable.getDefaultCell().setGrayFill(1);
                    }
                    datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                    datatable.addCell(new Phrase(w.getFirstName(), detailFont));
                    datatable.addCell(new Phrase(w.getLastName(), detailFont));
                    datatable.addCell(new Phrase(w.getTeamName(), detailFont));
                    datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    Integer place = w.getPlace();
                    String placeStr = (place == null) ? "" : place.toString();
                    datatable.addCell(new Phrase(placeStr, detailFont));
                }
                datatable.setSpacingBefore(5f);
                datatable.setSpacingAfter(15f);
                document.add(datatable);
            }
        } catch (DocumentException de) {
            logger.error("Document Exception", de);
            return false;
        }
        document.close();
        return true;
    }
