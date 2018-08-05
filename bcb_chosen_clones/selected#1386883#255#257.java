    public void copyFiles() {
        IOUtils.copyWithRsync(projectDirectory, outputDirectory, String.format("(%s->%s)   ", projectDirectory().getName(), outputDirectory.getName()));
    }
