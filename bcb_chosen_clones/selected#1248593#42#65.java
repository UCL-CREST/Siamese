    public IUserProfile getUserProfile(String profileID) throws MM4UUserProfileNotFoundException {
        SimpleUserProfile tempProfile = null;
        String tempProfileString = this.profileURI + profileID + FILE_SUFFIX;
        try {
            URL url = new URL(tempProfileString);
            Debug.println("Retrieve profile with ID: " + url);
            tempProfile = new SimpleUserProfile();
            BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
            String tempLine = null;
            tempProfile.add("id", profileID);
            while ((tempLine = input.readLine()) != null) {
                Property tempProperty = PropertyList.splitStringIntoKeyAndValue(tempLine);
                if (tempProperty != null) {
                    tempProfile.addIfNotNull(tempProperty.getKey(), tempProperty.getValue());
                }
            }
            input.close();
        } catch (MalformedURLException exception) {
            throw new MM4UUserProfileNotFoundException(this, "getProfile", "Profile '" + tempProfileString + "' not found.");
        } catch (IOException exception) {
            throw new MM4UUserProfileNotFoundException(this, "getProfile", "Profile '" + tempProfileString + "' not found.");
        }
        return tempProfile;
    }
