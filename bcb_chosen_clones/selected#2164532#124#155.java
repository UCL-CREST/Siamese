    private Repository _createRepository(Results r, SymbolRepository sRepository) {
        DatabaseDriver driver = null;
        try {
            Class<?> c = Class.forName(sRepository.pDriver);
            Constructor co = c.getConstructor(new Class<?>[0]);
            driver = (DatabaseDriver) co.newInstance(new Object[0]);
        } catch (Exception ex) {
            r.addError("could not load database driver '" + sRepository.pDriver + "': " + ex);
            return null;
        }
        if (sRepository.pMaxActive != null) {
            sRepository.aMaxActive = _getIntParameter(r, sRepository.pMaxActive, "max-active");
        } else {
            sRepository.aMaxActive = 100;
        }
        if (sRepository.pMaxIdle != null) {
            sRepository.aMaxIdle = _getIntParameter(r, sRepository.pMaxIdle, "max-idle");
        } else {
            sRepository.aMaxIdle = 100;
        }
        if (sRepository.pMaxWait != null) {
            sRepository.aMaxWait = _getIntParameter(r, sRepository.pMaxWait, "max-wait");
        } else {
            sRepository.aMaxWait = 10;
        }
        if (sRepository.pRemoveAbandonedTimeout != null) {
            sRepository.aRemoveAbandonedTimeout = _getIntParameter(r, sRepository.pRemoveAbandonedTimeout, "remove-abandoned-timeout");
        } else {
            sRepository.aRemoveAbandonedTimeout = 60;
        }
        return new Repository(driver, sRepository);
    }
