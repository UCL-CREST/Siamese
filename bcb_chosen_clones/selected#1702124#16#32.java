    public static void openLink(URL link) {
        String message = null;
        if (!Desktop.isDesktopSupported()) {
            message = "<html><div style=\"width: 400;\">" + "" + "The link you selected was \"" + link + "\", but automatically opening links is not supported " + "by java on your machine.<br><br>To view this link, " + "you'll need to open it in your browser manually.</div></html>";
        } else {
            try {
                Desktop.getDesktop().browse(link.toURI());
            } catch (URISyntaxException use) {
                message = "<html><div style=\"width: 400;\">" + "" + "The link you selected was \"" + link + "\", but it appears to be invalid.\n\n" + "Please report this error on the Sims2Tracker bug " + "forums.</div></html>";
            } catch (IOException ioe) {
                message = "<html><div style=\"width: 400;\">" + "" + "The link you selected was \"" + link + "\", but we've failed to open this link in your browser." + "<br><br>To view this link, you'll need to open it in " + "your browser manually.</div></html>";
            }
        }
        if (message != null) {
            JOptionPane.showMessageDialog(null, message, "Can't open link", JOptionPane.INFORMATION_MESSAGE);
        }
    }
