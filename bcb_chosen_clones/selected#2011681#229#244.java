    @SuppressWarnings("unchecked")
    private R createRow() {
        R newRow = null;
        try {
            if (simple) {
                newRow = (R) new MappedTableRow();
            } else {
                newRow = (R) pclass.getConstructor(this.getClass()).newInstance(this);
            }
        } catch (InvocationTargetException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        } catch (NoSuchMethodException e) {
        }
        return newRow;
    }
