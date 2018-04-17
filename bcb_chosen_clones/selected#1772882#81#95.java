            public void render(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("application/zip");
                ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
                for (Iterator i = storage.getBooks().iterator(); i.hasNext(); ) {
                    Book book = (Book) i.next();
                    String bookFileName = book.getName().replaceAll("[^0-9A-Za-z_.&@()' -]", "_") + ".book.xml";
                    zos.putNextEntry(new ZipEntry(bookFileName));
                    StringWriter sw = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(sw);
                    getStorage().exportBook(book, printWriter);
                    zos.write(sw.toString().getBytes());
                    zos.closeEntry();
                }
                zos.close();
            }
