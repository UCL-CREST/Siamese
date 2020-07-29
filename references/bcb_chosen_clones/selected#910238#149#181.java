    public void set(String name, int index, Object value) {
        DynaProperty dynaProperty = getDynaProperty(name);
        Class type = dynaProperty.getType();
        if (!type.isArray() && !List.class.isAssignableFrom(type)) {
            throw new MorphException("Non-Indexed property name: " + name + " index: " + index);
        }
        Object prop = dynaValues.get(name);
        if (prop == null) {
            if (List.class.isAssignableFrom(type)) {
                prop = new ArrayList();
            } else {
                prop = Array.newInstance(type.getComponentType(), index + 1);
            }
            dynaValues.put(name, prop);
        }
        if (prop.getClass().isArray()) {
            if (index >= Array.getLength(prop)) {
                Object tmp = Array.newInstance(type.getComponentType(), index + 1);
                System.arraycopy(prop, 0, tmp, 0, Array.getLength(prop));
                prop = tmp;
                dynaValues.put(name, tmp);
            }
            Array.set(prop, index, value);
        } else if (prop instanceof List) {
            List l = (List) prop;
            if (index >= l.size()) {
                for (int i = l.size(); i <= index + 1; i++) {
                    l.add(null);
                }
            }
            ((List) prop).set(index, value);
        }
    }
