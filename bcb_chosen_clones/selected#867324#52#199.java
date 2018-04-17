    public WebmillDeploy(String inputName, String outputName, boolean stripLoggers) throws Exception {
        File tempFile = null;
        JarFile jin = null;
        JarOutputStream jout = null;
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            String portletApplicationName = getPortletApplicationName(outputName);
            System.out.println("portletApplicationName = " + portletApplicationName);
            tempFile = File.createTempFile("webmill-delpoy-", "");
            tempFile.deleteOnExit();
            jin = new JarFile(inputName);
            jout = new JarOutputStream(new FileOutputStream(tempFile));
            Document webXml = null;
            Document portletXml = null;
            Document contextXml = null;
            ZipEntry src;
            InputStream source;
            Enumeration zipEntries = jin.entries();
            while (zipEntries.hasMoreElements()) {
                src = (ZipEntry) zipEntries.nextElement();
                source = jin.getInputStream(src);
                try {
                    String target = src.getName();
                    if (WEB_INF_WEB_XML.equals(target)) {
                        System.out.println("Found web.xml");
                        webXml = parseXml(source);
                    } else if (WEB_INF_PORTLET_XML.equals(target)) {
                        System.out.println("Found WEB-INF/portlet.xml");
                        portletXml = parseXml(source);
                    } else if (META_INF_CONTEXT_XML.equals(target)) {
                        System.out.println("Found META-INF/context.xml");
                        contextXml = parseXml(source);
                    }
                } finally {
                    source.close();
                }
            }
            if (webXml == null) {
                throw new IllegalArgumentException("WEB-INF/web.xml not found");
            }
            if (portletXml == null) {
                throw new IllegalArgumentException("WEB-INF/portlet.xml not found");
            }
            WebmillWebApplicationRewriter webRewriter = new WebmillWebApplicationRewriter(webXml);
            webRewriter.processWebXML();
            WebmillContextRewriter contextRewriter = new WebmillContextRewriter(contextXml, portletApplicationName);
            contextRewriter.processContextXML();
            jin = new JarFile(inputName);
            zipEntries = jin.entries();
            while (zipEntries.hasMoreElements()) {
                src = (ZipEntry) zipEntries.nextElement();
                source = jin.getInputStream(src);
                try {
                    String target = src.getName();
                    String fullTarget = '/' + target;
                    if (stripLoggers && target.endsWith(".jar") && (target.startsWith("WEB-INF/lib/commons-logging") || target.startsWith("WEB-INF/lib/log4j"))) {
                        System.out.println("Skip logger " + target);
                        continue;
                    } else {
                        if (webRewriter.getRealPortletTldFile() != null && fullTarget.equals(webRewriter.getRealPortletTldFile())) {
                            System.out.println("Skip portlet tld file " + fullTarget);
                            continue;
                        } else if (target.equals(WEB_INF_WEB_XML)) {
                            System.out.println("Skip web.xml file " + target);
                            continue;
                        } else if (target.equals(WEB_INF_PORTLET_XML)) {
                            System.out.println("Skip portlet.xml file " + target);
                            continue;
                        } else if (target.equals(META_INF_CONTEXT_XML)) {
                            System.out.println("Skip context.xml file " + target);
                            continue;
                        }
                        System.out.println("Add file " + target);
                    }
                    addFile(target, source, jout);
                } finally {
                    source.close();
                }
            }
            addFile(WEB_INF_WEB_XML, webXml, jout);
            addFile(WEB_INF_PORTLET_XML, portletXml, jout);
            addFile(META_INF_CONTEXT_XML, contextXml, jout);
            System.out.println("Attempting to add portlet.tld to war...");
            InputStream is = this.getClass().getResourceAsStream("/org/riverock/webmill/container/tags/portlet.tld");
            if (is == null) {
                System.out.println("Failed to find portlet.tld in classpath");
            } else {
                String portletTldFile = webRewriter.getRealPortletTldFile();
                if (portletTldFile.charAt(0) == '/') {
                    portletTldFile = portletTldFile.substring(1);
                }
                System.out.println("Adding file " + portletTldFile);
                try {
                    addFile(portletTldFile, is, jout);
                } finally {
                    is.close();
                }
            }
            jout.close();
            jin.close();
            jin = null;
            jout = null;
            System.out.println("Creating war " + outputName + " ...");
            System.out.flush();
            srcChannel = new FileInputStream(tempFile).getChannel();
            dstChannel = new FileOutputStream(outputName).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            srcChannel = null;
            dstChannel.close();
            dstChannel = null;
            tempFile.delete();
            tempFile = null;
            System.out.println("War " + outputName + " created");
            System.out.flush();
        } finally {
            if (srcChannel != null && srcChannel.isOpen()) {
                try {
                    srcChannel.close();
                } catch (IOException e1) {
                }
            }
            if (dstChannel != null && dstChannel.isOpen()) {
                try {
                    dstChannel.close();
                } catch (IOException e1) {
                }
            }
            if (jin != null) {
                try {
                    jin.close();
                    jin = null;
                } catch (IOException e1) {
                }
            }
            if (jout != null) {
                try {
                    jout.close();
                    jout = null;
                } catch (IOException e1) {
                }
            }
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }
