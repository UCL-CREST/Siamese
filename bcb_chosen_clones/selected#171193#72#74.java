    private void copyArtifact(String name) throws IOException {
        IOUtils.copyFromClassPath(name, model.getOutputFolder() + name);
    }
