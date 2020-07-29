        public Object add(Object newValue) throws PropertyVetoException {
            if (newValue == null) throw new IllegalArgumentException();
            Object newArray;
            int index;
            if (_value == null) {
                index = 0;
                newArray = Array.newInstance(newValue.getClass(), index + 1);
            } else {
                index = Array.getLength(_value);
                newArray = Array.newInstance(_value.getClass().getComponentType(), index + 1);
                System.arraycopy(_value, 0, newArray, 0, index);
            }
            Array.set(newArray, index, newValue);
            PropertyChangeEvent evt = null;
            if (hasVetoListeners(_propertyName) || hasChangeListeners(_propertyName)) {
                evt = new PropertyChangeEvent(AbstractBean.this, _propertyName, _value, newArray);
            }
            fireVetoableChange(evt);
            _value = newArray;
            firePropertyChange(evt);
            if (_model != null) _model.fireIntervalAdded(index, index);
            return newArray;
        }
