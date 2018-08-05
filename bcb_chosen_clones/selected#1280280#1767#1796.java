        private Object[] toArray(Object[] dest, boolean copied) {
            int destIndex = 0;
            for (final Segment<K, V> segment : ConcurrentWeakKeyHashMap.this.segments) {
                for (HashEntry<K, V> e : segment.table) {
                    while (e != null) {
                        final V value = e.value;
                        if (value != null) {
                            if (destIndex == dest.length) {
                                final Object[] newDest = (Object[]) Array.newInstance(dest.getClass().getComponentType(), (destIndex == 0) ? 16 : (destIndex << 1));
                                System.arraycopy(dest, 0, newDest, 0, destIndex);
                                dest = newDest;
                                copied = true;
                            }
                            dest[destIndex++] = value;
                        }
                        e = e.next;
                    }
                }
            }
            if (destIndex < dest.length) {
                if (copied) {
                    final Object[] newDest = new Object[destIndex];
                    System.arraycopy(dest, 0, newDest, 0, destIndex);
                    dest = newDest;
                } else {
                    dest[destIndex] = null;
                }
            }
            return dest;
        }
