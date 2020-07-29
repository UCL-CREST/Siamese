    public boolean update(double tdiff, UnitEngine ue) {
        if (cancel) {
            complete = true;
            return true;
        }
        time += tdiff;
        if (time >= buildTime) {
            Class<?>[] arguments = { Owner.class, double.class, double.class };
            try {
                Object[] parameters = { builder.getOwner(), builder.getLocation()[0], builder.getLocation()[1] };
                Constructor<? extends Unit> c = u.getConstructor(arguments);
                Unit u = c.newInstance(parameters);
                ue.registerUnit(u);
            } catch (Exception e) {
                e.printStackTrace();
            }
            complete = true;
            return true;
        }
        return false;
    }
