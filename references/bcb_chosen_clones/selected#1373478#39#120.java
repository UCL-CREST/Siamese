    public void generate(FileObject outputDirectory, FileObject generatedOutputDirectory, List<Library> libraryModels, String tapdocXml) throws FileSystemException {
        if (!generatedOutputDirectory.exists()) {
            generatedOutputDirectory.createFolder();
        }
        if (outputDirectory.exists()) {
            outputDirectory.createFolder();
        }
        ZipUtils.extractZip(new ClasspathResource(classResolver, "/com/erinors/tapestry/tapdoc/service/xdoc/resources.zip"), outputDirectory);
        for (Library library : libraryModels) {
            String libraryName = library.getName();
            String libraryLocation = library.getLocation();
            generatedOutputDirectory.resolveFile(fileNameGenerator.getLibraryDirectory(libraryLocation)).createFolder();
            try {
                String result = XsltUtils.xsltTransform(tapdocXml, getClass().getResourceAsStream("Library.xsl"), "libraryName", libraryName);
                FileObject index = generatedOutputDirectory.resolveFile(fileNameGenerator.getLibraryDirectory(libraryLocation)).resolveFile("index.xml");
                Writer out = new OutputStreamWriter(index.getContent().getOutputStream(), "UTF-8");
                out.write(result);
                out.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            for (Component component : library.getComponents()) {
                String componentName = component.getName();
                System.out.println("Generating " + libraryName + ":" + componentName + "...");
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("libraryName", libraryName);
                parameters.put("componentName", componentName);
                String result = XsltUtils.xsltTransform(tapdocXml, getClass().getResourceAsStream("Component.xsl"), parameters);
                Writer out = null;
                try {
                    FileObject index = generatedOutputDirectory.resolveFile(fileNameGenerator.getComponentIndexFile(libraryLocation, componentName, true));
                    out = new OutputStreamWriter(index.getContent().getOutputStream(), "UTF-8");
                    out.write(result);
                    out.close();
                    Resource specificationLocation = component.getSpecificationLocation();
                    if (specificationLocation.getRelativeResource(componentName + "_tapdoc/resource").getResourceURL() != null) {
                        File srcResourcesDirectory = new File(specificationLocation.getRelativeResource(componentName + "_tapdoc/resource").getResourceURL().toURI());
                        FileObject dstResourcesFileObject = outputDirectory.resolveFile(fileNameGenerator.getComponentDirectory(libraryLocation, componentName)).resolveFile("resource");
                        if (srcResourcesDirectory.exists() && srcResourcesDirectory.isDirectory()) {
                            File[] files = srcResourcesDirectory.listFiles();
                            if (files != null) {
                                for (File resource : files) {
                                    if (resource.isFile() && !resource.isHidden()) {
                                        FileObject resourceFileObject = dstResourcesFileObject.resolveFile(resource.getName());
                                        resourceFileObject.createFile();
                                        InputStream inResource = null;
                                        OutputStream outResource = null;
                                        try {
                                            inResource = new FileInputStream(resource);
                                            outResource = resourceFileObject.getContent().getOutputStream();
                                            IOUtils.copy(inResource, outResource);
                                        } finally {
                                            IOUtils.closeQuietly(inResource);
                                            IOUtils.closeQuietly(outResource);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    IOUtils.closeQuietly(out);
                }
            }
        }
        {
            Writer out = null;
            try {
                String result = XsltUtils.xsltTransform(tapdocXml, getClass().getResourceAsStream("Overview.xsl"));
                FileObject index = generatedOutputDirectory.resolveFile("index.xml");
                out = new OutputStreamWriter(index.getContent().getOutputStream(), "UTF-8");
                out.write(result);
                out.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(out);
            }
        }
    }
