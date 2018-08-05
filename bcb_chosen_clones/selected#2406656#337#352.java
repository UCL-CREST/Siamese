    public <T> T[] toArray(T[] a) {
        lock.readLock().lock();
        try {
            if (a.length < size) {
                Class<? extends T[]> type = (Class<? extends T[]>) a.getClass();
                T[] copy = ((Object) type == (Object) Object[].class) ? (T[]) new Object[size] : (T[]) Array.newInstance(type.getComponentType(), size);
                System.arraycopy(array, 0, copy, 0, Math.min(array.length, size));
                return copy;
            }
            System.arraycopy(array, 0, a, 0, size);
            if (a.length > size) a[size] = null;
            return a;
        } finally {
            lock.readLock().unlock();
        }
    }
