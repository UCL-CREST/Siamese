    public static void main(String[] args) {
        try {
            final Class startupClass = Class.forName("org.gudy.azureus2.ui.swt.Main");
            final Constructor constructor = startupClass.getConstructor(new Class[] { String[].class });
            constructor.newInstance(new Object[] { args });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
