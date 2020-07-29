    private Number getValue(String tag) {
        Number curr;
        try {
            Element vv = (Element) el.getChild(tag);
            Attribute attr = vv.getAttribute(ATTRIBUTE_TYPE);
            Class type = Class.forName(attr.getValue());
            attr = vv.getAttribute(ATTRIBUTE_VALUE);
            Constructor constructor = type.getConstructor(new Class[] { String.class });
            curr = (Number) constructor.newInstance(new Object[] { attr.getValue() });
        } catch (ClassNotFoundException e) {
            curr = null;
        } catch (SecurityException e) {
            curr = null;
        } catch (NoSuchMethodException e) {
            curr = null;
        } catch (IllegalArgumentException e) {
            curr = null;
        } catch (InstantiationException e) {
            curr = null;
        } catch (InvocationTargetException e) {
            curr = null;
        } catch (IllegalAccessException e) {
            curr = null;
        }
        return curr;
    }
