    public Element createElement(String tagName) throws DOMException {
        Class elemClass;
        Constructor cnst;
        elemClass = (Class) _elementTypesWML.get(tagName);
        if (elemClass != null) {
            try {
                cnst = elemClass.getConstructor(_elemClassSigWML);
                return (Element) cnst.newInstance(new Object[] { this, tagName });
            } catch (Exception except) {
                Throwable thrw;
                if (except instanceof java.lang.reflect.InvocationTargetException) thrw = ((java.lang.reflect.InvocationTargetException) except).getTargetException(); else thrw = except;
                System.out.println("Exception " + thrw.getClass().getName());
                System.out.println(thrw.getMessage());
                throw new IllegalStateException("Tag '" + tagName + "' associated with an Element class that failed to construct.");
            }
        }
        return new WMLElementImpl(this, tagName);
    }
