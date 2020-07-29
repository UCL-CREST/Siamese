    public static void main(String[] args) throws Exception {
        final URL url = new URL("http://www.ebi.ac.uk/Tools/webservices/psicquic/registry/registry?action=ACTIVE&format=txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String str;
        Map<String, String> psiqcuicServices = new HashMap<String, String>();
        while ((str = in.readLine()) != null) {
            final int idx = str.indexOf('=');
            psiqcuicServices.put(str.substring(0, idx), str.substring(idx + 1, str.length()));
        }
        in.close();
        System.out.println("Found " + psiqcuicServices.size() + " active service(s).");
        for (Object o : psiqcuicServices.keySet()) {
            String serviceName = (String) o;
            String serviceUrl = psiqcuicServices.get(serviceName);
            System.out.println(serviceName + " -> " + serviceUrl);
            UniversalPsicquicClient client = new UniversalPsicquicClient(serviceUrl);
            try {
                SearchResult<?> result = client.getByInteractor("brca2", 0, 50);
                System.out.println("Interactions found: " + result.getTotalCount());
                for (BinaryInteraction binaryInteraction : result.getData()) {
                    String interactorIdA = binaryInteraction.getInteractorA().getIdentifiers().iterator().next().getIdentifier();
                    String interactorIdB = binaryInteraction.getInteractorB().getIdentifiers().iterator().next().getIdentifier();
                    String interactionAc = "-";
                    if (!binaryInteraction.getInteractionAcs().isEmpty()) {
                        CrossReference cr = (CrossReference) binaryInteraction.getInteractionAcs().iterator().next();
                        interactionAc = cr.getIdentifier();
                    }
                    System.out.println("\tInteraction (" + interactionAc + "): " + interactorIdA + " interacts with " + interactorIdB);
                }
            } catch (Throwable e) {
                System.err.println("Service is down! " + serviceName + "(" + serviceUrl + ")");
            }
        }
    }
