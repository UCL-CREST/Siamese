    public static <T extends Control> VNative<T> create(Class<T> type, VPanel parent, int style) {
        try {
            Constructor<T> constructor = type.getConstructor(Composite.class, int.class);
            T control = constructor.newInstance(parent.getComposite(), style);
            VNative<T> vn = new VNative<T>(parent, style);
            vn.setControl(control);
            return vn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
