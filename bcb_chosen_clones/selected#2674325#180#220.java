            @Override
            public void widgetSelected(SelectionEvent e) {
                ListDialog typeSelectionDialog = new ListDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
                typeSelectionDialog.setContentProvider(new ArrayContentProvider() {

                    @Override
                    public Object[] getElements(Object inputElement) {
                        Class<?>[] wizards = { CreateAtopTaskWizard.class, CreateJmtTaskWizard.class };
                        return wizards;
                    }
                });
                typeSelectionDialog.setLabelProvider(new LabelProvider() {

                    @Override
                    public String getText(Object element) {
                        if (element instanceof Class<?>) {
                            Class<? extends INewTaskWizard> wizardClass = (Class<? extends INewTaskWizard>) element;
                            if (wizardClass.equals(CreateAtopTaskWizard.class)) return "ATOP Task"; else if (wizardClass.equals(CreateJmtTaskWizard.class)) return "JMT Task"; else return "UNKNOWN";
                        } else return "UNKNOWN";
                    }
                });
                typeSelectionDialog.setTitle("Add New Task");
                typeSelectionDialog.setMessage("Select the type of task to create :");
                typeSelectionDialog.setInput(new Integer(1));
                if (typeSelectionDialog.open() == WizardDialog.OK) {
                    if (typeSelectionDialog.getResult().length == 1) {
                        Class<? extends INewTaskWizard> wizardClass = (Class<? extends INewTaskWizard>) typeSelectionDialog.getResult()[0];
                        try {
                            Constructor<? extends INewTaskWizard> constr = wizardClass.getConstructor(ModelingProject.class, String.class);
                            INewTaskWizard wizard = constr.newInstance(getInputProject(), getInputProjectFileName());
                            WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), wizard);
                            if (dialog.open() == WizardDialog.OK) {
                                tasksListSectionPart.markStale();
                                tasksListSectionPart.markDirty();
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
