    public static void main(String[] args) {
        try {
            configurationModules = new ConfigurationModules();
        } catch (ExceptionInitialisation e) {
            UI.erreurFatale("Erreur lors du chargement des modules !", e);
        }
        configurationGenerale = new ConfigurationGenerale(configurationModules);
        try {
            configurationGenerale.chargeConfiguration(configurationModules, fichierConfigurationGenerale);
        } catch (FileNotFoundException e) {
            System.err.println("[INIT] Attention : Fichier de configuration générale non trouvé à l'emplacement \"" + fichierConfigurationGenerale + "\" !");
        } catch (InvalidPropertiesFormatException e) {
            UI.erreur("Le fichier de configuration général est mal formé !", e);
        } catch (IOException e) {
            UI.erreur("Erreur lors du chargement du fichier de configuration général.", e);
        }
        args = recolleArgumentsEspaces(args);
        int index = 0;
        while (index != -1 && index < args.length) {
            index = analyseArgument(args, index);
        }
        i18n = UI.getI18nInstance(Galaxiia.class);
        Thread.setDefaultUncaughtExceptionHandler(new GestionExceptions());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Throwable t) {
            UI.erreur("Impossible d'utiliser le thème système pour l'interface.", t);
        }
        UI.init();
        try {
            configurationGenerale.sauvegardeConfiguration();
        } catch (IOException e) {
            UI.erreur("Erreur lors de la sauvegarde du fichier de configuration générale.", e);
        }
        try {
            configurationGenerale.getInterfaceJoueur().getConstructor(new Class[] { args.getClass(), ConfigurationGenerale.class }).newInstance(new Object[] { args, configurationGenerale });
        } catch (Throwable t) {
            UI.erreurFatale("Impossible de démarrer l'interface !", t);
        }
    }
