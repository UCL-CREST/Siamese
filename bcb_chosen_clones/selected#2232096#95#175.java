    @Override
    public boolean performOk() {
        this.setPropertyValue("bea.home", this.beaHome.getText());
        this.setPropertyValue("bea.domain", this.domainDirectory.getText());
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                saveWLHome();
                for (final TabControl control : tabControls) {
                    control.performOk(WLPropertyPage.this.getProject(), WLPropertyPage.this);
                }
                if (isEnabledJCLCopy()) {
                    final File url = new File(WLPropertyPage.this.domainDirectory.getText());
                    File lib = new File(url, "lib");
                    File log4jLibrary = new File(lib, "log4j-1.2.13.jar");
                    if (!log4jLibrary.exists()) {
                        InputStream srcFile = null;
                        FileOutputStream fos = null;
                        try {
                            srcFile = toInputStream(new Path("jcl/log4j-1.2.13.jar"));
                            fos = new FileOutputStream(log4jLibrary);
                            IOUtils.copy(srcFile, fos);
                            srcFile.close();
                            fos.flush();
                            fos.close();
                            srcFile = toInputStream(new Path("/jcl/commons-logging-1.0.4.jar"));
                            File jcl = new File(lib, "commons-logging-1.0.4.jar");
                            fos = new FileOutputStream(jcl);
                            IOUtils.copy(srcFile, fos);
                        } catch (IOException e) {
                            Logger.log(Logger.ERROR, "Could not copy JCL jars file to Bea WL", e);
                        } finally {
                            try {
                                if (srcFile != null) {
                                    srcFile.close();
                                    srcFile = null;
                                }
                                if (fos != null) {
                                    fos.flush();
                                    fos.close();
                                    fos = null;
                                }
                            } catch (IOException e) {
                            }
                        }
                    }
                }
                if (isEnabledJSTLCopy()) {
                    File url = new File(WLPropertyPage.this.domainDirectory.getText());
                    File lib = new File(url, "lib");
                    File jstlLibrary = new File(lib, "jstl.jar");
                    if (!jstlLibrary.exists()) {
                        InputStream srcFile = null;
                        FileOutputStream fos = null;
                        try {
                            srcFile = toInputStream(new Path("jstl/jstl.jar"));
                            fos = new FileOutputStream(jstlLibrary);
                            IOUtils.copy(srcFile, fos);
                        } catch (IOException e) {
                            Logger.log(Logger.ERROR, "Could not copy the JSTL 1.1 jar file to Bea WL", e);
                        } finally {
                            try {
                                if (srcFile != null) {
                                    srcFile.close();
                                    srcFile = null;
                                }
                                if (fos != null) {
                                    fos.flush();
                                    fos.close();
                                    fos = null;
                                }
                            } catch (final IOException e) {
                                Logger.getLog().debug("I/O exception closing resources", e);
                            }
                        }
                    }
                }
            }
        });
        return super.performOk();
    }
