    private void javaToHtml(File source, File destination) throws IOException {
        Reader reader = new FileReader(source);
        Writer writer = new FileWriter(destination);
        JavaUtils.writeJava(reader, writer);
        writer.flush();
        writer.close();
    }
