    @SuppressWarnings("unchecked")
    public Object getValue(String v) throws InvalidParameter {
        if (v == null) {
            if (this.isOptional()) {
                return null;
            } else if (this.getDefaultValue() != null) {
                return this.getDefaultValue();
            } else {
                throw new InvalidParameter(getName() + " is not optional");
            }
        }
        try {
            Constructor constructor = type.getConstructor(String.class);
            Object result = constructor.newInstance(v);
            if (min != null && !min.equals(max) && ((Comparable) min).compareTo(result) >= 0) {
                throw new InvalidParameter(getName() + " must be greater than " + min);
            } else if (max != null && !max.equals(min) && ((Comparable) max).compareTo(result) <= 0) {
                throw new InvalidParameter(getName() + " must be less than " + max);
            } else if (getItems().size() > 0) {
                if (!getItems().contains(result)) {
                    throw new InvalidParameter(getName() + " must be one of " + getItems());
                }
            }
            return result;
        } catch (Exception e) {
            throw new InvalidParameter("The value '" + v + "' is not valid for " + getName() + " : " + e.getMessage(), e);
        }
    }
