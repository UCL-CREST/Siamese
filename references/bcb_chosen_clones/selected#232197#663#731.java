    public void updateProperties(Element elem) {
        beginUpdate();
        Element e = elem.getChild(ELEMENT_MINIATURES);
        if (e == null) {
            return;
        }
        Iterator iter = e.getAttributes().iterator();
        while (iter.hasNext()) {
            try {
                Attribute attrib = (Attribute) iter.next();
                if (attrib.getName().equals(ATTRIBUTE_SERIAL_NUMBER)) {
                    setSerial(attrib.getIntValue());
                }
            } catch (Exception ex) {
            }
        }
        logger.debug(xout.outputString(e));
        iter = e.getChildren(ELEMENT_MINIATURE).iterator();
        while (iter.hasNext()) {
            Element mini = (Element) iter.next();
            if (mini.getAttributeValue(ATTRIBUTE_ACTION).equals(ACTION_NEW)) {
                Miniature miniature = null;
                try {
                    Class k = getClass().getClassLoader().loadClass(mini.getAttributeValue(MapConstants.MINIATURE_TYPE, Miniature.class.getName()));
                    Constructor cons = k.getConstructor(new Class[] {});
                    miniature = (Miniature) cons.newInstance(new Object[] {});
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (SecurityException se) {
                    miniature = new Miniature();
                    se.printStackTrace();
                } catch (NoSuchMethodException nsme) {
                    miniature = new Miniature();
                    nsme.printStackTrace();
                } catch (IllegalArgumentException iae) {
                    miniature = new Miniature();
                    iae.printStackTrace();
                } catch (InstantiationException ie) {
                    miniature = new Miniature();
                    ie.printStackTrace();
                } catch (IllegalAccessException iarge) {
                    miniature = new Miniature();
                    iarge.printStackTrace();
                } catch (InvocationTargetException ite) {
                    miniature = new Miniature();
                    ite.printStackTrace();
                }
                miniature.fromXML(mini);
                add(miniature);
            } else if (mini.getAttributeValue(ATTRIBUTE_ACTION).equals(ACTION_UPDATE)) {
                for (int loop = 0; loop < getMiniatures().size(); loop++) {
                    Miniature m = (Miniature) getMiniatures().get(loop);
                    if (m.getId().equals(mini.getAttributeValue(Miniature.ATTRIBUTE_ID))) {
                        m.fromXML(mini);
                        break;
                    }
                }
            } else if (mini.getAttributeValue(ATTRIBUTE_ACTION).equals(ACTION_DELETE)) {
                for (int loop = 0; loop < getMiniatures().size(); loop++) {
                    Miniature m = (Miniature) getMiniatures().get(loop);
                    if (m.getId().equals(mini.getAttributeValue(Miniature.ATTRIBUTE_ID))) {
                        getMiniatures().remove(m);
                        break;
                    }
                }
            }
        }
        endUpdate();
    }
