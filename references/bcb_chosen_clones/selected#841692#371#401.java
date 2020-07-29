    public static PositionTransformation loadFieldTransformator() {
        PositionTransformation result = null;
        String name = null;
        if (Configuration.dimensions == 2) {
            name = Configuration.guiPositionTransformation2D;
        } else if (Configuration.dimensions == 3) {
            name = Configuration.guiPositionTransformation3D;
        } else {
            Main.fatalError("The 'dimensions' field in the configuration file is invalid. Valid values are either '2' for 2D or '3' for 3D. (Cannot create corresponding position transformation instance.)");
        }
        try {
            Class<?> c = Class.forName(name);
            Constructor<?> cons = c.getConstructor();
            result = (PositionTransformation) cons.newInstance();
        } catch (ClassNotFoundException e) {
            Main.fatalError("Cannot find the class " + name + " which contains the implementation for the field transformation. Please check the guiPositionTransformation field in the config file.");
        } catch (SecurityException e) {
            Main.fatalError("Cannot generate the field transformation object due to a security exception:\n\n" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Main.fatalError("The field transformation " + name + " must provide a constructor taking no arguments.\n\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            Main.fatalError("The field transformation " + name + " must provide a constructor taking no arguments.\n\n" + e.getMessage());
        } catch (InstantiationException e) {
            Main.fatalError("Classes usable as field transformators must be instantiable classes, i.e. no interfaces and not abstract.\n\n" + e.getMessage());
        } catch (IllegalAccessException e) {
            Main.fatalError("Cannot generate the field transformator object due to illegal access:\n\n" + e.getMessage());
        } catch (InvocationTargetException e) {
            Main.fatalError("Exception while instanciating " + name + ":\n\n" + e.getCause().getMessage());
        }
        return result;
    }
