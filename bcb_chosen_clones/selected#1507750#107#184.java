    public void outputLogStats(OutputStream outputStream, List<LogStatsDTO> logStats) throws ServiceException, ResourceNotFoundException, ParseErrorException, Exception, MethodInvocationException, IOException {
        if (outputStream instanceof ZipOutputStream) {
            ((ZipOutputStream) outputStream).putNextEntry(new ZipEntry(getFileName()));
        }
        DataProviderDTO dataProvider = null;
        if (providerKey != null) {
            dataProvider = dataResourceManager.getDataProviderFor(providerKey);
        }
        Map<Integer, LogStatsDTO> eventIdProviderStats = new HashMap<Integer, LogStatsDTO>();
        for (LogStatsDTO logStat : logStats) {
            LogStatsDTO providerLogStats = eventIdProviderStats.get(logStat.getEventId());
            if (providerLogStats == null) {
                providerLogStats = new LogStatsDTO();
                if (dataProvider == null) {
                    DataResourceDTO dataResource = dataResourceManager.getDataResourceFor(logStat.getEntityKey());
                    providerLogStats.setEntityKey(dataResource.getDataProviderKey());
                    providerLogStats.setEntityName(dataResource.getDataProviderName());
                } else {
                    providerLogStats.setEntityKey(dataProvider.getKey());
                    providerLogStats.setEntityName(dataProvider.getName());
                }
                providerLogStats.setEventId(logStat.getEventId());
                providerLogStats.setEventName(logStat.getEventName());
                providerLogStats.setEventCount(new Integer(0));
                eventIdProviderStats.put(providerLogStats.getEventId(), providerLogStats);
            }
            if (logStat.getEventCount() != null) {
                providerLogStats.setEventCount(providerLogStats.getEventCount() + logStat.getEventCount());
            }
            if (logStat.getCount() != null) {
                if (providerLogStats.getCount() == null) {
                    providerLogStats.setCount(logStat.getCount());
                } else {
                    providerLogStats.setCount(providerLogStats.getCount() + logStat.getCount());
                }
            }
        }
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("logQuery", this);
        velocityContext.put("date", new DateFormatUtils());
        if (eventIdProviderStats != null) {
            List<LogStatsDTO> providerStats = new ArrayList<LogStatsDTO>();
            for (Integer key : eventIdProviderStats.keySet()) {
                providerStats.add(eventIdProviderStats.get(key));
            }
            Collections.sort(providerStats, new Comparator<LogStatsDTO>() {

                public int compare(LogStatsDTO ls1, LogStatsDTO ls2) {
                    if (!ls1.getEntityName().equals(ls2.getEntityName())) {
                        return ls1.getEntityKey().compareTo(ls2.getEntityName());
                    } else {
                        return ls1.getEventId().compareTo(ls2.getEventId());
                    }
                }
            });
            velocityContext.put("dataProviderStats", providerStats);
        }
        if (dataProvider != null) {
            velocityContext.put("dataProvider", dataProvider);
        }
        velocityContext.put("dataResourceStats", logStats);
        Template template = Velocity.getTemplate("org/gbif/portal/io/logMessageStats.vm");
        template.initDocument();
        LogEventField lef = new LogEventField();
        lef.setFieldName("record.eventId");
        List<Field> downloadFields = new ArrayList<Field>();
        downloadFields.add(lef);
        FieldFormatter ff = new FieldFormatter(downloadFields, messageSource, null, null);
        velocityContext.put("propertyFormatter", ff);
        TemplateUtils tu = new TemplateUtils();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        tu.merge(template, velocityContext, writer);
        writer.flush();
        if (outputStream instanceof ZipOutputStream) {
            addTemplate(outputStream, velocityContext, tu, "org/gbif/portal/io/logMessageStatsHTML.vm", "log-statistics.html");
            addTemplate(outputStream, velocityContext, tu, "org/gbif/portal/io/logMessageReadme.vm", "README.txt");
        }
    }
