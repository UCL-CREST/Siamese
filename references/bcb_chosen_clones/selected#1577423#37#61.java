    @SuppressWarnings({ "unchecked", "deprecation" })
    public static void main(String[] args) {
        Document document = new Document();
        try {
            OutputStream os = new FileOutputStream(RESULT);
            PdfWriter.getInstance(document, os);
            document.open();
            Session session = (Session) MySessionFactory.currentSession();
            Query q = session.createQuery("select distinct festival.id.day from FestivalScreening as festival order by festival.id.day");
            java.util.List<Date> days = q.list();
            Query query = session.createQuery("from FestivalScreening where id.day=? order by id.time, id.place");
            for (Date day : days) {
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(day);
                if (gc.get(GregorianCalendar.YEAR) != YEAR) continue;
                document.add(getTable(query, day));
                document.newPage();
            }
            document.close();
        } catch (IOException e) {
            LOGGER.error("IOException: ", e);
        } catch (DocumentException e) {
            LOGGER.error("DocumentException: ", e);
        }
    }
