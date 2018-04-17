    public static void main(String[] args) {
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapterX/gis.pdf"));
            document.open();
            Image large = Image.getInstance("resources/in_action/chapterX/foobar.png");
            Image small = Image.getInstance("resources/in_action/chapterX/foobar_thumb.png");
            float[] widths = { large.getWidth(), small.getWidth() * 2 };
            PdfPTable table = new PdfPTable(widths);
            PdfPCell cell = new PdfPCell(large, true);
            cell.setBorderWidth(3);
            cell.setBorderColor(new GrayColor(0.7f));
            cell.setUseBorderPadding(true);
            table.addCell(cell);
            PdfPTable innertable = new PdfPTable(1);
            cell = new PdfPCell(small, true);
            cell.setBorder(PdfPCell.BOTTOM);
            cell.setBorderWidth(3);
            cell.setBorderColor(new GrayColor(0.7f));
            cell.setUseBorderPadding(true);
            innertable.addCell(cell);
            PdfPTable legendtable = new PdfPTable(1);
            legendtable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
            legendtable.addCell("Legend");
            legendtable.addCell("test1");
            legendtable.addCell("test2");
            legendtable.addCell("test3");
            legendtable.addCell("test4");
            legendtable.addCell("test5");
            legendtable.addCell("test6");
            legendtable.addCell("test7");
            cell = new PdfPCell(legendtable);
            cell.setBorder(PdfPCell.BOTTOM);
            cell.setBorderWidth(3);
            cell.setBorderColor(new GrayColor(0.7f));
            cell.setUseBorderPadding(true);
            innertable.addCell(cell);
            cell = new PdfPCell();
            cell.setBorder(PdfPCell.NO_BORDER);
            cell.setBackgroundColor(new GrayColor(0.7f));
            innertable.addCell(cell);
            cell = new PdfPCell(innertable);
            cell.setBorderWidth(3);
            cell.setBorderColor(new GrayColor(0.7f));
            cell.setUseBorderPadding(true);
            cell.setCellEvent(new GisExample());
            table.addCell(cell);
            document.add(table);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }
