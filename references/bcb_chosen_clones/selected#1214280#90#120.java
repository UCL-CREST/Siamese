    @Override
    public void onLoadingEnded() {
        if (m_frame != null) {
            try {
                String urltext = getDocument().getDocumentURI();
                URL url = new URL(urltext);
                InputStreamReader isr = new InputStreamReader(url.openStream());
                BufferedReader in = new BufferedReader(isr);
                String inputLine;
                urltext = null;
                url = null;
                m_content.clear();
                while ((inputLine = in.readLine()) != null) {
                    m_content.add(inputLine);
                }
                in.close();
                isr = null;
                in = null;
                inputLine = null;
                Action action = parseHtml();
                if (action.value() == Action.ACTION_BROWSER_LOADING_DONE && action.toString().equals(Action.COMMAND_CARD_PREVIEW)) {
                    FileUtils.copyURLToFile(new URL(getCardImageURL(m_card.MID)), new File(m_card.getImagePath()));
                    fireActionEvent(MainWindow.class, action.value(), action.toString());
                }
                action = null;
            } catch (Exception ex) {
                Dialog.ErrorBox(m_frame, ex.getStackTrace());
            }
        }
        m_loading = false;
    }
