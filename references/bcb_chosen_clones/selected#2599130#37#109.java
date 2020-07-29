    public List<String> generate(String geronimoVersion, String geronimoHome, String instanceNumber) {
        geronimoRepository = geronimoHome + "/repository";
        Debug.logInfo("The WASCE or Geronimo Repository is " + geronimoRepository, module);
        Classpath classPath = new Classpath(System.getProperty("java.class.path"));
        List<File> elements = classPath.getElements();
        List<String> jar_version = new ArrayList<String>();
        String jarPath = null;
        String jarName = null;
        String newJarName = null;
        String jarNameSimple = null;
        String jarVersion = "1.0";
        int lastDash = -1;
        for (File f : elements) {
            if (f.exists()) {
                if (f.isFile()) {
                    jarPath = f.getAbsolutePath();
                    jarName = f.getName();
                    String jarNameWithoutExt = (String) jarName.subSequence(0, jarName.length() - 4);
                    lastDash = jarNameWithoutExt.lastIndexOf("-");
                    if (lastDash > -1) {
                        jarVersion = jarNameWithoutExt.substring(lastDash + 1, jarNameWithoutExt.length());
                        jarNameSimple = jarNameWithoutExt.substring(0, lastDash);
                        boolean alreadyVersioned = 0 < StringUtil.removeRegex(jarVersion, "[^.0123456789]").length();
                        if (!alreadyVersioned) {
                            jarVersion = "1.0";
                            jarNameSimple = jarNameWithoutExt;
                            newJarName = jarNameWithoutExt + "-" + jarVersion + ".jar";
                        } else {
                            newJarName = jarName;
                        }
                    } else {
                        jarVersion = "1.0";
                        jarNameSimple = jarNameWithoutExt;
                        newJarName = jarNameWithoutExt + "-" + jarVersion + ".jar";
                    }
                    jar_version.add(jarNameSimple + "#" + jarVersion);
                    String targetDirectory = geronimoRepository + "/org/ofbiz/" + jarNameSimple + "/" + jarVersion;
                    File targetDir = new File(targetDirectory);
                    if (!targetDir.exists()) {
                        boolean created = targetDir.mkdirs();
                        if (!created) {
                            Debug.logFatal("Unable to create target directory - " + targetDirectory, module);
                            return null;
                        }
                    }
                    if (!targetDirectory.endsWith("/")) {
                        targetDirectory = targetDirectory + "/";
                    }
                    String newCompleteJarName = targetDirectory + newJarName;
                    File newJarFile = new File(newCompleteJarName);
                    try {
                        FileChannel srcChannel = new FileInputStream(jarPath).getChannel();
                        FileChannel dstChannel = new FileOutputStream(newCompleteJarName).getChannel();
                        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                        Debug.log("Created jar file : " + newJarName + " in WASCE or Geronimo repository", module);
                        srcChannel.close();
                        dstChannel.close();
                    } catch (IOException e) {
                        Debug.logFatal("Unable to create jar file - " + newJarName + " in WASCE or Geronimo repository (certainly already exists)", module);
                        return null;
                    }
                }
            }
        }
        List<ComponentConfig.WebappInfo> webApps = ComponentConfig.getAllWebappResourceInfos();
        File geronimoWebXml = new File(System.getProperty("ofbiz.home") + "/framework/appserver/templates/" + geronimoVersion + "/geronimo-web.xml");
        for (ComponentConfig.WebappInfo webApp : webApps) {
            if (null != webApp) {
                parseTemplate(geronimoWebXml, webApp);
            }
        }
        return jar_version;
    }
