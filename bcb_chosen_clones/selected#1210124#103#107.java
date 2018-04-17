                public String migratePolicy(InputStream stream, String url) throws ResourceMigrationException, IOException {
                    ByteArrayOutputCreator oc = new ByteArrayOutputCreator();
                    IOUtils.copyAndClose(stream, oc.getOutputStream());
                    return oc.getOutputStream().toString();
                }
