    private CmsAdapter createAdapter(CmsDetail cfg) {
        Class type = cfg.getAdapter();
        assert type != null : "Adapter class cannot be null.";
        try {
            if (AbstractCmsAdapter.class.isAssignableFrom(type)) {
                Constructor constructor = type.getConstructor(SFrame.class, STemplateLayout.class, CmsDetail.class);
                return (CmsAdapter) constructor.newInstance(this, layout, cfg);
            } else {
                return (CmsAdapter) type.newInstance();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
