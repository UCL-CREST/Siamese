    private static void createPdf(String filename) {
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.addTitle("Hello World example");
            document.addSubject("This example shows how to add metadata");
            document.addKeywords("Metadata, iText, step 3");
            document.addCreator("My program using iText");
            document.addAuthor("Bruno Lowagie");
            writer.createXmpMetadata();
            document.open();
            Paragraph hello = new Paragraph("(English:) hello, " + "(Esperanto:) he, alo, saluton, (Latin:) heu, ave, " + "(French:) allô, (Italian:) ciao, (German:) hallo, he, heda, holla, " + "(Portuguese:) alô, olá, hei, psiu, bom día, (Dutch:) hallo, dag, " + "(Spanish:) ola, eh, (Catalan:) au, bah, eh, ep, " + "(Swedish:) hej, hejsan(Danish:) hallo, dav, davs, goddag, hej, " + "(Norwegian:) hei; morn, (Papiamento:) halo; hallo; kí tal, " + "(Faeroese:) halló, hoyr, (Turkish:) alo, merhaba, (Albanian:) tungjatjeta");
            Chapter universe = new Chapter("To the Universe:", 1);
            Section section;
            section = universe.addSection("to the World:");
            section.add(hello);
            section = universe.addSection("to the Sun:");
            section.add(hello);
            section = universe.addSection("to the Moon:");
            section.add(hello);
            section = universe.addSection("to the Stars:");
            section.add(hello);
            document.add(universe);
            Chapter people = new Chapter("To the People:", 2);
            section = people.addSection("to mothers and fathers:");
            section.add(hello);
            section = people.addSection("to brothers and sisters:");
            section.add(hello);
            section = people.addSection("to wives and husbands:");
            section.add(hello);
            section = people.addSection("to sons and daughters:");
            section.add(hello);
            section = people.addSection("to complete strangers:");
            section.add(hello);
            document.add(people);
            document.setPageSize(PageSize.A4.rotate());
            Chapter animals = new Chapter("To the Animals:", 3);
            section = animals.addSection("to cats and dogs:");
            section.add(hello);
            section = animals.addSection("to birds and bees:");
            section.add(hello);
            section = animals.addSection("to farm animals and wild animals:");
            section.add(hello);
            section = animals.addSection("to bugs and beatles:");
            section.add(hello);
            section = animals.addSection("to fish and shellfish:");
            section.add(hello);
            document.add(animals);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
