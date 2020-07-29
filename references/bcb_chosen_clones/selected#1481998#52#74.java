    public void generatePdf() throws FileNotFoundException, DocumentException {
        Document cv = new Document();
        PdfWriter.getInstance(cv, new FileOutputStream(name + ".pdf"));
        cv.open();
        cv.add(cvgeneratoritext.ITextSchema.Title("Curiculum vitae"));
        cv.add(new Paragraph("Osnovne informacije", normal));
        cv.add(new Paragraph("Ime: " + name, normal));
        cv.add(new Paragraph("Datum rođenja: " + birthDate, normal));
        cv.add(new Paragraph("Adresa: " + adress, normal));
        cv.add(new Paragraph("Telefon: " + phone, normal));
        cv.add(new Paragraph("Mobilni telefon: " + cellphone, normal));
        cv.add(new Paragraph("E-mail adresa " + email, normal));
        cv.add(new Paragraph("Obrazovanje ", normal));
        cv.add(new Paragraph("Fakultet: " + faculty));
        cv.add(new Paragraph("Obrazovni profil/smer: " + profile, normal));
        cv.add(new Paragraph("Zvanje/diploma: " + diploma, normal));
        cv.add(new Paragraph("Zaposlenje "));
        cv.add(new Paragraph("Kompanija: " + company, normal));
        cv.add(new Paragraph("Pozicija:" + position, normal));
        cv.add(new Paragraph("Period zaposlenja: " + period, normal));
        cv.add(new Paragraph("Opis posla: " + description, normal));
        cv.close();
    }
