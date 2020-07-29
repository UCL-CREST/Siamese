    BaseNodehandler loadJavaNodehandler(String name, Element e) {
        try {
            String classname = "net.etherstorm.jOpenRPG.nodehandlers." + name;
            Class c = getClass().getClassLoader().loadClass(classname);
            Class[] arg_types = { Element.class };
            java.lang.reflect.Constructor ctor = c.getConstructor(arg_types);
            Object[] args = { e };
            BaseNodehandler bnh = (BaseNodehandler) ctor.newInstance(args);
            return bnh;
        } catch (ClassNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
            return null;
        }
    }
