    protected void readScript() throws IOException {
        Reader scriptReader = openScriptContentReader();
        IOUtils.copy(scriptReader, new NullWriter());
        scriptReader.close();
    }
