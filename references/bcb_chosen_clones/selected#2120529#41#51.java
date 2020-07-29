    public void exportTheme(final ZipOutStreamTaskAdapter out, final TemplateEntity theme) throws IOException, TaskTimeoutException {
        String themeFolder = getThemeZipPath(theme);
        addThemeResources(out, theme);
        String descriptionName = themeFolder + "_template.xml";
        if (!out.isSkip(descriptionName)) {
            out.putNextEntry(new ZipEntry(descriptionName));
            out.write(createThemeExportXML(theme).getBytes("UTF-8"));
            out.closeEntry();
            out.nextFile();
        }
    }
