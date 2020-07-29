    public static void main(String[] args) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("ThePdf.pdf"));
            document.open();
            document.addTitle("A list of persons");
            document.addAuthor("Toni Kostov");
            document.addSubject("Generating PDF from mock up data");
            document.addKeywords("iText, pdf, generatior, generation");
            document.addCreator("Java code using iText");
            MockUpData data = new MockUpData();
            List<Person> persons = data.generatePersonList();
            int counter = 0;
            for (int i = 0; i < persons.size(); i++) {
                Person person = persons.get(i);
                PdfPTable table = new PdfPTable(2);
                int columnWidths[] = { 10, 20 };
                table.setWidths(columnWidths);
                table.setWidthPercentage(100);
                table.setHorizontalAlignment(Element.ALIGN_LEFT);
                PdfPCell headerCell = new PdfPCell();
                headerCell.setColspan(2);
                headerCell.setGrayFill(0.9f);
                headerCell.setMinimumHeight(10f);
                table.addCell(headerCell);
                table.addCell("Name: ");
                table.addCell(person.getFirstName() + " " + person.getLastName());
                table.addCell("Address: ");
                table.addCell(person.getAddress().getStreetNumber() + ", " + person.getAddress().getStreetName());
                table.addCell("");
                table.addCell(String.valueOf(person.getAddress().getPostalCode()));
                table.addCell("");
                table.addCell(person.getAddress().getCity());
                table.addCell("");
                table.addCell(person.getAddress().getCountry());
                table.setSplitRows(false);
                document.add(new Paragraph(" "));
                document.add(new DottedLineSeparator());
                document.add(new Paragraph(" "));
                document.add(table);
                counter++;
                if (counter > 4) {
                    counter = 0;
                    document.newPage();
                }
            }
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
