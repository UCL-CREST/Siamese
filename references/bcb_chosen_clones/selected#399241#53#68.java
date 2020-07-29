    public LayoutSelectorImpl(Graph g, Class layoutClass) {
        availableLayout.add(MyCircleLayout.class);
        availableLayout.add(MyFRLayout.class);
        availableLayout.add(MyFRLayout2.class);
        if (!availableLayout.contains(layoutClass)) availableLayout.add(layoutClass);
        layoutSelectionDispatcher = new LayoutSelectionDispatcherImpl();
        Layout defaultLayout = null;
        try {
            defaultLayout = (Layout) layoutClass.getConstructor(Graph.class).newInstance(g);
        } catch (Exception ex) {
            ex.printStackTrace();
            defaultLayout = new MyCircleLayout(g);
        }
        selectedLayout = defaultLayout;
        typedLayout.put(selectedLayout.getClass(), selectedLayout);
    }
