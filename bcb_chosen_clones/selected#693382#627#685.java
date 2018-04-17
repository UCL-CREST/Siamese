        public void performOk(final IProject project, final TomcatPropertyPage page) {
            page.setPropertyValue("tomcat.jdbc.driver", c_drivers.getText());
            page.setPropertyValue("tomcat.jdbc.url", url.getText());
            page.setPropertyValue("tomcat.jdbc.user", username.getText());
            page.setPropertyValue("tomcat.jdbc.password", password.getText());
            File lib = new File(page.tomcatHome.getText(), "lib");
            if (!lib.exists()) {
                lib = new File(new File(page.tomcatHome.getText(), "common"), "lib");
                if (!lib.exists()) {
                    Logger.log(Logger.ERROR, "Not properly location of Tomcat Home at :: " + lib);
                    throw new IllegalStateException("Not properly location of Tomcat Home");
                }
            }
            final File conf = new File(page.tomcatHome.getText(), "conf/Catalina/localhost");
            if (!conf.exists()) {
                final boolean create = NexOpenUIActivator.getDefault().getTomcatConfProperty();
                if (create) {
                    if (Logger.getLog().isDebugEnabled()) {
                        Logger.getLog().debug("Create directory " + conf);
                    }
                    try {
                        conf.mkdirs();
                    } catch (final SecurityException se) {
                        Logger.getLog().error("Retrieved a Security exception creating " + conf, se);
                        Logger.log(Logger.ERROR, "Not created " + conf + " directory. Not enough privilegies. Message :: " + se.getMessage());
                    }
                }
            }
            String str_driverLibrary = LIBRARIES.get(c_drivers.getText());
            if ("<mysql_driver>".equals(str_driverLibrary)) {
                str_driverLibrary = NexOpenUIActivator.getDefault().getMySQLDriver();
            }
            final File driverLibrary = new File(lib, str_driverLibrary);
            if (!driverLibrary.exists()) {
                InputStream driver = null;
                FileOutputStream fos = null;
                try {
                    driver = AppServerPropertyPage.toInputStream(new Path("jdbc/" + str_driverLibrary));
                    fos = new FileOutputStream(driverLibrary);
                    IOUtils.copy(driver, fos);
                } catch (IOException e) {
                    Logger.log(Logger.ERROR, "Could not copy the driver jar file to Tomcat", e);
                } finally {
                    try {
                        if (driver != null) {
                            driver.close();
                            driver = null;
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
            page.processTomcatCfg(c_drivers.getText(), url.getText(), username.getText(), password.getText());
        }
