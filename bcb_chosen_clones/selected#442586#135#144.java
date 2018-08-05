    private void copyThemeProviderClass() throws Exception {
        InputStream is = getClass().getResourceAsStream("/zkthemer/ThemeProvider.class");
        if (is == null) throw new RuntimeException("Cannot find ThemeProvider.class");
        File outFile = new File(theme.getJarRootFile(), "zkthemer/ThemeProvider.class");
        outFile.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(outFile);
        IOUtils.copy(is, out);
        out.close();
        FileUtils.writeStringToFile(new File(theme.getJarRootFile(), "zkthemer.properties"), "theme=" + theme.getName() + "\r\nfileList=" + fileList.deleteCharAt(fileList.length() - 1).toString());
    }
