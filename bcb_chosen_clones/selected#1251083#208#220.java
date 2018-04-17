    private <T extends Positionable> T spawnEntity(Class<T> clazz) {
        T object = null;
        try {
            Integer x = Integer.valueOf(editorPanel.getSelected().x);
            Integer y = Integer.valueOf(editorPanel.getSelected().y);
            Constructor constr = clazz.getConstructor(new Class[] { int.class, int.class });
            object = (T) constr.newInstance(new Object[] { x, y });
        } catch (Exception e) {
            Trace.error("I failed to instantiate " + clazz.getName());
            JOptionPane.showMessageDialog(this, "I failed to instantiate " + clazz.getName(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return object;
    }
