    ServiceDescription getServiceDescription() throws ConfigurationException {
        final XPath pathsXPath = this.xPathFactory.newXPath();
        try {
            final Node serviceDescriptionNode = (Node) pathsXPath.evaluate(ConfigurationFileTagsV1.SERVICE_DESCRIPTION_ELEMENT_XPATH, this.configuration, XPathConstants.NODE);
            final String title = getMandatoryElementText(serviceDescriptionNode, ConfigurationFileTagsV1.TITLE_ELEMENT);
            ServiceDescription.Builder builder = new ServiceDescription.Builder(title, Migrate.class.getCanonicalName());
            Property[] serviceProperties = getServiceProperties(serviceDescriptionNode);
            builder.author(getMandatoryElementText(serviceDescriptionNode, ConfigurationFileTagsV1.CREATOR_ELEMENT));
            builder.classname(this.canonicalServiceName);
            builder.description(getOptionalElementText(serviceDescriptionNode, ConfigurationFileTagsV1.DESCRIPTION_ELEMENT));
            final String serviceVersion = getOptionalElementText(serviceDescriptionNode, ConfigurationFileTagsV1.VERSION_ELEMENT);
            final Tool toolDescription = getToolDescriptionElement(serviceDescriptionNode);
            String identifier = getOptionalElementText(serviceDescriptionNode, ConfigurationFileTagsV1.IDENTIFIER_ELEMENT);
            if (identifier == null || "".equals(identifier)) {
                try {
                    final MessageDigest identDigest = MessageDigest.getInstance("MD5");
                    identDigest.update(this.canonicalServiceName.getBytes());
                    final String versionInfo = (serviceVersion != null) ? serviceVersion : "";
                    identDigest.update(versionInfo.getBytes());
                    final URI toolIDURI = toolDescription.getIdentifier();
                    final String toolIdentifier = toolIDURI == null ? "" : toolIDURI.toString();
                    identDigest.update(toolIdentifier.getBytes());
                    final BigInteger md5hash = new BigInteger(identDigest.digest());
                    identifier = md5hash.toString(16);
                } catch (NoSuchAlgorithmException nsae) {
                    throw new RuntimeException(nsae);
                }
            }
            builder.identifier(identifier);
            builder.version(serviceVersion);
            builder.tool(toolDescription);
            builder.instructions(getOptionalElementText(serviceDescriptionNode, ConfigurationFileTagsV1.INSTRUCTIONS_ELEMENT));
            builder.furtherInfo(getOptionalURIElement(serviceDescriptionNode, ConfigurationFileTagsV1.FURTHER_INFO_ELEMENT));
            builder.logo(getOptionalURIElement(serviceDescriptionNode, ConfigurationFileTagsV1.LOGO_ELEMENT));
            builder.serviceProvider(this.serviceProvider);
            final DBMigrationPathFactory migrationPathFactory = new DBMigrationPathFactory(this.configuration);
            final MigrationPaths migrationPaths = migrationPathFactory.getAllMigrationPaths();
            builder.paths(MigrationPathConverter.toPlanetsPaths(migrationPaths));
            builder.inputFormats(migrationPaths.getInputFormatURIs().toArray(new URI[0]));
            builder.parameters(getUniqueParameters(migrationPaths));
            builder.properties(serviceProperties);
            return builder.build();
        } catch (XPathExpressionException xPathExpressionException) {
            throw new ConfigurationException(String.format("Failed parsing the '%s' element in the '%s' element.", ConfigurationFileTagsV1.SERVICE_DESCRIPTION_ELEMENT_XPATH, this.configuration.getNodeName()), xPathExpressionException);
        } catch (NullPointerException nullPointerException) {
            throw new ConfigurationException(String.format("Failed parsing the '%s' element in the '%s' element.", ConfigurationFileTagsV1.SERVICE_DESCRIPTION_ELEMENT_XPATH, this.configuration.getNodeName()), nullPointerException);
        }
    }
