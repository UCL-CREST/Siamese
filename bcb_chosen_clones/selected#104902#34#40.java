    public IBonesaVisualization newVisualizationInstance(JPanel panel) {
        try {
            return className.getConstructor(JPanel.class).newInstance(panel);
        } catch (Throwable e) {
            return null;
        }
    }
