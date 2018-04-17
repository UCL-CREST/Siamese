    @SuppressWarnings("unchecked")
    public void run() {
        try {
            Class thinletclass = Class.forName(getParameter("class"));
            try {
                content = (Thinlet) thinletclass.getConstructor(new Class[] { Applet.class }).newInstance(new Object[] { this });
            } catch (NoSuchMethodException nsme) {
                content = (Thinlet) thinletclass.newInstance();
            }
            removeAll();
            add(content, BorderLayout.CENTER);
        } catch (Throwable exc) {
            removeAll();
            add(new Label(exc.getClass().getName() + " " + exc.getMessage(), Label.CENTER), BorderLayout.CENTER);
        }
        doLayout();
        repaint();
    }
