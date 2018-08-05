    protected Wizard constructWizard(final WizDefinition definition, final List branchNodes) throws DBException {
        String wizardClassString = definition.getWizardClass();
        Class wizardClass;
        try {
            wizardClass = Thread.currentThread().getContextClassLoader().loadClass(wizardClassString);
            if (!BranchingWizard.class.isAssignableFrom(wizardClass)) {
                throw new IllegalArgumentException("If you do not" + " implement a wizard" + " that derives from SequentialWizard and uses the same " + "constructor then you need to implement your own" + " Wizard Factory implementation");
            }
            Constructor c = wizardClass.getConstructor(new Class[] { WizardMonitor.class, BranchNode[].class });
            AbstractWizard emoWizard = (AbstractWizard) c.newInstance(new Object[] { constructMonitor(), (BranchNode[]) branchNodes.toArray(new BranchNode[branchNodes.size()]) });
            emoWizard.setId(new Integer(definition.getId()));
            emoWizard.setTitle(definition.getWizName());
            emoWizard.setSummary(definition.getSummary());
            setWizardForStepsThatNeedIt(emoWizard, branchNodes);
            return emoWizard;
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Unknown class: " + wizardClassString + " edit your wizard definition to solve this problem");
        } catch (InvocationTargetException ex) {
            throw new DBException("An Exception was thrown constructing the " + "wizard", ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("The wizard specified: " + wizardClassString + " does not have a public constructor" + " like the default.  You may need to implement your own " + "wizard factory.");
        } catch (InstantiationException ex) {
            throw new DBException("There was an error constructing your " + "wizard.", ex);
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("No appropriate constructor" + " found:" + ex.getMessage());
        }
    }
