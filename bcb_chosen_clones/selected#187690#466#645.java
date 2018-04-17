    @SuppressWarnings("unchecked")
    private static void buildClasses(String projectPath, Document classDoc, Element definitions, ClassDiagram classDiagram, Node diagram) {
        NodeList classNodes = getNodeByName(diagram.getChildNodes(), ELEMENTS).getChildNodes();
        Node classDefinitionsContent = getNodeByName(getNodeByName(definitions.getChildNodes(), DEFINITIONS_CONTENT).getChildNodes(), CLASSES);
        NodeList classDefinitionNodes = classDefinitionsContent.getChildNodes();
        boolean dateTime = false;
        AttributeFactory factory = new AttributeFactory();
        final String invariantsRegex = "[\\s\\t]*inv[\\s\\t]+[\\w\\W]+[\\s\\t\\W\\w]";
        final String annotationsRegex = "@(\\w?)+";
        final String attrAnnotationRegex = "@attribute\\((.?)*\\)";
        final String attrValuesAnnotationRegex = "([\\w?]+)[\\s\\t]*[=][\\s\\t]*[\"]([-\\w?\\.?]+)[\"][\\s\\t]*";
        Pattern invariantsPattern = Pattern.compile(invariantsRegex);
        Pattern annotationsPattern = Pattern.compile(annotationsRegex);
        Pattern attrAnnotationPattern = Pattern.compile(attrAnnotationRegex);
        Pattern attrValuesAnnotationPattern = Pattern.compile(attrValuesAnnotationRegex);
        for (int i = 0; i < classNodes.getLength(); i++) {
            Node classNode = classNodes.item(i);
            if (classNode.getNodeType() == Node.ELEMENT_NODE) {
                String name = removeSpace(capitalize(getNodeValue(getNodeByName(classNode.getChildNodes(), NAME))));
                if (name.equalsIgnoreCase(DATETIME)) {
                    if (loadInternalClassesOption(projectPath)[2].equalsIgnoreCase("true")) {
                        dateTime = true;
                    } else if (loadInternalClassesOption(projectPath)[2].equalsIgnoreCase("false")) {
                        dateTime = false;
                    } else {
                        String answer = MessageBox.showOptionsDialogs("Do you want to use the tool internal " + DATETIME + " class instead of the one in your model?");
                        if (answer.startsWith("Yes")) {
                            dateTime = true;
                            if (answer.endsWith("SAVE")) {
                                saveInternalClassOption(TOOL_DATETIME, true, projectPath);
                            }
                        } else {
                            if (answer.endsWith("SAVE")) {
                                saveInternalClassOption(TOOL_DATETIME, false, projectPath);
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < classNodes.getLength(); i++) {
            Node classNode = classNodes.item(i);
            if (classNode.getNodeType() == Node.ELEMENT_NODE && getNodeAttributeValue(classNode, ATTRIBUTE_TYPE).equals(TYPE_CLASS)) {
                String name = removeSpace(capitalize(getNodeValue(getNodeByName(classNode.getChildNodes(), NAME))));
                if (name.equalsIgnoreCase(DATE)) {
                    if (dateTime) {
                        name = INTERNAL_DATE;
                    } else if (loadInternalClassesOption(projectPath)[0].equalsIgnoreCase("true")) {
                        name = INTERNAL_DATE;
                    } else if (loadInternalClassesOption(projectPath)[0].isEmpty()) {
                        String answer = MessageBox.showOptionsDialogs("Do you want to use the tool internal " + DATE + " class instead of the one in your model?");
                        if (answer.startsWith("Yes")) {
                            name = INTERNAL_DATE;
                            if (answer.endsWith("SAVE")) {
                                saveInternalClassOption(TOOL_DATE, true, projectPath);
                            }
                        } else {
                            if (answer.endsWith("SAVE")) {
                                saveInternalClassOption(TOOL_DATE, false, projectPath);
                            }
                        }
                    }
                } else if (name.equalsIgnoreCase(TIME)) {
                    if (dateTime) {
                        name = INTERNAL_TIME;
                    } else if (loadInternalClassesOption(projectPath)[1].equalsIgnoreCase("true")) {
                        name = INTERNAL_TIME;
                    } else if (loadInternalClassesOption(projectPath)[1].isEmpty()) {
                        String answer = MessageBox.showOptionsDialogs("Do you want to use the tool internal " + TIME + " class instead of the one in your model?");
                        if (answer.startsWith("Yes")) {
                            name = INTERNAL_TIME;
                            if (answer.endsWith("SAVE")) {
                                saveInternalClassOption(TOOL_TIME, true, projectPath);
                            }
                        } else {
                            if (answer.endsWith("SAVE")) {
                                saveInternalClassOption(TOOL_TIME, false, projectPath);
                            }
                        }
                    }
                } else if (name.equalsIgnoreCase(DATETIME)) {
                    if (dateTime) {
                        name = INTERNAL_DATETIME;
                    }
                }
                DmClass dmClass = new DmClass(getNodeAttributeValue(classNode, ID), name);
                for (int j = 0; j < classDefinitionNodes.getLength(); j++) {
                    Node classDefinitionNode = classDefinitionNodes.item(j);
                    if (classDefinitionNode.getNodeType() == Node.ELEMENT_NODE && getNodeAttributeValue(classDefinitionNode, ID_ELEMENT).equals(getNodeAttributeValue(classNode, ID_EXTERNAL))) {
                        Node attributesNode = getNodeByName(classDefinitionNode.getChildNodes(), ATTRIBUTES);
                        NodeList attributeNodes = attributesNode.getChildNodes();
                        for (int k = 0; k < attributeNodes.getLength(); k++) {
                            Node attributeNode = attributeNodes.item(k);
                            if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
                                String attrName = getNodeValue(getNodeByName(attributeNode.getChildNodes(), NAME));
                                String attrType = getNodeAttributeValue(attributeNode, TYPE);
                                dmClass.addAttribute(factory.createAttribute(attrName, attrType));
                            }
                        }
                        Node description = getNodeByName(classDefinitionNode.getChildNodes(), DESCRIPTION);
                        String descValue = getNodeValue(description);
                        Matcher mi = invariantsPattern.matcher(descValue);
                        if (mi.find()) {
                            dmClass.setInv(mi.group());
                        }
                        Matcher ma = annotationsPattern.matcher(descValue);
                        if (ma.find()) {
                            dmClass.setAutoGenerated(ma.group().equalsIgnoreCase("@processIn"));
                        }
                        Matcher mAttr = attrAnnotationPattern.matcher(descValue);
                        while (mAttr.find()) {
                            Matcher mAttrValue = attrValuesAnnotationPattern.matcher(mAttr.group(0));
                            HashMap<String, String> attributeParams = new HashMap<String, String>();
                            while (mAttrValue.find()) {
                                String attrName = mAttrValue.group(1);
                                String attrValue = mAttrValue.group(2);
                                attributeParams.put(attrName, attrValue);
                            }
                            String paramName = null;
                            if ((paramName = attributeParams.get("name")) != null) {
                                Attribute attr = dmClass.getAttrMap().get(paramName);
                                try {
                                    if (attributeParams.get("generator") != null) {
                                        Class genClass = Class.forName("com.scatter.model.dataGenerators." + attributeParams.get("generator"));
                                        Constructor constructor = genClass.getConstructor(null);
                                        DataGenerator generator = (DataGenerator) constructor.newInstance(null);
                                        attr.setGenerator(generator);
                                        if (attributeParams.get("minRange") != null && attributeParams.get("maxRange") != null) {
                                            boolean isInteger = attributeParams.get("generator").equalsIgnoreCase("IntegerGenerator") || attributeParams.get("generator").equalsIgnoreCase("DateTimeGenerator");
                                            Class partypesMinRange[] = new Class[1];
                                            partypesMinRange[0] = attributeParams.get("generator").equalsIgnoreCase("FloatGenerator") ? Double.TYPE : Integer.TYPE;
                                            Class partypesMaxRange[] = new Class[1];
                                            partypesMaxRange[0] = attributeParams.get("generator").equalsIgnoreCase("FloatGenerator") ? Double.TYPE : Integer.TYPE;
                                            Method setMinRangeMeth = null;
                                            try {
                                                setMinRangeMeth = attr.getClass().getMethod("setMinRange", partypesMinRange);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            int minRangeInt, maxRangeInt = 0;
                                            double minRangeDouble, maxRangeDouble;
                                            Method setMaxRangeMeth = attr.getClass().getMethod("setMaxRange", partypesMaxRange);
                                            if (isInteger) {
                                                minRangeInt = new Integer(attributeParams.get("minRange")).intValue();
                                                maxRangeInt = new Integer(attributeParams.get("maxRange")).intValue();
                                                setMinRangeMeth.invoke(attr, minRangeInt);
                                                setMaxRangeMeth.invoke(attr, maxRangeInt);
                                            } else {
                                                minRangeDouble = new Double(attributeParams.get("minRange")).doubleValue();
                                                maxRangeDouble = new Double(attributeParams.get("maxRange")).doubleValue();
                                                setMinRangeMeth.invoke(attr, minRangeDouble);
                                                setMaxRangeMeth.invoke(attr, maxRangeDouble);
                                            }
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                } catch (SecurityException e) {
                                    e.printStackTrace();
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                classDiagram.addClass(dmClass);
            }
        }
    }
