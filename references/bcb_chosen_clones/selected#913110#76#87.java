    public void migrate(InputMetadata meta, InputStream input, OutputCreator outputCreator) throws IOException, ResourceMigrationException {
        RestartInputStream restartInput = new RestartInputStream(input);
        Match match = resourceIdentifier.identifyResource(meta, restartInput);
        restartInput.restart();
        if (match != null) {
            reporter.reportNotification(notificationFactory.createLocalizedNotification(NotificationType.INFO, "migration-resource-migrating", new Object[] { meta.getURI(), match.getTypeName(), match.getVersionName() }));
            processMigrationSteps(match, restartInput, outputCreator);
        } else {
            reporter.reportNotification(notificationFactory.createLocalizedNotification(NotificationType.INFO, "migration-resource-copying", new Object[] { meta.getURI() }));
            IOUtils.copyAndClose(restartInput, outputCreator.createOutputStream());
        }
    }
