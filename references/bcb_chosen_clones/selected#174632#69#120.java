		@Override
		public void onAction() {
			Config config = Plugin.this.getLocalConfig();
			ScreenshotSettingsForm form = new ScreenshotSettingsForm();
			form.delay = config.readInt("delay", ScreenshotSettingsForm.DEFAULT_DELAY, 0, ScreenshotSettingsForm.MAX_DELAY);
			form.hideMainWindow = config.read("hideMainWindow", form.hideMainWindow);
			FormPanel<ScreenshotSettingsForm> panel = new FormPanel<>(form);
			panel.setLabel("delay", _("Delay (in seconds):"));
			panel.setLabel("hideMainWindow", _("Hide Main Window"));
			MDialog dialog = panel.createDialog(this.getSourceWindow(), _("New Screenshot"), "ui/image");
			dialog.packFixed();
			
			if (!dialog.exec())
				return;

			config.write("delay", form.delay);
			config.write("hideMainWindow", form.hideMainWindow);
			config.sync();

			MainWindow mainWindow = MainWindow.getInstance();
			try {
				if (form.hideMainWindow)
					mainWindow.setVisible(false);

				if (form.delay > 0) {
// FIXME: blocks main window
					TK.sleep(form.delay * 1000);
				}

				Robot robot = new Robot();
				Rectangle area = new Rectangle();
				area.setSize(UI.getScreenSize());
				BufferedImage image = robot.createScreenCapture(area);
				
				if (form.hideMainWindow)
					mainWindow.setVisible(true);

				TreeFS treeFS = TreeFS.getInstance();
				MetaInfo folder = treeFS.getCurrentFolder(false);
				MetaInfo file = treeFS.createUniqueFile(folder, _("Screenshot"), "png");
				ImageIO.write(image, "png", file.getFile());
// TODO: add image format help/tips
				Tree.getInstance().open(file);
			}
			catch (Exception exception) {
				MMessage.error(this.getSourceWindow(), exception);
			}
			finally {
				if (form.hideMainWindow && !mainWindow.isVisible())
					mainWindow.setVisible(true);
			}
		}
