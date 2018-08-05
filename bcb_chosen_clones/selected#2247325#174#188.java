    public static PZKSAbstractGenerateDialog createGeneraetDialog(Frame owner) {
        PZKSAbstractGenerateDialog dialog = null;
        String className = System.getProperty("generate-dialog");
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(Frame.class);
            dialog = (PZKSAbstractGenerateDialog) constructor.newInstance(owner);
        } catch (Exception e) {
            System.out.println("Error during creating generate dialog " + className + ". Default dialog will be used");
            System.out.println(e.getMessage());
            dialog = new PZKSLamaoGenerateDialog(owner);
        }
        return dialog;
    }
