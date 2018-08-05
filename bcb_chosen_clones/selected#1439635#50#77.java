    public static long sizeOf(Class clazz, int userData) {
        long size = 0;
        Object[] objects = new Object[100];
        try {
            Constructor c;
            try {
                c = clazz.getConstructor(long.class);
            } catch (Exception e) {
                c = null;
            }
            if (c == null) c = clazz.getConstructor(int.class);
            Object primer = c.newInstance(userData);
            long startingMemoryUse = getUsedMemory();
            for (int i = 0; i < objects.length; i++) {
                objects[i] = c.newInstance(userData);
                fill(objects[i], userData);
                optimize(objects[i]);
            }
            long endingMemoryUse = getUsedMemory();
            float approxSize = (endingMemoryUse - startingMemoryUse) / 100f;
            size = Math.round(approxSize);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("WARNING:couldn't instantiate" + clazz);
            e.printStackTrace();
        }
        return size;
    }
