    public Node createSimpleNodeForBO(IBusinessObject businessObject, NodesFactoryKeys configuration) {
        Map<NodesFactoryKeys, Class> nodeMap = this.factoryMap.get(businessObject.getClass());
        if (nodeMap == null) {
            return null;
        }
        Class nodeClass = nodeMap.get(configuration);
        if (nodeClass == null) {
            return null;
        }
        try {
            Constructor nodeConstructor = nodeClass.getConstructor(businessObject.getClass());
            return (Node) nodeConstructor.newInstance(businessObject);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }
