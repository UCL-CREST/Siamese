    protected static void checkAttribute(ModuleAttributeInfo att, ComplexType mbean, CrawlerSettings settings, HttpServletRequest request, boolean expert) {
        Object currentAttribute = null;
        try {
            currentAttribute = mbean.getAttribute(settings, att.getName());
        } catch (Exception e) {
            logger.severe("Failed getting " + mbean.getAbsoluteName() + " attribute " + att.getName() + ": " + e.getMessage());
            return;
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("MBEAN: " + mbean.getAbsoluteName() + " " + att.getName() + " TRANSIENT " + att.isTransient() + " " + att.isExpertSetting() + " " + expert);
        }
        if (att.isTransient() == false && (att.isExpertSetting() == false || expert)) {
            if (currentAttribute instanceof ComplexType) {
                writeNewOrderFile((ComplexType) currentAttribute, settings, request, expert);
            } else {
                String attName = att.getName();
                String attAbsoluteName = mbean.getAbsoluteName() + "/" + attName;
                boolean override = (request.getParameter(attAbsoluteName + ".override") != null) && (request.getParameter(attAbsoluteName + ".override").equals("true"));
                if (settings == null || override) {
                    if (currentAttribute instanceof ListType) {
                        try {
                            ListType list = (ListType) currentAttribute;
                            Class cls = list.getClass();
                            Constructor constructor = cls.getConstructor(String.class, String.class);
                            list = (ListType) constructor.newInstance(list.getName(), list.getDescription());
                            String[] elems = request.getParameterValues(attAbsoluteName);
                            for (int i = 0; elems != null && i < elems.length; i++) {
                                list.add(elems[i]);
                            }
                            writeAttribute(attName, attAbsoluteName, mbean, settings, list);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.severe("Setting new list values on " + attAbsoluteName + ": " + e.getMessage());
                            return;
                        }
                    } else {
                        writeAttribute(attName, attAbsoluteName, mbean, settings, request.getParameter(attAbsoluteName));
                    }
                } else if (settings != null && override == false) {
                    try {
                        mbean.unsetAttribute(settings, attName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.severe("Unsetting attribute on " + attAbsoluteName + ": " + e.getMessage());
                        return;
                    }
                }
            }
        }
    }
