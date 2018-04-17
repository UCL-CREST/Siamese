    private void initClass(Class thrC, int num) {
        Constructor[] cons = thrC.getConstructors();
        for (int i = 0; i < cons.length; i++) {
            try {
                Throwable obj = (Throwable) cons[i].newInstance(args[num]);
                t_Class(obj, num);
                break;
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
                fail("IllegalAccessException while creating instance of: " + thrC.getName());
            } catch (InstantiationException e) {
                fail("InstantiationException while creating instance of: " + thrC.getName());
            } catch (InvocationTargetException e) {
                fail("InvocationTargetException while creating instance of: " + thrC.getName());
            }
            if (i == cons.length - 1) {
                fail("Failed to create newInstance of: " + thrC.getName());
            }
        }
    }
