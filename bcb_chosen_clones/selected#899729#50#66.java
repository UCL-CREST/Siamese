    public ImmutableList(T[] elements) {
        if (elements == null) throw new IllegalArgumentException("argument was null");
        boolean allImmutable = true;
        this.data = new Object[elements.length];
        if (ImmutableHelper.isImmutableType(elements.getClass().getComponentType())) System.arraycopy(elements, 0, data, 0, elements.length); else for (int i = 0; i < elements.length; i++) {
            T element = elements[i];
            if (element != null) {
                if (allImmutable) {
                    if (ImmutableHelper.isImmutableType(element.getClass())) this.data[i] = element; else allImmutable = false;
                }
                if (!allImmutable) this.data[i] = ImmutableHelper.handleValue("Array element", element);
            }
        }
        this.allImmutable = allImmutable;
        firstIndex = 0;
        size = elements.length;
    }
