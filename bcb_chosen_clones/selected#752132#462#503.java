    private void constructComponent(boolean loadExtension) {
        try {
            Class[] types = new Class[0];
            Object[] params = new Object[0];
            Class swingClass = Class.forName(className);
            Constructor swingNew = swingClass.getConstructor(types);
            swingComp = (JDrawable) swingNew.newInstance(params);
            swingComp.initForEditing();
            String[] extList = swingComp.getExtensionList();
            if (loadExtension) {
                setExtensionList(extList);
                for (int i = 0; i < extList.length; i++) {
                    super.setExtendedParam(i, swingComp.getExtendedParam(extList[i]));
                }
                theFont = swingComp.getComponent().getFont();
                if (theFont == null) {
                    theFont = JDLabel.fontDefault;
                    swingComp.getComponent().setFont(theFont);
                }
                foreground = swingComp.getComponent().getForeground();
                background = swingComp.getComponent().getBackground();
                Border b = swingComp.getComponent().getBorder();
                if (b == lowerBevelBorder) {
                    border = LOWERED_BORDER;
                } else if (b == raiseBevelBorder) {
                    border = RAISED_BORDER;
                } else if (b == etchedBevelBorder) {
                    border = ETCHED_BORDER;
                }
            } else {
                for (int i = 0; i < extList.length; i++) {
                    if (!hasExtendedParam(extList[i])) {
                        addExtension(extList[i]);
                        super.setExtendedParam(extList[i], swingComp.getExtendedParam(extList[i]));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("JDraw.constructComponent() : " + e.getClass() + " while trying to construct " + className);
            swingComp = null;
        }
    }
