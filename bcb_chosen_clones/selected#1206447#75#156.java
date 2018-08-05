    public void deploy(final File extension) {
        log.info("Deploying new extension from {}", extension.getPath());
        RequestContextHolder.setRequestContext(new RequestContext(SZoneConfig.getDefaultZoneName(), SZoneConfig.getAdminUserName(SZoneConfig.getDefaultZoneName()), new BaseSessionContext()));
        RequestContextHolder.getRequestContext().resolve();
        JarInputStream warIn;
        try {
            warIn = new JarInputStream(new FileInputStream(extension), true);
        } catch (IOException e) {
            log.warn("Unable to open extension WAR at " + extension.getPath(), e);
            return;
        }
        SAXReader reader = new SAXReader(false);
        reader.setIncludeExternalDTDDeclarations(false);
        String extensionPrefix = extension.getName().substring(0, extension.getName().lastIndexOf("."));
        File extensionDir = new File(extensionBaseDir, extensionPrefix);
        extensionDir.mkdirs();
        File extensionWebDir = new File(this.extensionWebDir, extensionPrefix);
        extensionWebDir.mkdirs();
        try {
            for (JarEntry entry = warIn.getNextJarEntry(); entry != null; entry = warIn.getNextJarEntry()) {
                File inflated = new File(entry.getName().startsWith(infPrefix) ? extensionDir : extensionWebDir, entry.getName());
                if (entry.isDirectory()) {
                    log.debug("Creating directory at {}", inflated.getPath());
                    inflated.mkdirs();
                    continue;
                }
                inflated.getParentFile().mkdirs();
                FileOutputStream entryOut = new FileOutputStream(inflated);
                if (!entry.getName().endsWith(configurationFileExtension)) {
                    log.debug("Inflating file resource to {}", inflated.getPath());
                    IOUtils.copy(warIn, entryOut);
                    entryOut.close();
                    continue;
                }
                try {
                    final Document document = reader.read(new TeeInputStream(new CloseShieldInputStream(warIn), entryOut, true));
                    Attribute schema = document.getRootElement().attribute(schemaAttribute);
                    if (schema == null || StringUtils.isBlank(schema.getText())) {
                        log.debug("Inflating XML with unrecognized schema to {}", inflated.getPath());
                        continue;
                    }
                    if (schema.getText().contains(definitionsSchemaNamespace)) {
                        log.debug("Inflating and registering definition from {}", inflated.getPath());
                        document.getRootElement().add(new AbstractAttribute() {

                            private static final long serialVersionUID = -7880537136055718310L;

                            public QName getQName() {
                                return new QName(extensionAttr, document.getRootElement().getNamespace());
                            }

                            public String getValue() {
                                return extension.getName().substring(0, extension.getName().lastIndexOf("."));
                            }
                        });
                        definitionModule.addDefinition(document, true);
                        continue;
                    }
                    if (schema.getText().contains(templateSchemaNamespace)) {
                        log.debug("Inflating and registering template from {}", inflated.getPath());
                        templateService.addTemplate(document, true, zoneModule.getDefaultZone());
                        continue;
                    }
                } catch (DocumentException e) {
                    log.warn("Malformed XML file in extension war at " + extension.getPath(), e);
                    return;
                }
            }
        } catch (IOException e) {
            log.warn("Malformed extension war at " + extension.getPath(), e);
            return;
        } finally {
            try {
                warIn.close();
            } catch (IOException e) {
                log.warn("Unable to close extension war at " + extension.getPath(), e);
                return;
            }
            RequestContextHolder.clear();
        }
        log.info("Extension deployed successfully from {}", extension.getPath());
    }
