    public void run() {
        counter = 0;
        Log.debug("Fetching news");
        Session session = botService.getSession();
        if (session == null) {
            Log.warn("No current IRC session");
            return;
        }
        final List<Channel> channels = session.getChannels();
        if (channels.isEmpty()) {
            Log.warn("No channel for the current IRC session");
            return;
        }
        if (StringUtils.isEmpty(feedURL)) {
            Log.warn("No feed provided");
            return;
        }
        Log.debug("Creating feedListener");
        FeedParserListener feedParserListener = new DefaultFeedParserListener() {

            public void onChannel(FeedParserState state, String title, String link, String description) throws FeedParserException {
                Log.debug("onChannel:" + title + "," + link + "," + description);
            }

            public void onItem(FeedParserState state, String title, String link, String description, String permalink) throws FeedParserException {
                if (counter >= MAX_FEEDS) {
                    throw new FeedPollerCancelException("Maximum number of items reached");
                }
                boolean canAnnounce = false;
                try {
                    if (lastDigest == null) {
                        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
                        md.update(title.getBytes());
                        lastDigest = md.digest();
                        canAnnounce = true;
                    } else {
                        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
                        md.update(title.getBytes());
                        byte[] currentDigest = md.digest();
                        if (!MessageDigest.isEqual(currentDigest, lastDigest)) {
                            lastDigest = currentDigest;
                            canAnnounce = true;
                        }
                    }
                    if (canAnnounce) {
                        String shortTitle = title;
                        if (shortTitle.length() > TITLE_MAX_LEN) {
                            shortTitle = shortTitle.substring(0, TITLE_MAX_LEN) + " ...";
                        }
                        String shortLink = IOUtil.getTinyUrl(link);
                        Log.debug("Link:" + shortLink);
                        for (Channel channel : channels) {
                            channel.say(String.format("%s, %s", shortTitle, shortLink));
                        }
                    }
                } catch (Exception e) {
                    throw new FeedParserException(e);
                }
                counter++;
            }

            public void onCreated(FeedParserState state, Date date) throws FeedParserException {
            }
        };
        if (parser != null) {
            InputStream is = null;
            try {
                Log.debug("Reading feedURL");
                is = new URL(feedURL).openStream();
                parser.parse(feedParserListener, is, feedURL);
                Log.debug("Parsing done");
            } catch (IOException ioe) {
                Log.error(ioe.getMessage(), ioe);
            } catch (FeedPollerCancelException fpce) {
            } catch (FeedParserException e) {
                for (Channel channel : channels) {
                    channel.say(e.getMessage());
                }
            } finally {
                IOUtil.closeQuietly(is);
            }
        } else {
            Log.warn("Wasn't able to create feed parser");
        }
    }
