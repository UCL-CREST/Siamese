    protected void lookupForParsedElementAnnotations(JClassType t) {
        List<JMethod> methods = getParsableElementMethods(t);
        if (methods != null) {
            for (JMethod method : methods) {
                ParsedElement elementAnnotation = method.getAnnotation(ParsedElement.class);
                if (elementAnnotation.type() == ParsedElement.Types.SYNC) {
                    try {
                        String contents = "";
                        URL url = getClass().getClassLoader().getResource(elementAnnotation.file());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            contents += line;
                        }
                        reader.close();
                        ParsedElementDescriptor elementDescriptor = new ParsedElementDescriptor(method.getName(), contents.replaceAll("\"", "'"), elementAnnotation.type());
                        this.parsedElementList.add(elementDescriptor);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ParsedElementDescriptor elementDescriptor = new ParsedElementDescriptor(method.getName(), elementAnnotation.file(), elementAnnotation.type());
                    this.parsedElementList.add(elementDescriptor);
                }
            }
        }
    }
