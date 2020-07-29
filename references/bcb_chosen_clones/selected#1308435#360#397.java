    @Override
    protected void addPages() {
        try {
            List<EditorPageDefinition> pages = getPageDefinitions();
            String prevId;
            String nextId;
            for (EditorPageDefinition pageDef : pages) {
                int index = pages.indexOf(pageDef);
                String className = pageDef.getServiceEditorClass();
                Class<?> classInstance = null;
                if (bundlesToLook != null) {
                    logger.finest("Loading class:" + className + " from bundle:" + bundlesToLook);
                    classInstance = bundlesToLook.loadClass(className);
                } else {
                    classInstance = Class.forName(className);
                }
                AbstractBaseEditorPage page = (AbstractBaseEditorPage) classInstance.getConstructor(AbstractBaseEditor.class, String.class, String.class).newInstance(this, pageDef.getId(), pageDef.getTitle());
                if (index == 0) {
                    prevId = "";
                } else {
                    prevId = pages.get(index - 1).getId();
                }
                if (index == (pages.size() - 1)) {
                    nextId = "";
                } else {
                    nextId = pages.get(index + 1).getId();
                }
                initializePage(page, pageDef);
                page.setSiblings(prevId, nextId);
                addPage(page);
            }
            addAdditionalPages();
            registerEditorActions();
        } catch (Exception exception) {
            ControlFactory.showStackTrace("Exception occured while creating the XAware Editor.", exception);
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }
