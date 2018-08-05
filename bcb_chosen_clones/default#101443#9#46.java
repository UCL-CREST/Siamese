    public static void main(String[] args) {
        System.out.println("Chapter 5 example 1: My first table");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0501.pdf"));
            document.open();
            Table table = new Table(3);
            table.setBorderWidth(1);
            table.setBorderColor(new Color(0, 0, 255));
            table.setCellpadding(5);
            table.setCellspacing(5);
            Cell cell = new Cell("header");
            cell.setHeader(true);
            cell.setColspan(3);
            table.addCell(cell);
            table.endHeaders();
            cell = new Cell("example cell with colspan 1 and rowspan 2");
            cell.setRowspan(2);
            cell.setBorderColor(new Color(255, 0, 0));
            table.addCell(cell);
            table.addCell("1.1");
            table.addCell("2.1");
            table.addCell("1.2");
            table.addCell("2.2");
            table.addCell("cell test1");
            cell = new Cell("big cell");
            cell.setRowspan(2);
            cell.setColspan(2);
            table.addCell(cell);
            table.addCell("cell test2");
            document.add(table);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
