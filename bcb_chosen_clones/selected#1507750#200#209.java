    private void addTemplate(OutputStream outputStream, VelocityContext velocityContext, TemplateUtils tu, String templatePath, String fileName) throws IOException, ResourceNotFoundException, ParseErrorException, Exception, MethodInvocationException {
        ((ZipOutputStream) outputStream).closeEntry();
        ((ZipOutputStream) outputStream).putNextEntry(new ZipEntry(fileName));
        Template template = Velocity.getTemplate(templatePath);
        template = Velocity.getTemplate(templatePath);
        template.initDocument();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        tu.merge(template, velocityContext, writer);
        writer.flush();
    }
