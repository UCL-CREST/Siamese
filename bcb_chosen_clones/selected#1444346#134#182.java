    public Exception toException() {
        Element detail = getFirstDetail();
        if (detail != null) {
            String namespace = detail.getNamespaceURI();
            String localName = detail.getLocalName();
            if (StringUtils.equals(NS_EXCEPTION, detail.getNamespaceURI()) && StringUtils.equals(EXCEPTION_ELEMENT, detail.getLocalName())) {
                String className = detail.getAttribute("className");
                if (className == null) {
                    className = "java.lang.Exception";
                }
                String message = detail.getAttribute("message");
                Exception ex = null;
                try {
                    if (message == null) {
                        ex = (Exception) Class.forName(className).newInstance();
                    } else {
                        Class exClass = Class.forName(className);
                        try {
                            Constructor ctor = exClass.getConstructor(new Class[] { String.class });
                            ex = (Exception) ctor.newInstance(new Object[] { message });
                        } catch (Exception e) {
                            ex = (Exception) exClass.newInstance();
                        }
                    }
                } catch (Throwable e) {
                    ex = new Exception(message);
                }
                NodeList children = detail.getChildNodes();
                ArrayList stElements = new ArrayList();
                for (int i = 0; i < children.getLength(); ++i) {
                    Node n = children.item(i);
                    if (n instanceof Element) {
                        Element ste = (Element) n;
                        if (StringUtils.equals(NS_EXCEPTION, ste.getNamespaceURI()) && StringUtils.equals(STACK_TRACE_ELEMENT, ste.getLocalName())) {
                            try {
                                StackTraceElement st = newStackTraceElement(ste);
                                stElements.add(st);
                            } catch (Exception e) {
                                LOG.error(e);
                            }
                        }
                    }
                }
                ex.setStackTrace((StackTraceElement[]) stElements.toArray(new StackTraceElement[0]));
                return ex;
            }
        }
        return null;
    }
