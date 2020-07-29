    public static ImmerseDialog createImmerseDialog(Window window, String title) {
        ImmerseDialog dialog = null;
        String className = System.getProperty("immerse-dialog");
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(Window.class, String.class);
            dialog = (ImmerseDialog) constructor.newInstance(window, title);
        } catch (Exception e) {
            System.out.println("Error during creating immerse dialog " + className + ". Default dialog will be used");
            System.out.println(e.getMessage());
            dialog = new PZKSLamaoImmerseDialog(window, title);
        }
        return dialog;
    }
