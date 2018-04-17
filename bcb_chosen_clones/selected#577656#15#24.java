    public void compile(Project project) throws ProjectCompilerException {
        List<Resource> resources = project.getModel().getResource();
        for (Resource resource : resources) {
            try {
                IOUtils.copy(srcDir.getRelative(resource.getLocation()).getInputStream(), outDir.getRelative(resource.getLocation()).getOutputStream());
            } catch (IOException e) {
                throw new ProjectCompilerException("Resource cannot be copied. Compilation failed", e);
            }
        }
    }
