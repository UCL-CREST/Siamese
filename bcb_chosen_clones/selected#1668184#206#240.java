    public List<Template> getTemplates(boolean fromPrivate) {
        String shared = fromPrivate ? "private" : "public";
        List<Template> templates = new ArrayList<Template>();
        String restURL = configuration.getBeehiveRESTRootUrl() + "account/" + userService.getAccount().getOid() + "/templates/" + shared;
        HttpGet httpGet = new HttpGet(restURL);
        httpGet.setHeader("Accept", "application/json");
        this.addAuthentication(httpGet);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
                if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_UNAUTHORIZED) {
                    throw new NotAuthenticatedException("User " + userService.getCurrentUser().getUsername() + " not authenticated! ");
                }
                throw new BeehiveNotAvailableException("Beehive is not available right now! ");
            }
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            BufferedReader buffReader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = buffReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            String result = sb.toString();
            TemplateList templateList = buildTemplateListFromJson(result);
            List<TemplateDTO> dtoes = templateList.getTemplates();
            for (TemplateDTO dto : dtoes) {
                templates.add(dto.toTemplate());
            }
        } catch (IOException e) {
            throw new BeehiveNotAvailableException("Failed to get template list, The beehive is not available right now ", e);
        }
        return templates;
    }
