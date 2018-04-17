    @Override
    public void apply(PatchApplicator patchApplicator, Change change) {
        if (change instanceof PrimitiveArrayChange) {
            PrimitiveArrayChange primitiveArrayChange = (PrimitiveArrayChange) change;
            if (array == null) {
                array = Array.newInstance(getReference().getClass().getComponentType(), length);
            }
            for (Entry<Integer, Object> entry : primitiveArrayChange.changes.entrySet()) {
                Array.set(array, entry.getKey(), entry.getValue());
            }
            if (patchApplicator.isOverride()) {
                System.arraycopy(array, 0, getReference(), 0, length);
            }
        } else {
            raiseIncompatibleChange(change);
        }
    }
