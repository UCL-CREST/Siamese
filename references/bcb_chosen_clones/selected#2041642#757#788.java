	private static boolean tryDefaultLaunch(final Desktop.Action action, final URI uri) {
		try {
			if (!Desktop.isDesktopSupported())
				return false;
		}
		// bad implementation...
		catch (Exception exception) {
			MLogger.exception(exception);
			
			return false;
		}
		
		Desktop desktop = Desktop.getDesktop();
		if (desktop.isSupported(action)) {
			MLogger.debug("core", "Using default launcher...");
			try {
				switch (action) {
					case BROWSE:
						desktop.browse(uri);
						break;
					case MAIL:
						desktop.mail(uri);
						break;
				}
					
				return true;
			}
			catch (IOException exception) { } // quiet
		}
		
		return false;
	}
