    public Wizard createWizard(final Shell parent, final String definitionName, final Object inputData) throws WizardException {
        WizardDefinition def = (WizardDefinition) wizardDefs.get(definitionName);
        if (def == null) {
            def = (WizardDefinition) templateWizardDefs.get(definitionName);
        }
        controller = new WizardController(def, inputData);
        Wizard theWiz = null;
        try {
            final Class[] argTypes = { Shell.class, WizardController.class, WizardDefinition.class };
            final Object[] args = { parent, controller, def };
            theWiz = (Wizard) def.getWizardClass().getConstructor(argTypes).newInstance(args);
        } catch (final Exception e) {
            throw new WizardException("Error creating new Wizard instance : " + e.toString());
        }
        return theWiz;
    }
