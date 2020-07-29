    public void testMandatorySections() throws Exception {
        final File specificationDirectory = this.getTestSourcesDirectory();
        final File implementationDirectory = this.getTestSourcesDirectory();
        IOUtils.copy(this.getClass().getResourceAsStream("ImplementationWithoutAnnotationsSection.java.txt"), new FileOutputStream(new File(implementationDirectory, "Implementation.java")));
        try {
            this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
            Assert.fail("Expected IOException not thrown.");
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
        IOUtils.copy(this.getClass().getResourceAsStream("ImplementationWithoutDependenciesSection.java.txt"), new FileOutputStream(new File(implementationDirectory, "Implementation.java")));
        try {
            this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
            Assert.fail("Expected IOException not thrown.");
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
        IOUtils.copy(this.getClass().getResourceAsStream("ImplementationWithoutMessagesSection.java.txt"), new FileOutputStream(new File(implementationDirectory, "Implementation.java")));
        try {
            this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
            Assert.fail("Expected IOException not thrown.");
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
        IOUtils.copy(this.getClass().getResourceAsStream("ImplementationWithoutPropertiesSection.java.txt"), new FileOutputStream(new File(implementationDirectory, "Implementation.java")));
        try {
            this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
            Assert.fail("Expected IOException not thrown.");
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
        IOUtils.copy(this.getClass().getResourceAsStream("ImplementationOfSpecificationWithoutConstructorsSection.java.txt"), new FileOutputStream(new File(implementationDirectory, "ImplementationOfSpecification.java")));
        try {
            this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("ImplementationOfSpecification"), implementationDirectory);
            Assert.fail("Expected IOException not thrown.");
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
        IOUtils.copy(this.getClass().getResourceAsStream("SpecificationWithoutAnnotationsSection.java.txt"), new FileOutputStream(new File(specificationDirectory, "Specification.java")));
        try {
            this.getTestTool().manageSources(this.getTestTool().getModules().getSpecification("Specification"), specificationDirectory);
            Assert.fail("Expected IOException not thrown.");
        } catch (IOException e) {
            Assert.assertNotNull(e.getMessage());
            System.out.println(e);
        }
    }
