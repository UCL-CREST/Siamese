    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        if (namespaceURI.equals(MathMLElementImpl.mathmlURI)) {
            String localName;
            Class elemClass;
            Constructor cnst;
            int index = qualifiedName.indexOf(':');
            if (index < 0) {
                localName = qualifiedName;
            } else {
                localName = qualifiedName.substring(index + 1);
            }
            elemClass = (Class) _elementTypesMathML.get(localName);
            if (elemClass != null) {
                try {
                    cnst = elemClass.getConstructor(_elemClassSigMathML);
                    return (Element) cnst.newInstance(new Object[] { this, qualifiedName });
                } catch (Exception except) {
                    Throwable thrw;
                    if (except instanceof InvocationTargetException) {
                        thrw = ((InvocationTargetException) except).getTargetException();
                    } else {
                        thrw = except;
                    }
                    System.out.println("Exception " + thrw.getClass().getName());
                    System.out.println(thrw.getMessage());
                    throw new IllegalStateException("Tag '" + qualifiedName + "' associated with an Element class that failed to construct.");
                }
            } else {
                return new MathMLElementImpl(this, qualifiedName);
            }
        } else {
            return super.createElementNS(namespaceURI, qualifiedName);
        }
    }
