    @SuppressWarnings("unchecked")
    private void loadConverters() throws ConfigurationException {
        this.converterMap = new LinkedHashMap<Object, CosmosArgumentConverter>();
        this.converters = new ArrayList<CosmosArgumentConverter>();
        Collection<String> names = getByName(Constants.COSMOS_CONVERTERS_CONVERTER);
        try {
            int converterIndex = 0;
            for (String name : names) {
                String clazz = this.config.getString(String.format(Constants.COSMOS_CONVERTERS_CONVERTER_CLASS, converterIndex));
                String label = this.config.getString(String.format(Constants.COSMOS_CONVERTERS_CONVERTER_LABEL, converterIndex));
                Constructor<CosmosArgumentConverter> constructor = ((Class<CosmosArgumentConverter>) Class.forName(clazz)).getConstructor(String.class, String.class);
                CosmosArgumentConverter converter = constructor.newInstance(name, label);
                Collection<String> jdbcTypeValues = this.getByAttribute(String.format(Constants.COSMOS_CONVERTERS_CONVERTER_JDBC_TYPE, converterIndex), "value");
                Collection<Integer> jdbcTypes = new ArrayList<Integer>();
                if (jdbcTypeValues != null) {
                    for (String jdbcTypeValue : jdbcTypeValues) {
                        Integer jdbcType = Integer.valueOf(jdbcTypeValue);
                        jdbcTypes.add(jdbcType);
                        this.converterMap.put(jdbcType, converter);
                    }
                }
                converter.setMappedJdbcTypes(jdbcTypes);
                this.converterMap.put(name, converter);
                this.converters.add(converter);
                converterIndex++;
            }
        } catch (Exception e) {
            throw new ConfigurationException("Loading converters has some errors. ", e);
        }
    }
