                        public void run() {
                            try {
                                System.out.println("Setting page on Cobra");
                                SimpleHtmlRendererContext rendererContext = new SimpleHtmlRendererContext(htmlPanel, new SimpleUserAgentContext());
                                int nodeBaseEnd = furl.indexOf("/", 10);
                                if (nodeBaseEnd == -1) nodeBaseEnd = furl.length();
                                String nodeBase = furl.substring(0, nodeBaseEnd);
                                InputStream pageStream = new URL(furl).openStream();
                                BufferedReader pageStreamReader = new BufferedReader(new InputStreamReader(pageStream));
                                String pageContent = "";
                                String line;
                                while ((line = pageStreamReader.readLine()) != null) pageContent += line;
                                pageContent = borderImages(pageContent, nodeBase);
                                htmlPanel.setHtml(pageContent, furl, rendererContext);
                            } catch (Exception e) {
                                System.out.println("Error loading page " + furl + " : " + e);
                            }
                        }
