        public Object remove(Object elem) throws PropertyVetoException {
            if (_value == null) return null;
            if (elem == null) throw new IllegalArgumentException();
            Object newArray = _value;
            for (int index = 0, length = Array.getLength(_value); index < length; index++) {
                if (Array.get(_value, index).equals(elem)) {
                    newArray = Array.newInstance(_value.getClass().getComponentType(), length - 1);
                    System.arraycopy(_value, 0, newArray, 0, index);
                    System.arraycopy(_value, index + 1, newArray, index, length - index - 1);
                    PropertyChangeEvent evt = null;
                    if (hasVetoListeners(_propertyName) || hasChangeListeners(_propertyName)) {
                        evt = new PropertyChangeEvent(AbstractBean.this, _propertyName, _value, newArray);
                    }
                    fireVetoableChange(evt);
                    _value = newArray;
                    firePropertyChange(evt);
                    if (_model != null) _model.fireIntervalRemoved(index, index);
                    break;
                }
            }
            return newArray;
        }
