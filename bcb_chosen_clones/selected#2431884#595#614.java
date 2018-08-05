    @SuppressWarnings("unchecked")
    private void loadExecuters() throws ConfigurationException {
        this.executers = new ArrayList<CosmosExecuter>();
        Collection<String> names = getByName(Constants.COSMOS_EXECUTERS_EXECUTER);
        try {
            int executerIndex = 0;
            for (String name : names) {
                String clazz = this.config.getString(String.format(Constants.COSMOS_EXECUTERS_EXECUTER_CLASS, executerIndex));
                String label = this.config.getString(String.format(Constants.COSMOS_EXECUTERS_EXECUTER_LABEL, executerIndex));
                String category = this.config.getString(String.format(Constants.COSMOS_EXECUTERS_EXECUTER_CATEGORY, executerIndex));
                String description = this.config.getString(String.format(Constants.COSMOS_EXECUTERS_EXECUTER_DESCRIPTION, executerIndex));
                Constructor<CosmosExecuter> constructor = ((Class<CosmosExecuter>) Class.forName(clazz)).getConstructor(String.class, String.class, String.class, String.class);
                CosmosExecuter converter = constructor.newInstance(name, label, description, category);
                this.executers.add(converter);
                executerIndex++;
            }
        } catch (Exception e) {
            throw new ConfigurationException("Loading converters has some errors. ", e);
        }
    }
