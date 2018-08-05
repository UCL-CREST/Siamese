    public PropertyCollectionController createListController(PropertyCollectionController parentController, PropertyCollection model, PropertyType typeInList) {
        Class controllerClass = (Class) _listControllerCreationMap.get(typeInList);
        PropertyCollectionController createdController = null;
        if (controllerClass == null) {
            createdController = new DirectPropertyCollectionController();
        } else {
            try {
                Class[] classArg = null;
                Constructor constructor = controllerClass.getConstructor(classArg);
                Object[] objectArg = null;
                createdController = (PropertyCollectionController) constructor.newInstance(objectArg);
            } catch (Exception e) {
                AssertUtility.exception(e);
            }
        }
        if (createdController == null) {
            createdController = new DirectPropertyCollectionController();
        }
        Object addedObject = parentController.getChildCollectionControllerMap().addPropertyInstance(parentController.getControlledCollection().getInstanceKey(model), null, createdController);
        AssertUtility.assertSame(createdController, addedObject);
        createdController.setIsAddAvailable(true);
        createdController.setIsDeleteAvailable(false);
        createdController.setControlledCollection(model);
        return createdController;
    }
