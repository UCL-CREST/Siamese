    @TestProperties(name = "Simple test for adding different image files to report as link")
    public void testAddLinkToImage() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("regression/generic/images/splash.jpg");
        report.report(report.getCurrentTestFolder() + System.getProperty("file.separator") + "splash.jpg");
        IOUtils.copy(in, new FileOutputStream(new File(report.getCurrentTestFolder() + System.getProperty("file.separator") + "splash.jpg")));
        in = this.getClass().getClassLoader().getResourceAsStream("regression/generic/images/blue.png");
        IOUtils.copy(in, new FileOutputStream(new File(report.getCurrentTestFolder() + System.getProperty("file.separator") + "blue.png")));
        in = this.getClass().getClassLoader().getResourceAsStream("regression/generic/images/classDir.gif");
        IOUtils.copy(in, new FileOutputStream(new File(report.getCurrentTestFolder() + System.getProperty("file.separator") + "classDir.gif")));
        report.addLink("Link to JPG", "splash.jpg");
        report.addLink("Link to PNG", "blue.png");
        report.addLink("Link to GIF", "classDir.gif");
    }
