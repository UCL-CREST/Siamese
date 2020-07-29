    public ComponentTester getTester(Class forClass) {
        if (!(Component.class.isAssignableFrom(forClass))) {
            throw new IllegalArgumentException("Class must derive from " + "Component");
        }
        ComponentTester tester = (ComponentTester) testers.get(forClass);
        if (tester == null) {
            String cname = simpleClassName(forClass);
            try {
                String pkg = ComponentTester.class.getPackage().getName();
                cname = pkg + "." + cname + "Tester";
                Class testClass = Class.forName(cname);
                Constructor ctor = testClass.getConstructor(new Class[] { ComponentFinder.class });
                tester = (ComponentTester) ctor.newInstance(new Object[] { this });
                testers.put(forClass, tester);
                tested.put(tester, forClass);
            } catch (InvocationTargetException ite) {
                Log.warn(ite);
            } catch (NoSuchMethodException nsm) {
                tester = getTester(forClass.getSuperclass());
            } catch (InstantiationException ie) {
                tester = getTester(forClass.getSuperclass());
            } catch (IllegalAccessException iae) {
                tester = getTester(forClass.getSuperclass());
            } catch (ClassNotFoundException cnf) {
                tester = getTester(forClass.getSuperclass());
            }
        }
        return tester;
    }
