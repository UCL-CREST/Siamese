        public Object get() {
            Object newArray;
            if (_beanContext != null && _beanContext.isDesignTime()) {
                if (_value == null) {
                    newArray = null;
                } else {
                    int length = Array.getLength(_value);
                    newArray = Array.newInstance(_value.getClass().getComponentType(), length);
                    System.arraycopy(_value, 0, newArray, 0, length);
                }
            } else {
                newArray = _value;
            }
            return newArray;
        }
