    public static void main(String[] args) {
        String modelName = "";
        for (int argIndex = 0; argIndex < args.length; argIndex += 2) {
            if (argIndex + 1 >= args.length) {
                System.err.println("Wrong number of arguments");
                System.exit(1);
                return;
            }
            if ("-model".compareTo(args[argIndex]) == 0) {
                modelName = args[argIndex + 1];
            } else {
                System.err.println("Invalid argument:\"" + args[argIndex] + "\"");
                System.exit(1);
                return;
            }
        }
        IMandalDisplayModel model = null;
        if (modelName != null) {
            final Class<?> interfaceType;
            try {
                interfaceType = Class.forName("smartHexMandalzoom.MandalDisplayModel");
            } catch (ClassNotFoundException e) {
                System.err.println("Internal error: Couldn't find class object for interface MandalDisplayModel");
                System.exit(1);
                return;
            }
            final Class<?> modelType;
            try {
                modelType = Class.forName(modelName);
            } catch (ClassNotFoundException e) {
                System.err.println("Couldn't find class \"" + modelName + "\"");
                System.exit(1);
                return;
            }
            if (!interfaceType.isAssignableFrom(modelType)) {
                System.err.println("\"" + modelName + "\" does not implement ModelType");
                System.exit(1);
                return;
            }
            final Constructor<?> modelConstructor;
            try {
                modelConstructor = modelType.getConstructor(new Class<?>[0]);
            } catch (NoSuchMethodException e) {
                System.err.println("\"" + modelName + "\" has no default constructor");
                System.exit(1);
                return;
            }
            try {
                model = (IMandalDisplayModel) (modelConstructor.newInstance(new Object[0]));
            } catch (Exception e) {
                System.err.println("Failed to create model \"" + modelName + "\"");
                System.exit(1);
                return;
            }
        } else {
            System.err.println("No default model has been defined yet.");
            System.exit(1);
            return;
        }
        System.out.println("Created an instance of \"" + modelName + "\"");
        final IMandalDisplayModel finalModel = model;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI(finalModel);
            }
        });
    }
