    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            this.getLog().info("copy source web.xml - " + this.getWebXml() + " to build dir (source web.xml required if mergewebxml execution is enabled)");
            File destination = new File(this.getBuildDir(), "web.xml");
            if (!destination.exists()) {
                destination.getParentFile().mkdirs();
                destination.createNewFile();
            }
            FileIOUtils.copyFile(this.getWebXml(), destination);
            for (int i = 0; i < this.getCompileTarget().length; i++) {
                File moduleFile = null;
                for (Iterator it = this.getProject().getCompileSourceRoots().iterator(); it.hasNext() && moduleFile == null; ) {
                    File check = new File(it.next().toString() + "/" + this.getCompileTarget()[i].replace('.', '/') + ".gwt.xml");
                    getLog().debug("Looking for file: " + check.getAbsolutePath());
                    if (check.exists()) {
                        moduleFile = check;
                    }
                }
                for (Iterator it = this.getProject().getResources().iterator(); it.hasNext(); ) {
                    Resource r = (Resource) it.next();
                    File check = new File(r.getDirectory() + "/" + this.getCompileTarget()[i].replace('.', '/') + ".gwt.xml");
                    getLog().debug("Looking for file: " + check.getAbsolutePath());
                    if (check.exists()) {
                        moduleFile = check;
                    }
                }
                ClassLoader loader = this.fixThreadClasspath();
                if (moduleFile == null) {
                    try {
                        String classpath = "/" + this.getCompileTarget()[i].replace('.', '/') + ".gwt.xml";
                        InputStream is = loader.getResourceAsStream(classpath);
                        System.out.println("Looking for classpath: " + classpath + "(" + (is != null) + ")");
                        if (is != null) {
                            File temp = new File(this.getBuildDir(), this.getCompileTarget()[i].concat(".gwt.xml"));
                            FileOutputStream fos = new FileOutputStream(temp);
                            FileIOUtils.copyStream(is, fos);
                            moduleFile = temp;
                        }
                    } catch (IOException e) {
                        this.getLog().info(e);
                    }
                }
                GwtWebInfProcessor processor = null;
                try {
                    if (moduleFile != null) {
                        getLog().info("Module file: " + moduleFile.getAbsolutePath());
                        processor = new GwtWebInfProcessor(this.getCompileTarget()[i], moduleFile, destination.getAbsolutePath(), destination.getAbsolutePath(), this.isWebXmlServletPathAsIs());
                        processor.process();
                    } else {
                        throw new MojoExecutionException("module file null");
                    }
                } catch (ExitException e) {
                    this.getLog().info(e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to merge web.xml", e);
        }
    }
