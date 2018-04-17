    protected static boolean isLatestVersion(double myVersion, String referenceAddress) {
        Scanner scanner = null;
        try {
            URL url = new URL(referenceAddress);
            InputStream iS = url.openStream();
            scanner = new Scanner(iS);
            String firstLine = scanner.nextLine();
            double latestVersion = Double.valueOf(firstLine.trim());
            double thisVersion = OpenSONAR.VERSION;
            return thisVersion >= latestVersion;
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host!!!");
            return false;
        } catch (Exception e) {
            System.out.println("Can't decide latest version");
            e.printStackTrace();
            return false;
        }
    }
