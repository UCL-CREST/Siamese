    private void createScript(File scriptsLocation, String relativePath, String scriptContent) {
        Writer fileWriter = null;
        try {
            File scriptFile = new File(scriptsLocation.getAbsolutePath() + "/" + relativePath);
            scriptFile.getParentFile().mkdirs();
            fileWriter = new FileWriter(scriptFile);
            IOUtils.copy(new StringReader(scriptContent), fileWriter);
        } catch (IOException e) {
            throw new UnitilsException(e);
        } finally {
            IOUtils.closeQuietly(fileWriter);
        }
    }
