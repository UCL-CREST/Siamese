    @Override
    public <T> T[] toArray(T[] a) {
        synchronized (modLock) {
            if (a.length < this.size()) a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), this.size());
            System.arraycopy(this.store.elementData, 1, a, 0, this.size());
            if (a.length > this.size()) a[this.size()] = null;
            return a;
        }
    }
