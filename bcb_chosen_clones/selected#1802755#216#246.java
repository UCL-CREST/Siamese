    public void testManageSources() throws Exception {
        this.getTestTool().manageSources(this.getTestSourcesDirectory());
        this.getTestTool().manageSources(this.getTestTool().getModules().getModule("Module"), this.getTestSourcesDirectory());
        final File implementationDirectory = this.getTestSourcesDirectory();
        this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
        this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
        final File specificationDirectory = this.getTestSourcesDirectory();
        this.getTestTool().manageSources(this.getTestTool().getModules().getSpecification("Specification"), specificationDirectory);
        this.getTestTool().manageSources(this.getTestTool().getModules().getSpecification("Specification"), specificationDirectory);
        IOUtils.copy(this.getClass().getResourceAsStream("IllegalImplementationSource.java.txt"), new FileOutputStream(new File(implementationDirectory, "Implementation.java")));
        IOUtils.copy(this.getClass().getResourceAsStream("IllegalSpecificationSource.java.txt"), new FileOutputStream(new File(specificationDirectory, "Specification.java")));
        try {
            this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
            Assert.fail("Expected IOException not thrown.");
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e.toString());
        }
        try {
            this.getTestTool().manageSources(this.getTestTool().getModules().getSpecification("Specification"), specificationDirectory);
            Assert.fail("Expected IOException not thrown.");
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e.toString());
        }
        this.getTestTool().setProfile("DOES_NOT_EXIST");
        this.getTestTool().manageSources(this.getTestSourcesDirectory());
        this.getTestTool().manageSources(this.getTestTool().getModules().getModule("Module"), this.getTestSourcesDirectory());
        this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), this.getTestSourcesDirectory());
        this.getTestTool().manageSources(this.getTestTool().getModules().getSpecification("Specification"), this.getTestSourcesDirectory());
    }
