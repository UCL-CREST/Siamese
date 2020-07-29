    public Component getSplitPane(final Component leftPane, final Component centerPane) {
        if (leftPane == null) {
            return centerPane;
        }
        if (centerPane == null) {
            return leftPane;
        }
        Container retval = null;
        try {
            final Class jsplitClass = Class.forName("javax.swing.JSplitPane");
            final Class[] params = { Integer.TYPE, Component.class, Component.class };
            final Constructor jsplitConstructor = jsplitClass.getConstructor(params);
            final Field horizontalField = jsplitClass.getField("HORIZONTAL_SPLIT");
            final Object horizontal = horizontalField.get(jsplitClass);
            final Object[] args = { horizontal, leftPane, centerPane };
            retval = (Container) jsplitConstructor.newInstance(args);
        } catch (final Throwable ignored) {
            retval = new Panel(new BorderLayout());
            retval.add(leftPane, BorderLayout.WEST);
            retval.add(centerPane, BorderLayout.CENTER);
        }
        return retval;
    }
