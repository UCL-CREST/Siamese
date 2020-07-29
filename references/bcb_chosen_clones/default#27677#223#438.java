    public ArrayList createClassList(String rootClass, ArrayList currentClassList) {
        if (exclude(rootClass)) return currentClassList;
        excludedClasses.add(rootClass);
        int index;
        Element thisElement = null;
        if (useDependenceFile) thisElement = getElementForClass(rootClass);
        if ((thisElement == null) && (useDependenceFile)) {
            thisElement = xmlDepDoc.createElement("class");
            thisElement.setAttribute("name", rootClass);
            rootElement.appendChild(thisElement);
            dependencyChanged = true;
        } else {
            if (useDependenceFile) {
                NodeList nList = thisElement.getElementsByTagName("depClass");
                int tmpLen = nList.getLength();
                for (int i = 0; i < tmpLen; i++) {
                    Element tmpElement = (Element) nList.item(i);
                    String depClass = tmpElement.getAttribute("name");
                    index = currentClassList.indexOf(depClass);
                    if (index == -1) {
                        currentClassList = createClassList(depClass, currentClassList);
                    }
                }
            }
        }
        if (useReflection) {
            Class thisClass = null;
            try {
                thisClass = Class.forName(rootClass);
            } catch (ClassNotFoundException cnfe) {
            } catch (NoClassDefFoundError ncdfe) {
            }
            if (thisClass != null) {
                Constructor[] cons;
                try {
                    cons = thisClass.getDeclaredConstructors();
                    for (int i = 0; i < cons.length; i++) {
                        Class[] constructorParams = cons[i].getParameterTypes();
                        for (int j = 0; j < constructorParams.length; j++) {
                            instNode(thisElement, constructorParams[j].getName());
                            index = currentClassList.indexOf(constructorParams[j].getName());
                            if (index == -1) {
                                currentClassList = createClassList(constructorParams[j].getName(), currentClassList);
                            }
                        }
                    }
                } catch (NoClassDefFoundError nce) {
                    String message = nce.getMessage();
                    int pos1 = message.lastIndexOf(" ");
                    String problemClassName = message.substring(pos1 + 1).replace('/', '.');
                    index = currentClassList.indexOf(problemClassName);
                    if (index == -1) {
                        currentClassList = createClassList(problemClassName, currentClassList);
                    }
                    instNode(thisElement, problemClassName);
                }
                Method[] methods;
                try {
                    methods = thisClass.getDeclaredMethods();
                    for (int i = 0; i < methods.length; i++) {
                        Class[] params = methods[i].getParameterTypes();
                        for (int j = 0; j < params.length; j++) {
                            instNode(thisElement, params[j].getName());
                            index = currentClassList.indexOf(params[j].getName());
                            if (index == -1) {
                                currentClassList = createClassList(params[j].getName(), currentClassList);
                            }
                        }
                        params = methods[i].getExceptionTypes();
                        for (int j = 0; j < params.length; j++) {
                            instNode(thisElement, params[j].getName());
                            index = currentClassList.indexOf(params[j].getName());
                            if (index == -1) {
                                currentClassList = createClassList(params[j].getName(), currentClassList);
                            }
                        }
                        Class fieldType = methods[i].getReturnType();
                        instNode(thisElement, fieldType.getName());
                        index = currentClassList.indexOf(fieldType.getName());
                        if (index == -1) {
                            currentClassList = createClassList(fieldType.getName(), currentClassList);
                        }
                    }
                } catch (NoClassDefFoundError nce) {
                    String message = nce.getMessage();
                    int pos1 = message.lastIndexOf(" ");
                    String problemClassName = message.substring(pos1 + 1).replace('/', '.');
                    index = currentClassList.indexOf(problemClassName);
                    if (index == -1) {
                        currentClassList = createClassList(problemClassName, currentClassList);
                    }
                    instNode(thisElement, problemClassName);
                }
                Field[] fields;
                try {
                    fields = thisClass.getFields();
                    for (int i = 0; i < fields.length; i++) {
                        Class fieldType = fields[i].getType();
                        instNode(thisElement, fieldType.getName());
                        index = currentClassList.indexOf(fieldType.getName());
                        if (index == -1) {
                            currentClassList = createClassList(fieldType.getName(), currentClassList);
                        }
                    }
                } catch (NoClassDefFoundError nce) {
                    String message = nce.getMessage();
                    int pos1 = message.lastIndexOf(" ");
                    String problemClassName = message.substring(pos1 + 1).replace('/', '.');
                    index = currentClassList.indexOf(problemClassName);
                    if (index == -1) {
                        currentClassList = createClassList(problemClassName, currentClassList);
                    }
                    instNode(thisElement, problemClassName);
                }
                Class[] interfaces;
                try {
                    interfaces = thisClass.getInterfaces();
                    for (int i = 0; i < interfaces.length; i++) {
                        instNode(thisElement, interfaces[i].getName());
                        index = currentClassList.indexOf(interfaces[i].getName());
                        if (index == -1) {
                            currentClassList = createClassList(interfaces[i].getName(), currentClassList);
                        }
                    }
                } catch (NoClassDefFoundError nce) {
                    String message = nce.getMessage();
                    int pos1 = message.lastIndexOf(" ");
                    String problemClassName = message.substring(pos1 + 1).replace('/', '.');
                    index = currentClassList.indexOf(problemClassName);
                    if (index == -1) {
                        currentClassList = createClassList(problemClassName, currentClassList);
                    }
                    instNode(thisElement, problemClassName);
                }
                Class superClass;
                try {
                    superClass = thisClass.getSuperclass();
                    if (superClass != null) {
                        instNode(thisElement, superClass.getName());
                        index = currentClassList.indexOf(superClass.getName());
                        if (index == -1) {
                            currentClassList = createClassList(superClass.getName(), currentClassList);
                        }
                    }
                } catch (NoClassDefFoundError nce) {
                    String message = nce.getMessage();
                    int pos1 = message.lastIndexOf(" ");
                    String problemClassName = message.substring(pos1 + 1).replace('/', '.');
                    index = currentClassList.indexOf(problemClassName);
                    if (index == -1) {
                        currentClassList = createClassList(problemClassName, currentClassList);
                    }
                    instNode(thisElement, problemClassName);
                }
                try {
                    Runtime rt = Runtime.getRuntime();
                    String classPath = System.getProperty("java.class.path");
                    String command = "javap -c -b -classpath " + classPath + " " + rootClass;
                    Process p = rt.exec(command);
                    BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String outLine;
                    String codeString = new String();
                    while ((outLine = out.readLine()) != null) {
                        Matcher m = fieldPattern.matcher(outLine);
                        while (m.find()) {
                            String fieldClassName = (m.group(1)).replace('/', '.');
                            instNode(thisElement, fieldClassName);
                            index = currentClassList.indexOf(fieldClassName);
                            if (index == -1) {
                                currentClassList = createClassList(fieldClassName, currentClassList);
                            }
                        }
                        m = methodPattern.matcher(outLine);
                        while (m.find()) {
                            String fieldClassName = m.group(1);
                            instNode(thisElement, fieldClassName);
                            index = currentClassList.indexOf(fieldClassName);
                            if (index == -1) {
                                currentClassList = createClassList(fieldClassName, currentClassList);
                            }
                        }
                        m = methodPattern2.matcher(outLine);
                        while (m.find()) {
                            String methodParameters = m.group(1);
                            Matcher m2 = internalMethodPattern.matcher(methodParameters);
                            while (m2.find()) {
                                String fieldClassName = (m2.group(1)).replace('/', '.');
                                instNode(thisElement, fieldClassName);
                                index = currentClassList.indexOf(fieldClassName);
                                if (index == -1) {
                                    currentClassList = createClassList(fieldClassName, currentClassList);
                                }
                            }
                        }
                        m = classPattern.matcher(outLine);
                        while (m.find()) {
                            String fieldClassName = m.group(1);
                            instNode(thisElement, fieldClassName);
                            index = currentClassList.indexOf(fieldClassName);
                            if (index == -1) {
                                currentClassList = createClassList(fieldClassName, currentClassList);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        index = currentClassList.indexOf(rootClass);
        if (index == -1) {
            currentClassList.add(rootClass);
        }
        excludedClasses.remove(rootClass);
        return currentClassList;
    }
