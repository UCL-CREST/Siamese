                public void migrate(InputMetadata meta, InputStream inputStream, OutputCreator outputCreator) throws IOException, ResourceMigrationException {
                    OutputStream outputStream = outputCreator.createOutputStream();
                    IOUtils.copy(inputStream, outputStream);
                }
