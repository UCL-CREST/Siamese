    public void handler(List<GoldenBoot> gbs, TargetPage target) {
        try {
            URL url = new URL(target.getUrl());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = null;
            String include = "Top Scorers";
            while ((line = reader.readLine()) != null) {
                if (line.indexOf(include) != -1) {
                    buildGildenBoot(line, gbs);
                    break;
                }
            }
            reader.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
    }
