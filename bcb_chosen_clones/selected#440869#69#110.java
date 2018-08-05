        public ResourceMigratorBuilder createResourceMigratorBuilder(NotificationReporter reporter) {
            return new ResourceMigratorBuilder() {

                public ResourceMigrator getCompletedResourceMigrator() {
                    return new ResourceMigrator() {

                        public void migrate(InputMetadata meta, InputStream inputStream, OutputCreator outputCreator) throws IOException, ResourceMigrationException {
                            OutputStream outputStream = outputCreator.createOutputStream();
                            IOUtils.copy(inputStream, outputStream);
                        }
                    };
                }

                public void setTarget(Version version) {
                }

                public void startType(String typeName) {
                }

                public void setRegexpPathRecogniser(String re) {
                }

                public void setCustomPathRecogniser(PathRecogniser pathRecogniser) {
                }

                public void addRegexpContentRecogniser(Version version, String re) {
                }

                public void addCustomContentRecogniser(Version version, ContentRecogniser contentRecogniser) {
                }

                public XSLStreamMigratorBuilder createXSLStreamMigratorBuilder() {
                    return null;
                }

                public void addStep(Version inputVersion, Version outputVersion, StreamMigrator streamMigrator) {
                }

                public void endType() {
                }
            };
        }
