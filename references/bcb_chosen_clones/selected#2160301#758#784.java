    @SuppressWarnings("unchecked")
    public ComponentOperator getComponentOperator(ContainerOperator operator, String txt, Class jcomponentClass, Class operatorClass, int indexNumber) {
        Component c = locateComponent(operator, txt);
        if (c == null) {
            return null;
        }
        Container parent = c.getParent();
        Component[] components = parent.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i].equals(c)) {
                for (; i < components.length; i++) {
                    if (jcomponentClass.isInstance(components[i])) {
                        indexNumber--;
                        if (indexNumber >= 0) {
                            continue;
                        }
                        try {
                            return (ComponentOperator) operatorClass.getConstructor(jcomponentClass).newInstance(components[i]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
