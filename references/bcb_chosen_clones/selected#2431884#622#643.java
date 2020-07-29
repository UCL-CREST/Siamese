    @SuppressWarnings("unchecked")
    private void loadDomains() throws ConfigurationException {
        this.domainMap = new LinkedHashMap<String, CosmosDomain>();
        Collection<String> names = getByName(Constants.COSMOS_DOMAINS_DOMAIN);
        try {
            int domainIndex = 0;
            for (String name : names) {
                String clazz = this.config.getString(String.format(Constants.COSMOS_DOMAINS_DOMAIN_CLASS, domainIndex));
                String type = this.config.getString(String.format(Constants.COSMOS_DOMAINS_DOMAIN_TYPE, domainIndex));
                CosmosDomainType domainType = CosmosDomainType.valueOf(type);
                Constructor<CosmosDomain> constructor = ((Class<CosmosDomain>) Class.forName(clazz)).getConstructor(String.class, CosmosDomainType.class, int.class);
                CosmosDomain domain = constructor.newInstance(name, domainType, domainIndex);
                if (domainType == CosmosDomainType.master) {
                    this.master = domain;
                }
                this.domainMap.put(name, domain);
                domainIndex++;
            }
        } catch (Exception e) {
            throw new ConfigurationException("Loading domains has some errors. ", e);
        }
    }
