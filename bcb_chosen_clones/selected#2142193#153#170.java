    public Component addComponent(NodeList nodes, String class_name) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, InstantiationException, IllegalAccessException {
        Class component_class = Class.forName(class_name);
        Class[] cargs = new Class[1];
        Object[] oargs = new Object[1];
        cargs[0] = Node.class;
        Component res = null;
        for (int i = 0; i < nodes.getLength(); ++i) {
            Node n = nodes.item(i);
            oargs[0] = n;
            Constructor constructor = component_class.getConstructor(cargs);
            Component component = (Component) constructor.newInstance(oargs);
            treeData.componentMap.put(n, component);
            if (null == res) {
                res = component;
            }
        }
        return res;
    }
