/*
 *    Copyright 2006 Baltijos Sprendimai (http://www.bsprendimai.lt/)
 *              Authorship: Aleksandr Panzin (http://www.activelogic.eu/)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package lt.bsprendimai.ddesk.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import lt.bsprendimai.ddesk.dao.Company;
import lt.bsprendimai.ddesk.dao.Person;
import lt.bsprendimai.ddesk.dao.Ticket;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * PDF ticket version dgenerator.
 * Currently in lithuanian language only, not internationalized.
 * Uses iText for PDF generation.
 *
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class PDFTicket {

    private Image logo;

    private final static SimpleDateFormat sdf = new SimpleDateFormat(
	    "yyyy.MM.dd");
    private final static SimpleDateFormat sdtf = new SimpleDateFormat(
	    "yyyy.MM.dd HH:mm");

    private Ticket tt;
    private Person me;
    private Person clientPerson;
    private Company client;
    private BaseFont baseArial;
    private Company we;

    private File root;

    /** Creates a new instance of PDFTicket */
    public PDFTicket(Ticket tt, Person me, Person clientPerson, Company client,
	    Company we) {
	tt.postLoad();
	this.tt = tt;
	this.me = me;
	this.clientPerson = clientPerson;
	this.client = client;
	this.we = we;

    }

    public byte[] writePage() throws Exception {
	ByteArrayOutputStream bout = new ByteArrayOutputStream();

	try {
	    logo = Image
		    .getInstance(root.getAbsolutePath() + "/logo.gif");
	    logo.setAlignment(Image.ALIGN_LEFT);
	} catch (Exception exc) {
	    exc.printStackTrace();
	}

	baseArial = BaseFont.createFont(root.getAbsolutePath()
		+ "/objects/arial.ttf", BaseFont.CP1257, BaseFont.EMBEDDED);

	Document doc = new Document(PageSize.A4, 56.5354331f, 22.6771654f,
		28.3464567f, 25.0f);
	PdfWriter.getInstance(doc, bout);

	doc.open();

	logo.setAbsolutePosition(28.3464567f, doc.getPageSize().height()
		- logo.height() - 25.0f);
	doc.add(logo);
	Paragraph pg = new Paragraph(sdf.format(new Date()), new Font(
		baseArial, 10));
	pg.setAlignment(Element.ALIGN_RIGHT);
	doc.add(pg);
	doc.add(new Phrase("\n\n\n"));

	writeClientInfo(doc);
	writeTicketInfo(doc);
	writeJobInfo(doc);
	writeWorkerInfo(doc);

	doc.close();

	return bout.toByteArray();
    }

    private void writeClientInfo(Document doc) throws Exception {
	PdfPTable tb = new PdfPTable(2);
	tb.setWidthPercentage(100.0f);
	tb.setSpacingAfter(20.0f);

	PdfPCell cl = new PdfPCell(new Phrase(this.client.getName() + "\n"
		+ this.client.getAdress(), new Font(baseArial, 10)));
	tb.addCell(cl);

	cl = new PdfPCell(new Phrase(this.we.getName() + "\n"
		+ this.we.getAdress(), new Font(baseArial, 10)));
	tb.addCell(cl);

	doc.add(tb);

    }

    private void writeTicketInfo(Document doc) throws Exception {
	Paragraph pg = new Paragraph("#" + tt.getUniqueId(), new Font(
		baseArial, 12, Font.BOLD));
	doc.add(pg);

	PdfPTable tb = new PdfPTable(1);
	tb.setWidthPercentage(100.0f);
	tb.setSpacingBefore(5.0f);
	tb.setSpacingAfter(5.0f);

	PdfPCell cl = new PdfPCell(new Phrase(tt.getName(), new Font(baseArial,
		10, Font.BOLD)));

	tb.addCell(cl);

	if (clientPerson != null) {
	    cl = new PdfPCell(new Phrase("Pranešė: " + clientPerson.getName()
		    + " (Telefono Nr: " + clientPerson.getPhoneNo() + ")",
		    new Font(baseArial, 10)));
	    tb.addCell(cl);
	}

	cl = new PdfPCell(new Phrase("Užduoties informacija: ", new Font(
		baseArial, 10)));
	tb.addCell(cl);

	cl = new PdfPCell(new Phrase(tt.getDescription(), new Font(baseArial,
		10)));

	tb.addCell(cl);

	doc.add(tb);
    }

    private void writeWorkerInfo(Document doc) throws Exception {

	PdfPTable tb = new PdfPTable(2);
	tb.setWidthPercentage(100.0f);
	tb.setSpacingBefore(8.0f);
	tb.setSpacingAfter(8.0f);

	PdfPCell cl;

	cl = new PdfPCell(new Phrase("Atsakingas asmuo: " + me.getName(),
		new Font(baseArial, 10)));
	cl.setColspan(2);
	tb.addCell(cl);

	cl = new PdfPCell(new Phrase("Įvykdymo data: "
		+ timeFormat(tt.getDateClosed()), new Font(baseArial, 10)));
	tb.addCell(cl);

	if (tt.getWorktime() != null) {
	    cl = new PdfPCell(new Phrase("Darbo valandos:" + tt.getWorktime(),
		    new Font(baseArial, 10)));
	    tb.addCell(cl);
	} else {
	    cl = new PdfPCell(new Phrase("Darbo valandos: ", new Font(
		    baseArial, 10)));
	    tb.addCell(cl);
	}

	doc.add(tb);

	tb = new PdfPTable(2);

	tb.setWidthPercentage(100.0f);
	tb.setSpacingBefore(50.0f);

	cl = new PdfPCell(new Phrase("Kliento parašas\n\n\n\n", new Font(
		baseArial, 10)));
	cl.setBorder(0);
	tb.addCell(cl);

	cl = new PdfPCell(new Phrase("Užduotį atlikęs darbuotojas\n\n\n\n",
		new Font(baseArial, 10)));
	cl.setBorder(0);
	tb.addCell(cl);
	doc.add(tb);

    }

    private void writeJobInfo(Document doc) throws Exception {

	Paragraph pg = new Paragraph("\nUŽDUOTIES ATLIKIMO INFORMACIJA",
		new Font(baseArial, 10, Font.BOLD));
	doc.add(pg);

	PdfPTable tb = new PdfPTable(3);
	tb.setWidthPercentage(100.0f);
	tb.setSpacingBefore(5.0f);
	tb.setSpacingAfter(12.0f);
	tb.setWidths(new float[] { 6.0f, 84.0f, 10.0f });

	PdfPCell cl;

	doc.add(new Phrase("\nAtlikti darbai", new Font(baseArial, 12,
		Font.BOLD)));

	cl = new PdfPCell(new Phrase("Eil.nr", new Font(baseArial, 10,
		Font.BOLD)));
	tb.addCell(cl);
	cl = new PdfPCell(new Phrase("Užduoties pavadinimas", new Font(
		baseArial, 10, Font.BOLD)));
	tb.addCell(cl);
	cl = new PdfPCell(new Phrase("Valandos", new Font(baseArial, 10,
		Font.BOLD)));
	tb.addCell(cl);

	doc.add(tb);

    }

    private String timeFormat(Date dt) {
	if (dt == null)
	    return "";
	return sdtf.format(dt);
    }

    public File getRoot() {
	return root;
    }

    public void setRoot(File root) {
	this.root = root;
    }

}
