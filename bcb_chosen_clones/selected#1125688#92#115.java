    @SuppressWarnings("rawtypes")
    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
            Class<?> dynClass = myClassLoader.loadClass("be.potame.cavadium." + evt.getActionCommand());
            java.lang.reflect.Constructor constructeur = dynClass.getConstructor(new Class[] { Class.forName("be.potame.cavadium.Cavadium") });
            constructeur.newInstance(new Object[] { this });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
