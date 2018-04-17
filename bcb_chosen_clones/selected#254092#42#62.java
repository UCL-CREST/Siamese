    private void putQuestion(Question q, ZipOutputStream out) throws IOException, JAXBException {
        ZipEntry entry = new ZipEntry("question" + q.getNumber() + ".xml");
        out.putNextEntry(entry);
        JAXBContext context = JAXBContext.newInstance(Question.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(m.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(q, out);
        if (q.hasImage()) {
            entry = new ZipEntry("question" + q.getNumber() + ".png");
            out.putNextEntry(entry);
            ImageIO.write(q.getImage(), "png", out);
        }
        for (int i = 0; i < q.getAnswers().size(); i++) {
            Answer a = q.getAnswers().get(i);
            if (a.hasImage()) {
                entry = new ZipEntry("question" + q.getNumber() + "_answer" + a.getLetter() + ".png");
                out.putNextEntry(entry);
                ImageIO.write(a.getImage(), "png", out);
            }
        }
    }
