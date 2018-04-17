    public static void loadCache() {
        Document doc = Utils.readFileAsXml("cache.xml");
        if (!doc.hasRootElement()) return;
        Element root = doc.getRootElement();
        Iterator iterator = root.getChildren().iterator();
        while (iterator.hasNext()) {
            Element elem = (Element) iterator.next();
            Attribute A = elem.getAttribute("class");
            String value = elem.getValue();
            String name = elem.getName();
            try {
                Class c = Class.forName(A.getValue());
                Constructor k = c.getConstructor(new Class[] { String.class });
                Object o = k.newInstance(new Object[] { value });
                putIntoCache(name, o);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (SecurityException ex) {
                ex.printStackTrace();
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            }
        }
    }
