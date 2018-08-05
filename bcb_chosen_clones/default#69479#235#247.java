    protected final void addProps(Field f, Properties props) throws Exception {
        f.setAccessible(true);
        String name = f.getDeclaringClass().getSimpleName();
        Object vm = f.get(null);
        Method[] ms = vm.getClass().getMethods();
        for (Method m : ms) {
            m.setAccessible(true);
            if (Modifier.isAbstract(m.getModifiers())) continue;
            if (m.getParameterTypes().length > 0) continue;
            if (m.getReturnType().equals(void.class)) continue;
            props.put(convertCamelCase(m.getName()) + " [" + name + "]", m.invoke(vm, new Object[0]));
        }
    }
