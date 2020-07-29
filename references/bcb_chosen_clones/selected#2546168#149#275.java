    public static void createOutputStructure(String templatePath) throws InterruptedException {
        try {
            templatePath = new File(templatePath).getCanonicalPath();
            templatePath = templatePath.replace('\\', '/');
            File file = null;
            Paths paths = (Paths) GragGenerator.getObjectsFromTree(Paths.class).get(0);
            Config config = (Config) GragGenerator.getObjectsFromTree(Config.class).get(0);
            DirectoryIterator iterator = new DirectoryIterator(templatePath, true, true);
            while ((file = iterator.getNext()) != null) {
                boolean copyFile = false;
                String fullFilename = file.getCanonicalPath();
                int lastDirPos = fullFilename.lastIndexOf(System.getProperty("file.separator"));
                if (CVS_DIR.equals(file.getCanonicalPath().substring(fullFilename.length() - CVS_DIR.length(), fullFilename.length())) || CVS_DIR.equals(fullFilename.substring(lastDirPos - CVS_DIR.length(), lastDirPos))) {
                    continue;
                }
                if ("readme.txt".equals(file.getName())) {
                    continue;
                }
                String fileOut = outputDir.replace('\\', '/');
                String path = file.getCanonicalPath().replace('\\', '/');
                if (path.indexOf(templatePath) == 0) {
                    path = path.substring(templatePath.length());
                    if (path.startsWith(Paths.CONF_GENERAL_DIR)) {
                        path = paths.getConfigOutput() + path.substring(Paths.CONF_GENERAL_DIR.length());
                        copyFile = true;
                    } else if (path.startsWith(Paths.CONF_STRUTS_DIR)) {
                        path = paths.getConfigOutput() + path.substring(Paths.CONF_STRUTS_DIR.length());
                        copyFile = true;
                    } else if (path.startsWith(Paths.CONF_TAPESTRY4_DIR)) {
                        path = paths.getConfigOutput() + path.substring(Paths.CONF_TAPESTRY4_DIR.length());
                        copyFile = true;
                    } else if (path.startsWith(Paths.CONF_SWING_DIR)) {
                        path = paths.getConfigOutput() + path.substring(Paths.CONF_SWING_DIR.length());
                        copyFile = true;
                    } else if (path.startsWith(Paths.JAVA_WEB_STRUTS_DIR)) {
                        path = paths.getJspOutput() + path.substring(Paths.JAVA_WEB_STRUTS_DIR.length());
                        if (config.matchWebTier("struts").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_WEB_TAPESTRY4_DIR)) {
                        path = paths.getJspOutput() + path.substring(Paths.JAVA_WEB_TAPESTRY4_DIR.length());
                        if (config.matchWebTier("tapestry").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_SWING_DIR)) {
                        path = paths.getSwingOutput() + path.substring(Paths.JAVA_SWING_DIR.length());
                        if (config.matchWebTier("swing").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_STRUTS_DIR)) {
                        path = paths.getWebOutput() + path.substring(Paths.JAVA_STRUTS_DIR.length());
                        if (config.matchWebTier("struts").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_TAPESTRY4_DIR)) {
                        path = paths.getWebOutput() + path.substring(Paths.JAVA_TAPESTRY4_DIR.length());
                        if (config.matchWebTier("tapestry").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_EJB2_DIR)) {
                        path = paths.getEjbOutput() + path.substring(Paths.JAVA_EJB2_DIR.length());
                        if (config.matchBusinessTier("ejb 2").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_EJB3_DIR)) {
                        path = paths.getEjbOutput() + path.substring(Paths.JAVA_EJB3_DIR.length());
                        if (config.matchBusinessTier("ejb 3").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_HIBERNATE2_DIR)) {
                        path = paths.getHibernateOutput() + path.substring(Paths.JAVA_HIBERNATE2_DIR.length());
                        if (config.matchBusinessTier("hibernate 2").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_HIBERNATE3_DIR)) {
                        path = paths.getHibernateOutput() + path.substring(Paths.JAVA_HIBERNATE3_DIR.length());
                        if (config.matchBusinessTier("hibernate 3").booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_MOCK_DIR)) {
                        path = paths.getMockOutput() + path.substring(Paths.JAVA_MOCK_DIR.length());
                        if (config.useMock().booleanValue()) {
                            copyFile = true;
                        }
                    } else if (path.startsWith(Paths.JAVA_SERVICE_DIR)) {
                        path = paths.getServiceOutput() + path.substring(Paths.JAVA_SERVICE_DIR.length());
                        copyFile = true;
                    } else if (path.startsWith(Paths.JAVA_TEST_DIR)) {
                        path = paths.getTestOutput() + path.substring(Paths.JAVA_TEST_DIR.length());
                        copyFile = true;
                    } else if ((path.indexOf("build.bat") != -1) || ((path.indexOf("deploy.bat") != -1))) {
                        copyFile = true;
                    }
                }
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
                if (copyFile) {
                    fileOut += path;
                    path = outputDir + path;
                    if (!file.isDirectory()) {
                        String name = file.getName();
                        path = path.substring(0, (path.length() - name.length()));
                    }
                    new File(path).mkdirs();
                    if (!file.isDirectory()) {
                        byte array[] = new byte[1024];
                        int size = 0;
                        try {
                            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileOut));
                            while ((size = in.read(array)) != -1) out.write(array, 0, size);
                            in.close();
                            out.flush();
                            out.close();
                        } catch (Exception exc) {
                            log("[Error] Copy output file failed : " + fileOut);
                            log(exc.getMessage());
                        }
                    }
                }
            }
        } catch (Exception exc) {
            log.error("Error while copying files: ", exc);
            log(exc.getMessage());
        }
    }
