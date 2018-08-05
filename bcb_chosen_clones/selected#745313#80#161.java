    public static boolean start(RootDoc root) {
        Logger log = Logger.getLogger("DocletGenerator");
        if (destination == null) {
            try {
                ruleListenerDef = IOUtils.toString(GeneratorOfXmlSchemaForConvertersDoclet.class.getResourceAsStream("/RuleListenersFragment.xsd"), "UTF-8");
                String fn = System.getenv("annocultor.xconverter.destination.file.name");
                fn = (fn == null) ? "./../../../src/site/resources/schema/XConverterInclude.xsd" : fn;
                destination = new File(fn);
                if (destination.exists()) {
                    destination.delete();
                }
                FileOutputStream os;
                os = new FileOutputStream(new File(destination.getParentFile(), "XConverter.xsd"));
                IOUtils.copy(new AutoCloseInputStream(GeneratorOfXmlSchemaForConvertersDoclet.class.getResourceAsStream("/XConverterTemplate.xsd")), os);
                os.close();
                os = new FileOutputStream(destination);
                IOUtils.copy(new AutoCloseInputStream(GeneratorOfXmlSchemaForConvertersDoclet.class.getResourceAsStream("/XConverterInclude.xsd")), os);
                os.close();
            } catch (Exception e) {
                try {
                    throw new RuntimeException("On destination " + destination.getCanonicalPath(), e);
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
        try {
            String s = Utils.loadFileToString(destination.getCanonicalPath(), "\n");
            int breakPoint = s.indexOf(XSD_TEXT_TO_REPLACED_WITH_GENERATED_XML_SIGNATURES);
            if (breakPoint < 0) {
                throw new Exception("Signature not found in XSD: " + XSD_TEXT_TO_REPLACED_WITH_GENERATED_XML_SIGNATURES);
            }
            String preambula = s.substring(0, breakPoint);
            String appendix = s.substring(breakPoint);
            destination.delete();
            PrintWriter schemaWriter = new PrintWriter(destination);
            schemaWriter.print(preambula);
            ClassDoc[] classes = root.classes();
            for (int i = 0; i < classes.length; ++i) {
                ClassDoc cd = classes[i];
                PrintWriter documentationWriter = null;
                if (getSuperClasses(cd).contains(Rule.class.getName())) {
                    for (ConstructorDoc constructorDoc : cd.constructors()) {
                        if (constructorDoc.isPublic()) {
                            if (isMeantForXMLAccess(constructorDoc)) {
                                if (documentationWriter == null) {
                                    File file = new File("./../../../src/site/xdoc/rules." + cd.name() + ".xml");
                                    documentationWriter = new PrintWriter(file);
                                    log.info("Generating doc for rule " + file.getCanonicalPath());
                                    printRuleDocStart(cd, documentationWriter);
                                }
                                boolean initFound = false;
                                for (MethodDoc methodDoc : cd.methods()) {
                                    if ("init".equals(methodDoc.name())) {
                                        if (methodDoc.parameters().length == 0) {
                                            initFound = true;
                                            break;
                                        }
                                    }
                                }
                                if (!initFound) {
                                }
                                printConstructorSchema(constructorDoc, schemaWriter);
                                if (documentationWriter != null) {
                                    printConstructorDoc(constructorDoc, documentationWriter);
                                }
                            }
                        }
                    }
                }
                if (documentationWriter != null) {
                    printRuleDocEnd(documentationWriter);
                }
            }
            schemaWriter.print(appendix);
            schemaWriter.close();
            log.info("Saved to " + destination.getCanonicalPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }
