    private static void backend(Element root, InferenceSystem inf) throws DescriptionFileException, ProverException {
        try {
            Node s = root.getElementsByTagName("backend").item(0);
            NamedNodeMap m = s.getAttributes();
            String classname = getAttr(m, "class");
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            Class backendClass = cl.loadClass(classname);
            Class[] parmtypes = { InferenceSystem.class };
            Constructor ctor = backendClass.getConstructor(parmtypes);
            Object[] args = { inf };
            Prover backend = (Prover) ctor.newInstance(args);
            NodeList elements = s.getChildNodes();
            for (int i = 0; i < elements.getLength(); ++i) {
                Node n = elements.item(i);
                if (n.getNodeType() != Node.ELEMENT_NODE) continue;
                NamedNodeMap attr = n.getAttributes();
                String tag = n.getNodeName();
                int l = attr.getLength();
                Map<String, String> attrs = new HashMap<String, String>();
                for (int j = 0; j < l; ++j) attrs.put(attr.item(j).getNodeName(), attr.item(j).getNodeValue());
                backend.setParam(tag, attrs);
            }
            inf.setBackend(backend);
        } catch (ClassNotFoundException cnfe) {
            throw new DescriptionFileException("class not found", cnfe);
        } catch (NoSuchMethodException nsme) {
            throw new DescriptionFileException("prover does not have appropriate constructor", nsme);
        } catch (IllegalAccessException iae) {
            throw new DescriptionFileException("constructor not accessible", iae);
        } catch (InvocationTargetException ite) {
            if (ite.getCause() instanceof ProverException) throw (ProverException) ite.getCause();
            throw new DescriptionFileException("an invocation target exception occurred", ite);
        } catch (InstantiationException ie) {
            throw new DescriptionFileException("an instantiation exception occured", ie);
        } catch (ProverException pe) {
            throw new DescriptionFileException("a prover exception occured", pe);
        }
    }
