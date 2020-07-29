    @SuppressWarnings("unchecked")
    public File export(ScopePathTO path) throws AdminFacadeException {
        File result = null;
        ZipOutputStream out = null;
        try {
            ICfgServiceFacade facade = delegate.getCfgServiceFacade();
            String allConfigurations = facade.getAllConfigurations(TREE_NAME, path);
            result = File.createTempFile("ConfigurationExport_", ".zip");
            out = new ZipOutputStream(new FileOutputStream(result));
            out.putNextEntry(new ZipEntry(ALL_CONFIGURATION_FILENAME));
            out.write(allConfigurations.getBytes());
            out.closeEntry();
            Collection<ComponentTO> components = facade.getComponents(TREE_NAME, path);
            for (ComponentTO component : components) {
                ComponentTypeEnumTO type = component.getType();
                if (type == ComponentTypeEnumTO.RESOURCE) {
                    Collection<String> resourceIds = facade.getResourceIds(TREE_NAME, path, component.getName());
                    for (String resourceID : resourceIds) {
                        byte[] bytes = facade.getResource(TREE_NAME, path, component.getName(), resourceID);
                        out.putNextEntry(new ZipEntry("resource_ " + component.getName() + "_" + resourceID + ".bin"));
                        out.write(bytes);
                        out.closeEntry();
                    }
                }
            }
            out.close();
        } catch (AdminToolException e) {
            throwWrappedException("Could not export configuration", e);
        } catch (IOException e) {
            throwWrappedException("Could not export configuration", new AdminToolException(ErrorCodeEnum.FILE_OUTPUT, null, e));
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignore) {
                }
            }
        }
        return result;
    }
