    public void hyperlinkUpdate(HyperlinkEvent e) {
        Point l = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(l, invoker);
        final int mouseY = l.y;

        final Element element = e.getSourceElement();
        {
            BalloonTip balloonTip = openBalloons.get(element);
            if (balloonTip != null) {
                balloonTip.refreshLocation();
                return;
            }
        }

        URL url = e.getURL();
        String stringUrl = e.getDescription();
        String text = LinkUtils.getUrlText(element);

        if (url == null) {
            // Invalid url. Try to parse it from text.

            try {
                url = new URL(text);
                // Url in the 'text' field, so assume that text in the 'description' field
                text = stringUrl;
                stringUrl = url.toExternalForm();
            } catch (MalformedURLException ex) {
                // url can not be obtained neither from text nor from description.
            }
        }

        Integer messageId = LinkUtils.getMessageIdFromUrl(stringUrl);
        if (messageId == null) {
            messageId = LinkUtils.getMessageIdFromUrl(text);
        }

        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            final Timer aimedTimer = aimedTimers.remove(element);
            if (aimedTimer != null) {
                aimedTimer.stop();
            }

            if (url == null) {
                // TODO: show error or standard dialog
            } else if (messageId == null) {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();

                    try {
                        desktop.browse(url.toURI());
                    } catch (IOException e1) {
                        log.error("Can not open url " + url.toExternalForm() + " in default browser");
                    } catch (URISyntaxException e1) {
                        log.error("Can not obtain URI of URL: " + url.toExternalForm());
                    }
                } else {
                    ClipboardUtils.copyToClipboard(url.toExternalForm());
                    Rectangle r = getElementRectangle(element, mouseY);

                    Color color = BalloonTipUtils.TIP_BACKGROUND;
                    BalloonTipStyle tipStyle = BalloonTipUtils.createTipStyle(color);

                    JButton closeButton = BalloonTipUtils.balloonTipCloseButton();
                    final JLabel label = new JLabel(Message.PreviewLink_LinkCopied.get());
                    final BalloonTip balloonTip = new CustomBalloonTip(invoker, label, r, tipStyle, new LeftAbovePositioner(15, 15), closeButton);
                    openBalloons.put(element, balloonTip);

                    balloonTip.addHierarchyListener(new HierarchyListener() {
                        @Override
                        public void hierarchyChanged(HierarchyEvent e) {
                            if (HierarchyEvent.SHOWING_CHANGED == (HierarchyEvent.SHOWING_CHANGED & e.getChangeFlags())) {
                                if (balloonTip.isShowing()) {
                                    openBalloons.put(element, balloonTip);
                                } else {
                                    openBalloons.remove(element);
                                }
                            }
                        }
                    });
                    Timer timer = new Timer((int) TimeUnit.SECONDS.toMillis(3), new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            balloonTip.closeBalloon();
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            } else {
                appControl.openMessage(messageId, Property.OPEN_MESSAGE_BEHAVIOUR_GENERAL.get());
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.EXITED) {
            final Timer timer = aimedTimers.remove(element);
            if (timer != null) {
                timer.stop();
            }
        } else if (e.getEventType() == HyperlinkEvent.EventType.ENTERED) {
            final Runnable showBalloonAction = new ShowBalloonAction(url, messageId, element, mouseY);

            int delay = Property.LINK_PREVIEW_DELAY.get();
            if (delay > 0) {
                // Set up timer
                final Timer timer = new Timer(delay, null);
                timer.setRepeats(false);
                timer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        aimedTimers.remove(element);
                        showBalloonAction.run();
                    }
                });
                aimedTimers.put(element, timer);
                timer.start();
            } else {
                showBalloonAction.run();
            }
        }
    }
