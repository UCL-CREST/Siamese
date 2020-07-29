    private void createTasksListSection(final ScrolledForm form, FormToolkit toolkit) {
        tasksListSection = toolkit.createSection(form.getBody(), Section.EXPANDED | Section.TITLE_BAR | Section.DESCRIPTION);
        tasksListSection.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
        tasksListSection.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
        tasksListSection.setText("Tasks");
        tasksListSection.setDescription("This section provides information about tasks available for the model.");
        tasksListSection.addExpansionListener(new ExpansionAdapter() {

            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(false);
            }
        });
        Composite client = toolkit.createComposite(tasksListSection, SWT.WRAP);
        tasksListSection.setClient(client);
        toolkit.paintBordersFor(client);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        client.setLayout(layout);
        GridData gd = new GridData();
        tasksListViewer = new TableViewer(client);
        gd.widthHint = 250;
        gd.heightHint = 250;
        tasksListViewer.getControl().setLayoutData(gd);
        tasksListViewer.setContentProvider(new ArrayContentProvider() {

            @Override
            public Object[] getElements(Object inputElement) {
                ModelingProject prj = (ModelingProject) inputElement;
                return prj.getAllResources(AbstractTask.class).values().toArray();
            }
        });
        tasksListViewer.setLabelProvider(new LabelProvider() {

            @Override
            public String getText(Object element) {
                return ((IResource) element).getName();
            }
        });
        tasksListViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (selection.size() == 1) {
                    removeTaskButton.setEnabled(true);
                    executeTaskButton.setEnabled(true);
                    if (selection.getFirstElement() instanceof AtopTranslationTask) {
                        AtopTaskForm taskForm = new AtopTaskForm(taskSection, SWT.WRAP, taskSectionPart, (AtopTranslationTask) selection.getFirstElement(), TasksPage.this);
                        taskForm.initialize();
                        replaceTaskForm(taskForm);
                    } else if (selection.getFirstElement() instanceof JmtTranslationTask) {
                        JmtTaskForm taskForm = new JmtTaskForm(taskSection, SWT.WRAP, taskSectionPart, (JmtTranslationTask) selection.getFirstElement(), TasksPage.this);
                        taskForm.initialize();
                        replaceTaskForm(taskForm);
                    }
                } else {
                    removeTaskButton.setEnabled(false);
                    executeTaskButton.setEnabled(false);
                    resetTaskForm();
                }
            }
        });
        tasksListViewer.setInput(((ModelingProjectEditorInput) getEditorInput()).getProject());
        Composite buttonComposite = toolkit.createComposite(client, SWT.WRAP);
        gd = new GridData(GridData.FILL_VERTICAL);
        buttonComposite.setLayoutData(gd);
        layout = new GridLayout();
        layout.numColumns = 1;
        buttonComposite.setLayout(layout);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        Button button = toolkit.createButton(buttonComposite, "Add", SWT.PUSH);
        button.setLayoutData(gd);
        button.addSelectionListener(new SelectionAdapter() {

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
        });
        removeTaskButton = toolkit.createButton(buttonComposite, "Remove", SWT.PUSH);
        removeTaskButton.setLayoutData(gd);
        removeTaskButton.setEnabled(false);
        removeTaskButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IStructuredSelection selection = (IStructuredSelection) tasksListViewer.getSelection();
                AbstractTask task = (AbstractTask) selection.getFirstElement();
                try {
                    getInputProject().removeResource(task.getId());
                    tasksListSectionPart.markStale();
                    tasksListSectionPart.markDirty();
                } catch (ResourceException e1) {
                }
            }
        });
        executeTaskButton = toolkit.createButton(buttonComposite, "Execute", SWT.PUSH);
        executeTaskButton.setLayoutData(gd);
        executeTaskButton.setEnabled(false);
        executeTaskButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!getEditor().isDirty()) {
                    IStructuredSelection selection = (IStructuredSelection) tasksListViewer.getSelection();
                    AbstractTask task = (AbstractTask) selection.getFirstElement();
                    if (task.canExecute()) {
                        try {
                            task.execute();
                            tasksListSectionPart.markStale();
                            tasksListSectionPart.markDirty();
                        } catch (TaskException e1) {
                            ErrorDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Task Execution", "Task execution failed", new Status(IStatus.ERROR, "q_impress", e1.getMessage(), e1));
                        }
                    } else ErrorDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Task Execution", "Task execution failed", new Status(IStatus.ERROR, "q_impress", "Task is not configured correctly"));
                } else ErrorDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Task Execution", "Task execution failed", new Status(IStatus.ERROR, "q_impress", "Model must be saved before task can be executed"));
            }
        });
    }
