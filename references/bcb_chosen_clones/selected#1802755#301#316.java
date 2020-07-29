    public void testOptionalSections() throws Exception {
        final File implementationDirectory = this.getTestSourcesDirectory();
        final File specificationDirectory = this.getTestSourcesDirectory();
        IOUtils.copy(this.getClass().getResourceAsStream("ImplementationWithoutConstructorsSection.java.txt"), new FileOutputStream(new File(implementationDirectory, "Implementation.java")));
        this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
        IOUtils.copy(this.getClass().getResourceAsStream("ImplementationWithoutDefaultConstructorSection.java.txt"), new FileOutputStream(new File(implementationDirectory, "Implementation.java")));
        this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
        IOUtils.copy(this.getClass().getResourceAsStream("ImplementationWithoutDocumentationSection.java.txt"), new FileOutputStream(new File(implementationDirectory, "Implementation.java")));
        this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
        IOUtils.copy(this.getClass().getResourceAsStream("ImplementationWithoutLicenseSection.java.txt"), new FileOutputStream(new File(implementationDirectory, "Implementation.java")));
        this.getTestTool().manageSources(this.getTestTool().getModules().getImplementation("Implementation"), implementationDirectory);
        IOUtils.copy(this.getClass().getResourceAsStream("SpecificationWithoutDocumentationSection.java.txt"), new FileOutputStream(new File(specificationDirectory, "Specification.java")));
        this.getTestTool().manageSources(this.getTestTool().getModules().getSpecification("Specification"), specificationDirectory);
        IOUtils.copy(this.getClass().getResourceAsStream("SpecificationWithoutLicenseSection.java.txt"), new FileOutputStream(new File(specificationDirectory, "Specification.java")));
        this.getTestTool().manageSources(this.getTestTool().getModules().getSpecification("Specification"), specificationDirectory);
    }
