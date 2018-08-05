    public PropertyCollectionController createController(PropertyCollectionController parentController, PropertyCollection model) {
        if (model == null) {
            throw new NullPointerException();
        }
        Class controllerClass = (Class) _controllerCreationMap.get(model.getType());
        PropertyCollectionController createdController = null;
        if (controllerClass == null) {
            if (model instanceof PropertyMap) {
                PropertyMap mapModel = (PropertyMap) model;
                PropertyInstance fixedInstance = mapModel.getFixedInstance();
                if (fixedInstance != null) {
                    createdController = createListController(parentController, model, fixedInstance.getInstanceType());
                    return createdController;
                }
            }
        } else {
            try {
                Class[] classArgs = null;
                Constructor constructor = controllerClass.getConstructor(classArgs);
                Object[] objectArgs = null;
                createdController = (PropertyCollectionController) constructor.newInstance(objectArgs);
            } catch (Exception e) {
                AssertUtility.exception(e);
            }
        }
        if (createdController == null) {
            createdController = new DirectPropertyCollectionController();
        }
        Object addedObject = parentController.getChildCollectionControllerMap().addPropertyInstance(parentController.getControlledCollection().getInstanceKey(model), null, createdController);
        AssertUtility.assertSame(createdController, addedObject);
        createdController.setIsAddAvailable(false);
        createdController.setIsDeleteAvailable(true);
        createdController.setControlledCollection(model);
        return createdController;
    }
