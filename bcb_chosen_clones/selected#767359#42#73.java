    public void print(List<Test> tests, List<Database> databases) throws FileNotFoundException, DocumentException, URISyntaxException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(Resource.getNewFile("benchmark.pdf")));
        document.open();
        document.addTitle("Database Benchmark Report");
        document.addAuthor("Valerio Barbagallo");
        document.addSubject("A report for the benchmark");
        document.addKeywords("test, benchmark, in-memory, database, main memory, embedded");
        document.addCreator("The benchmark application");
        Paragraph title = new Paragraph(TITLE, TITLE_FONT);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingBefore(TITLE_SPACE_BEFORE);
        Paragraph date = new Paragraph(new Date().toString());
        date.setAlignment(Paragraph.ALIGN_CENTER);
        date.setSpacingBefore(DATA_SPACE_BEFORE);
        document.add(title);
        document.add(date);
        Paragraph intro = new Paragraph(INTRO);
        intro.setSpacingBefore(PARAGRAPH_SPACE_BEFORE);
        document.add(intro);
        document.add(getTestDescription(tests));
        document.add(getDatabaseDescription(databases));
        int chapterNumber = 1;
        for (Test test : tests) {
            Paragraph chapterName = new Paragraph(test.getName(), CHAPTER_FONT);
            Chapter chapter = new Chapter(chapterName, chapterNumber);
            test.print(chapter);
            document.add(chapter);
            chapterNumber++;
        }
        document.close();
    }
