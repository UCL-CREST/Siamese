    public Plot getSelectedPlot() {
        try {
            if (comboType.getSelectedItem() instanceof ComboTypeEntry) {
                ComboTypeEntry cte = (ComboTypeEntry) comboType.getSelectedItem();
                return (Plot) cte.plotClass.getConstructor(ExperimentController.class).newInstance(expController);
            }
        } catch (Exception e) {
        }
        return null;
    }
