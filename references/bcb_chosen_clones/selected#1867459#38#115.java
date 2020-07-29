    protected void load(String content, XMLNode parent) {
        this.parent = parent;
        content = normalizeXML(content);
        xmlHeader = findFirst(content, "<\\?.*\\?>");
        if (xmlHeader == null) {
            xmlHeader = "";
        }
        content = content.replaceAll("<\\?.*?>", "");
        id = findFirst(content, "^<[a-zA-Z0-9_:]+").replaceAll("^<", "").trim();
        String attributeContent = "";
        String internalContent = "";
        if (findFirst(content, "^<" + id + "[^<]*/>") != null) {
            attributeContent = findFirst(content, "^<" + id + "[^<]*/>").replaceAll("^<" + id, "").replaceAll("/>$", "").trim();
        } else {
            attributeContent = findFirst(content, "^<" + id + "[^<^>]*>").replaceAll("^<" + id, "").replaceAll(">$", "").trim();
            internalContent = content.replaceAll("^<" + id + "[^<^>]*>", "").replaceAll("</" + id + ">$", "");
        }
        attributes = parseAttributes(attributeContent);
        String parsedContent = internalContent;
        while (parsedContent.length() > 0) {
            String internalNodeName = findFirst(parsedContent, "^<[a-zA-Z0-9_:]+").replaceAll("^<", "").trim();
            String internalNodeBlock = findFirst(parsedContent, "^<" + internalNodeName + "[^<]*/>");
            if (internalNodeBlock == null) {
                int balance = 0;
                int fromIndex = 0;
                while (true) {
                    boolean isClosingNode = false;
                    int indexOfNodeName = parsedContent.indexOf(internalNodeName, fromIndex);
                    int indexOfOpeningBracket = parsedContent.lastIndexOf('<', indexOfNodeName);
                    int indexOfClosingBracket = indexOfNodeName;
                    while (true) {
                        indexOfClosingBracket = parsedContent.indexOf('>', indexOfClosingBracket);
                        if ((parsedContent.length() == indexOfClosingBracket + 1) || parsedContent.charAt(indexOfClosingBracket + 1) == '<') {
                            break;
                        }
                        indexOfClosingBracket++;
                    }
                    String nodeBody = parsedContent.substring(indexOfOpeningBracket, indexOfClosingBracket + 1);
                    if (findFirst(nodeBody, "<" + internalNodeName + "[^>]*/>") == null) {
                        if (findFirst(nodeBody, "</[^>]*>") != null) {
                            balance--;
                            isClosingNode = true;
                        } else {
                            balance++;
                        }
                    }
                    if (balance == 0 && isClosingNode) {
                        internalNodeBlock = parsedContent.substring(0, indexOfClosingBracket + 1);
                        break;
                    }
                    fromIndex = indexOfClosingBracket + 1;
                }
            }
            Class childClass = (Class) extenders.get(id + "." + internalNodeName);
            if (childClass == null) {
                childClass = (Class) extenders.get(internalNodeName);
            }
            if (childClass == null) {
                childClass = XMLNode.class;
            }
            XMLNode child = null;
            try {
                child = (XMLNode) childClass.getConstructor(new Class[] { String.class, XMLNode.class }).newInstance(new Object[] { internalNodeBlock, this });
            } catch (NoSuchMethodException e) {
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (children.get(child.getId()) == null) {
                children.put(child.getId(), new ArrayList());
            }
            ((List) children.get(child.getId())).add(child);
            parsedContent = parsedContent.substring(internalNodeBlock.length());
        }
    }
