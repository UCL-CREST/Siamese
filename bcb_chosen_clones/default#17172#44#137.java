    public static WeatherDatum getWeatherDotComInfo(SolarNodeSettings theSettings, String LocationIdentifier, WeatherDatum theWeather) {
        String inputLine = "";
        String[] finalString = new String[2];
        finalString[0] = "ok";
        finalString[1] = "";
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("http://xoap.weather.com/weather/local/" + LocationIdentifier + "?cc=*&dayf=5&link=xoap&prod=xoap&par=" + theSettings.weatherDotComPartnerId + "&key=" + theSettings.weatherDotComLicenseKey);
            if (theSettings.debug) {
            }
            doc.getDocumentElement().normalize();
            NodeList locationList = doc.getElementsByTagName("loc");
            Node locationNode = locationList.item(0);
            if (locationNode.getNodeType() == Node.ELEMENT_NODE) {
                Element locationElement = (Element) locationNode;
                NodeList sunriseList = locationElement.getElementsByTagName("sunr");
                Element sunriseElement = (Element) sunriseList.item(0);
                NodeList sunriseTextList = sunriseElement.getChildNodes();
                theWeather.sunrise = ((Node) sunriseTextList.item(0)).getNodeValue().toLowerCase().trim();
                NodeList sunsetList = locationElement.getElementsByTagName("suns");
                Element sunsetElement = (Element) sunsetList.item(0);
                NodeList sunsetTextList = sunsetElement.getChildNodes();
                theWeather.sunset = ((Node) sunsetTextList.item(0)).getNodeValue().toLowerCase().trim();
                NodeList latitudeList = locationElement.getElementsByTagName("lat");
                Element latitudeElement = (Element) latitudeList.item(0);
                NodeList latitudeTextList = latitudeElement.getChildNodes();
                double latitudeValue = Double.parseDouble(((Node) latitudeTextList.item(0)).getNodeValue().toLowerCase().trim());
                NodeList longitudeList = locationElement.getElementsByTagName("lat");
                Element longitudeElement = (Element) longitudeList.item(0);
                NodeList longitudeTextList = longitudeElement.getChildNodes();
                double longitudeValue = Double.parseDouble(((Node) longitudeTextList.item(0)).getNodeValue().toLowerCase().trim());
            }
            if (theSettings.debug) {
            }
            NodeList currentWeatherList = doc.getElementsByTagName("cc");
            if (theSettings.debug) {
            }
            Node currentWeatherNode = currentWeatherList.item(0);
            if (theSettings.debug) {
            }
            if (currentWeatherNode.getNodeType() == Node.ELEMENT_NODE) {
                Element settingsElement = (Element) currentWeatherNode;
                NodeList skyConditionsList = settingsElement.getElementsByTagName("t");
                Element skyConditionsElement = (Element) skyConditionsList.item(0);
                NodeList skyConditionsTextList = skyConditionsElement.getChildNodes();
                theWeather.skyConditions = ((Node) skyConditionsTextList.item(0)).getNodeValue().toLowerCase().trim();
                NodeList temperatureCelciusList = settingsElement.getElementsByTagName("tmp");
                Element temperatureCelciusElement = (Element) temperatureCelciusList.item(0);
                NodeList temperatureCelciusTextList = temperatureCelciusElement.getChildNodes();
                double temperatureFarhenheit = Double.parseDouble(((Node) temperatureCelciusTextList.item(0)).getNodeValue().toLowerCase().trim());
                theWeather.temperatureCelcius = ((5.0 / 9.0) * (temperatureFarhenheit - 32));
                NodeList humidityList = settingsElement.getElementsByTagName("hmid");
                Element humidityElement = (Element) humidityList.item(0);
                NodeList humidityTextList = humidityElement.getChildNodes();
                theWeather.humidity = Double.parseDouble(((Node) humidityTextList.item(0)).getNodeValue().toLowerCase().trim());
                NodeList visibilityList = settingsElement.getElementsByTagName("tmp");
                Element visibilityElement = (Element) visibilityList.item(0);
                NodeList visibilityTextList = visibilityElement.getChildNodes();
                theWeather.visibility = Double.parseDouble(((Node) visibilityTextList.item(0)).getNodeValue().toLowerCase().trim());
                NodeList dewpList = settingsElement.getElementsByTagName("tmp");
                Element dewpElement = (Element) dewpList.item(0);
                NodeList dewpTextList = dewpElement.getChildNodes();
                theWeather.dewPoint = Double.parseDouble(((Node) dewpTextList.item(0)).getNodeValue().toLowerCase().trim());
                NodeList barList = settingsElement.getElementsByTagName("bar");
                Element barElement = (Element) barList.item(0);
                NodeList barometricPressureList = barElement.getElementsByTagName("r");
                Element barometricPressureElement = (Element) barometricPressureList.item(0);
                NodeList barometricPressureTextList = barometricPressureElement.getChildNodes();
                theWeather.barometricPressure = Double.parseDouble(((Node) barometricPressureTextList.item(0)).getNodeValue().toLowerCase().trim());
                NodeList barometerDeltaList = barElement.getElementsByTagName("d");
                Element barometerDeltaElement = (Element) barometerDeltaList.item(0);
                NodeList barometerDeltaTextList = barometerDeltaElement.getChildNodes();
                theWeather.barometerDelta = ((Node) barometerDeltaTextList.item(0)).getNodeValue().toLowerCase().trim();
                NodeList uvList = settingsElement.getElementsByTagName("uv");
                Element uvElement = (Element) uvList.item(0);
                NodeList uvIndexList = uvElement.getElementsByTagName("i");
                Element uvIndexElement = (Element) uvIndexList.item(0);
                NodeList uvIndexTextList = uvIndexElement.getChildNodes();
                theWeather.uvIndex = Integer.parseInt(((Node) uvIndexTextList.item(0)).getNodeValue().toLowerCase().trim());
                if (theSettings.debug) {
                }
            }
        } catch (SAXParseException err) {
            System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
            System.out.println(" " + err.getMessage());
        } catch (SAXException e) {
            Exception x = e.getException();
            ((x == null) ? e : x).printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return theWeather;
    }
