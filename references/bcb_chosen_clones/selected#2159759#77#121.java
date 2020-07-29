    public static Object getDao(Class laClasse) {
        String sDao = AtgConstantesWF.getValue(AtgConstantes.ATG_DAO_CONST_PREFIX + laClasse.getName());
        if (sDao == null) {
            String pattern = null;
            Enumeration<String> patterns = AtgConstantesWF.listKeyValue.keys();
            while ((sDao == null) && (patterns.hasMoreElements())) {
                pattern = patterns.nextElement();
                if ((AtgConstantes.ATG_DAO_CONST_PREFIX + laClasse.getName()).matches(pattern.replace(".", "\\.").replace("*", ".*"))) {
                    logFine(pattern);
                    sDao = AtgConstantesWF.getValue(pattern);
                    AtgConstantesWF.setValue("dao." + laClasse.getName(), sDao);
                }
            }
        }
        if (sDao == null) {
            sDao = AtgConstantesWF.getValue(AtgConstantes.ATG_DAO_CONST_DEFAULT);
        }
        if (sDao == null) {
            sDao = AtgConstantes.ATG_DAO_DEFAULT;
        }
        if (sDao.equals("hibernate") || sDao.equals("atg.metier.dao.hibernate.ATGDaoHibernate")) {
            logFinest("Dao retourné : " + sDao);
            return new ATGDaoHibernate(laClasse);
        } else if (sDao.equals("jdo") || sDao.equals("atg.metier.dao.jdo.ATGDaoJdo")) {
            logFinest("Dao retourné : " + sDao);
            return new ATGDaoJdo(laClasse);
        } else {
            try {
                Class classeDao = Class.forName(sDao, true, (new ATGDaoFactory()).getClass().getClassLoader());
                try {
                    Class[] paramContructeur = { Class.class };
                    Constructor leContructeur = classeDao.getConstructor(paramContructeur);
                    Object[] initargs = { laClasse };
                    logFinest("Dao retourné : " + sDao);
                    return leContructeur.newInstance(initargs);
                } catch (Exception e) {
                    logFinest("Dao retourné : " + sDao);
                    return classeDao.newInstance();
                }
            } catch (Exception e) {
                logSevere("Impossible d'instancier le dao '" + sDao + "' : " + e.getMessage());
                return null;
            }
        }
    }
