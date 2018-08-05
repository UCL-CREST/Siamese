    @Test(dataProvider = Arquillian.ARQUILLIAN_DATA_PROVIDER)
    public void shouldGreetUser(@ArquillianResource URL baseURL) throws IOException {
        final String name = "Earthlings";
        final URL url = new URL(baseURL, "Foo.action");
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        LOGGER.info("Returned response: " + builder.toString());
        Assert.assertEquals(builder.toString(), FooService.GREETING + name);
    }
