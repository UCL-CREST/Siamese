    public void deleteObject(PropertyModel propertyModel, Object selectedChoice) {
        Object values = propertyModel.getObject();
        if (values.getClass().isArray()) {
            Object[] valuesArray = (Object[]) values;
            int selectedChoiceIndex = getIndex(selectedChoice, valuesArray);
            if (selectedChoiceIndex >= 0) {
                Object[] newArray = (Object[]) Array.newInstance(valuesArray.getClass().getComponentType(), valuesArray.length - 1);
                System.arraycopy(valuesArray, 0, newArray, 0, selectedChoiceIndex);
                System.arraycopy(valuesArray, selectedChoiceIndex + 1, newArray, selectedChoiceIndex, valuesArray.length - selectedChoiceIndex - 1);
                PropertyUtil.setPropertyValue(propertyModel.getTarget(), propertyModel.getPropertyExpression(), newArray);
            }
        } else if (List.class.isAssignableFrom(values.getClass())) {
            List valuesCollection = (List) values;
            int selectedChoiceIndex = valuesCollection.indexOf(selectedChoice);
            if (selectedChoiceIndex != -1) {
                valuesCollection.remove(selectedChoiceIndex);
            }
        }
    }
