    public void makePdf(HttpServletRequest request, HttpServletResponse response, String methodGetPost) {
        RunningMasterBean runningMasterBean = (RunningMasterBean) request.getSession().getAttribute("runningMasterBean");
        runningMasterBean.setRequest(request);
        RunningSystemBean runningSystemBean = (RunningSystemBean) request.getSession().getAttribute("runningSystemBean");
        runningSystemBean.insertPageTrace(Constants.CREATEVORANMELDUNGENPDF);
        String datumVon = request.getParameter("datumVon");
        Date dateFrom = RunningSystemBean.getDateFromGerman(datumVon);
        String datumBis = request.getParameter("datumBis");
        Date dateTo = RunningSystemBean.getDateFromGerman(datumBis);
        Event event;
        List<EventRoute> eventRoutes;
        String eventRouteId = request.getParameter("eventRouteId");
        String eventId = request.getParameter("eventId");
        if (RunningSystemBean.getStringValue(eventId).length() > 0) {
            event = EventManager.getInstance().getEventById(Integer.parseInt(eventId));
            Set<EventRoute> evRoutes = event.getEventRoutes();
            eventRoutes = (List) RunningSystemBean.getCollection(evRoutes);
            Collections.sort(eventRoutes, new EventRouteComparator());
        } else {
            EventRoute eventRoute = EventManager.getInstance().getEventRouteById(Integer.parseInt(eventRouteId));
            eventRoutes = Collections.singletonList(eventRoute);
            event = eventRoute.getEvent();
        }
        boolean showUrkundenRow = event.getCertificatesOrderable();
        boolean showTshirtRow = event.getTshirtsOrderable();
        int i = 0;
        try {
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);
            document.addAuthor("Martin Lang; www.myRunning.de");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            PdfWriter docWriter = null;
            try {
                docWriter = PdfWriter.getInstance(document, baos);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            document.open();
            for (EventRoute eventRoute : eventRoutes) {
                Collection<Starter> starters = RunnerManager.getInstance().getStarters(null, eventRoute, dateFrom, dateTo, null, null);
                StarterComparator starterComparator = new StarterComparator();
                starterComparator.setCompareProperty(StarterComparator.STARTNO);
                Collections.sort((ArrayList) starters, starterComparator);
                if (starters.size() > 0) {
                    for (Starter starter : starters) {
                        Runner runner = starter.getRunner();
                        if (i > 0) {
                            document.add(new Paragraph("--                                                                         -- " + "                                                                           --"));
                        } else {
                            document.add(new Paragraph(" "));
                        }
                        i++;
                        document.add(Chunk.NEWLINE);
                        PdfPTable table = new PdfPTable(2);
                        table.addCell("Name");
                        table.addCell(runner.getCompleteName());
                        table.addCell(runningSystemBean.getLocalizedText("route"));
                        table.addCell(starter.getEventRoute().getName());
                        if (starter.getStartNo() > 0) {
                            table.addCell("Startnr.");
                            table.addCell("" + starter.getStartNo());
                        }
                        String value;
                        if (starter.getTeamX() != null) {
                            value = starter.getTeamX().getName();
                            if (starter.getTeamX().getTeamType() != null) {
                                value += " (" + starter.getTeamX().getTeamType().getName() + ")";
                            }
                        } else {
                            value = starter.getTeam();
                        }
                        table.addCell("Team");
                        table.addCell(value);
                        table.addCell("Altersklasse (Jahrgang)");
                        table.addCell(starter.getAgeGroup().getName() + " (" + runner.getYearOfBirth() + ")");
                        PdfPCell cell = new PdfPCell();
                        Paragraph p = new Paragraph();
                        String adresse = "-";
                        String strasse = runner.getStreet();
                        if (strasse != null && strasse.length() > 0) {
                            adresse = strasse;
                        }
                        String hausnr = runner.getHouseNo();
                        if (hausnr != null && hausnr.length() > 0) {
                            adresse += " " + hausnr;
                        }
                        p.add(adresse);
                        p.add(Chunk.NEWLINE);
                        adresse = "-";
                        String plz = runner.getZip();
                        if (plz != null && plz.length() > 0 && !"0".equals(plz)) {
                            adresse = plz;
                        }
                        String ort = runner.getTown();
                        if (ort != null && ort.length() > 0) {
                            adresse += " " + ort;
                        }
                        p.add(adresse);
                        table.addCell("Adresse");
                        cell.addElement(p);
                        table.addCell(cell);
                        if (showUrkundenRow) {
                            table.addCell("Urkunde");
                            if (starter.getCertificate() != null) {
                                table.addCell(starter.getCertificate().getName());
                            } else {
                                table.addCell("-");
                            }
                        }
                        if (showTshirtRow) {
                            table.addCell("T-Shirt");
                            if (starter.getTshirt() != null) {
                                table.addCell(starter.getTshirt().getName() + " " + starter.getTshirt().getTshirtSize() == null ? "" : starter.getTshirt().getTshirtSize().getName());
                            } else {
                                table.addCell("-");
                            }
                        }
                        if (starter.getRemark() != null && starter.getRemark().length() > 0) {
                            table.addCell("Bemerkung");
                            table.addCell(starter.getRemark());
                        }
                        String gebuehr = starter.getRegistrationFee() + " " + event.getCurrency().getIso();
                        if (starter.getPayed()) {
                            gebuehr += " (bezahlt)";
                        }
                        table.addCell("Geb�hr");
                        table.addCell(gebuehr);
                        Collection<StarterProp> starterProps = RunningSystemBean.getCollection(starter.getStarterProps());
                        if (starterProps.size() > 0) {
                            table.addCell("Weitere Daten");
                            p = new Paragraph();
                            for (StarterProp starterProp : starterProps) {
                                p.add(starterProp.getName() + ": " + starterProp.getValue());
                                p.add(Chunk.NEWLINE);
                            }
                            table.addCell(p);
                        }
                        cell = new PdfPCell();
                        p = new Paragraph();
                        String datum = RunningSystemBean.formatToDateGerman(starter.getRegistrationDate());
                        p.add(datum);
                        p.add(Chunk.NEWLINE);
                        if (starter.getLastModificationDate() != null) {
                            datum = RunningSystemBean.formatToDateGerman(starter.getLastModificationDate());
                            p.add(datum);
                        } else {
                            p.add("-");
                        }
                        cell.addElement(p);
                        table.addCell("Anmelde- (�nderungs-) Datum");
                        table.addCell(cell);
                        document.add(table);
                        if (i == 3) {
                            i = 0;
                            document.newPage();
                        }
                    }
                }
            }
            document.close();
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            ServletOutputStream out = response.getOutputStream();
            baos.writeTo(out);
            out.flush();
        } catch (DocumentException e) {
            LOG.error(e);
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error(e);
            e.printStackTrace();
        }
    }
