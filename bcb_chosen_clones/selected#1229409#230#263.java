    private Object[] toArray(Object[] dest, boolean copied) {
        int destIndex = 0;
        for (ConcurrentWeakKeyHashMap.Segment<E, ?> segment : this.map.segments) {
            ConcurrentWeakKeyHashMap.HashEntry<E, ?>[] table = segment.table;
            for (ConcurrentWeakKeyHashMap.HashEntry<E, ?> e : table) {
                while (e != null) {
                    E key = e.get();
                    if (key != null) {
                        if (destIndex == dest.length) {
                            Object[] newDest = (Object[]) Array.newInstance(dest.getClass().getComponentType(), (destIndex == 0) ? 16 : (destIndex << 1));
                            System.arraycopy(dest, 0, newDest, 0, destIndex);
                            dest = newDest;
                            copied = true;
                        }
                        dest[destIndex++] = key;
                    } else {
                        e.value = null;
                    }
                    e = e.next;
                }
            }
            table = null;
        }
        if (destIndex < dest.length) {
            if (copied) {
                Object[] newDest = new Object[destIndex];
                System.arraycopy(dest, 0, newDest, 0, destIndex);
                dest = newDest;
            } else {
                dest[destIndex] = null;
            }
        }
        return dest;
    }
