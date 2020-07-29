    Object getSwing(Element element, Object obj) throws Exception {
        Factory factory = engine.getTaglib().getFactory(element.getName());
        String id = element.getAttribute(Parser.ATTR_ID) != null ? element.getAttribute(Parser.ATTR_ID).getValue().trim() : null;
        boolean unique = !engine.getIdMap().containsKey(id);
        boolean constructed = false;
        if (!unique) {
            throw new IllegalStateException("id already in use: " + id + " : " + engine.getIdMap().get(id).getClass().getName());
        }
        if (factory == null) {
            throw new Exception("Unknown TAG, implementation class not defined: " + element.getName());
        }
        if (element.getAttribute(Parser.ATTR_INCLUDE) != null) {
            StringTokenizer st = new StringTokenizer(element.getAttribute(Parser.ATTR_INCLUDE).getValue(), "#");
            element.removeAttribute(Parser.ATTR_INCLUDE);
            Document doc = new org.jdom.input.SAXBuilder().build(this.engine.getClassLoader().getResourceAsStream(st.nextToken()));
            Element xelem = find(doc.getRootElement(), st.nextToken());
            if (xelem != null) {
                moveContent(xelem, element);
            }
        }
        if (element.getAttribute(Parser.ATTR_REFID) != null) {
            element = (Element) element.clone();
            cloneAttributes(element);
            element.removeAttribute(Parser.ATTR_REFID);
        }
        List attributes = element.getAttributes();
        if (obj == null) {
            Object initParameter = null;
            if (element.getAttribute(Parser.ATTR_INITCLASS) != null) {
                StringTokenizer st = new StringTokenizer(element.getAttributeValue(Parser.ATTR_INITCLASS), "( )");
                element.removeAttribute(Parser.ATTR_INITCLASS);
                try {
                    if (st.hasMoreTokens()) {
                        Class initClass = Class.forName(st.nextToken());
                        try {
                            Method factoryMethod = initClass.getMethod(Parser.GETINSTANCE, null);
                            if (Modifier.isStatic(factoryMethod.getModifiers())) {
                                initParameter = factoryMethod.invoke(null, null);
                            }
                        } catch (NoSuchMethodException nsme) {
                        }
                        if (initParameter == null && st.hasMoreTokens()) {
                            try {
                                Constructor ctor = initClass.getConstructor(new Class[] { String.class });
                                String pattern = st.nextToken();
                                initParameter = ctor.newInstance(new Object[] { pattern });
                            } catch (NoSuchMethodException e) {
                            } catch (SecurityException e) {
                            } catch (InstantiationException e) {
                            } catch (IllegalAccessException e) {
                            } catch (IllegalArgumentException e) {
                            } catch (InvocationTargetException e) {
                            }
                        }
                        if (initParameter == null) {
                            initParameter = initClass.newInstance();
                        }
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println(Parser.ATTR_INITCLASS + " not instantiated : " + e.getLocalizedMessage() + e);
                } catch (SecurityException e) {
                    System.err.println(Parser.ATTR_INITCLASS + " not instantiated : " + e.getLocalizedMessage() + e);
                } catch (IllegalAccessException e) {
                    System.err.println(Parser.ATTR_INITCLASS + " not instantiated : " + e.getLocalizedMessage() + e);
                } catch (IllegalArgumentException e) {
                    System.err.println(Parser.ATTR_INITCLASS + " not instantiated : " + e.getLocalizedMessage() + e);
                } catch (InvocationTargetException e) {
                    System.err.println(Parser.ATTR_INITCLASS + " not instantiated : " + e.getLocalizedMessage() + e);
                } catch (InstantiationException e) {
                    System.err.println(Parser.ATTR_INITCLASS + " not instantiated : " + e.getLocalizedMessage() + e);
                } catch (RuntimeException re) {
                    throw re;
                } catch (Exception e) {
                    throw new Exception(Parser.ATTR_INITCLASS + " not instantiated : " + e.getLocalizedMessage(), e);
                }
            }
            if (element.getAttribute(Parser.ATTR_CLASS) != null) {
                Class customComponent = Class.forName(element.getAttributeValue(Parser.ATTR_CLASS), true, engine.getClassLoader());
                factory = new DefaultFactory(customComponent);
            }
            if (element.getName().toLowerCase().equals("timechart")) {
                factory = new ChartFactory(element);
            } else if (element.getName().toLowerCase().equals("graph")) {
                factory = new TimeSeriesFactory(element);
            }
            obj = initParameter != null ? factory.newInstance(new Object[] { initParameter }) : factory.newInstance();
            constructed = true;
            if (id != null) {
                engine.getIdMap().put(id, obj);
            }
        }
        Attribute actionAttr = element.getAttribute("Action");
        if (actionAttr != null) {
            element.removeAttribute(actionAttr);
            attributes.add(0, actionAttr);
        }
        if (element.getAttribute("Text") == null && 0 < element.getTextTrim().length()) {
            attributes.add(new Attribute("Text", element.getTextTrim()));
        }
        List remainingAttrs = applyAttributes(obj, factory, attributes);
        LayoutManager layoutMgr = obj instanceof Container ? ((Container) obj).getLayout() : null;
        Iterator it = element.getChildren().iterator();
        while (it != null && it.hasNext()) {
            Element child = (Element) it.next();
            if ("buttongroup".equalsIgnoreCase(child.getName())) {
                int k = JMenu.class.isAssignableFrom(obj.getClass()) ? ((JMenu) obj).getItemCount() : ((Container) obj).getComponentCount();
                getSwing(child, obj);
                int n = JMenu.class.isAssignableFrom(obj.getClass()) ? ((JMenu) obj).getItemCount() : ((Container) obj).getComponentCount();
                ButtonGroup btnGroup = new ButtonGroup();
                if (null != child.getAttribute(Parser.ATTR_ID)) {
                    engine.getIdMap().put(child.getAttribute(Parser.ATTR_ID).getValue(), btnGroup);
                }
                while (k < n) {
                    putIntoBtnGrp(JMenu.class.isAssignableFrom(obj.getClass()) ? ((JMenu) obj).getItem(k++) : ((Container) obj).getComponent(k++), btnGroup);
                }
                continue;
            }
            Attribute constrnAttr = child.getAttribute(Parser.ATTR_CONSTRAINTS);
            Object constrains = null;
            if (constrnAttr != null && layoutMgr != null) {
                child.removeAttribute(Parser.ATTR_CONSTRAINTS);
                constrains = ConstraintsConverter.convert(layoutMgr.getClass(), constrnAttr);
            }
            Element grandchild = child.getChild("gridbagconstraints");
            if (grandchild != null) {
                addChild(obj, getSwing(child, null), getSwing(grandchild, null));
            } else if (!child.getName().equals("gridbagconstraints")) {
                addChild(obj, getSwing(child, null), constrains);
            }
        }
        if (remainingAttrs != null && 0 < remainingAttrs.size()) {
            remainingAttrs = applyAttributes(obj, factory, remainingAttrs);
            if (remainingAttrs != null) {
                it = remainingAttrs.iterator();
                while (it != null && it.hasNext()) {
                    Attribute attr = (Attribute) it.next();
                    if (JComponent.class.isAssignableFrom(obj.getClass())) {
                        ((JComponent) obj).putClientProperty(attr.getName(), attr.getValue());
                        if (SwingEngine.DEBUG_MODE) {
                            System.out.println("ClientProperty put: " + obj.getClass().getName() + "(" + id + "): " + attr.getName() + "=" + attr.getValue());
                        }
                    } else {
                        if (SwingEngine.DEBUG_MODE) {
                            System.err.println(attr.getName() + " not applied for tag: <" + element.getName() + ">");
                        }
                    }
                }
            }
        }
        return (constructed ? obj : null);
    }
