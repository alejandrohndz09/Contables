/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EstadoBalance;

import Procesos.EstadodeResultado;
import Procesos.Mayor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entidades.Inventario;
import entidades.Periodo;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logica.logicaContables;

/**
 *
 * @author Xom
 */
public class BalanzaComprobacion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/pdf");
        OutputStream out = response.getOutputStream();
        try {
            try {

                //variables para los calculos
                logicaContables lc = new logicaContables();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
                simbolo.setDecimalSeparator('.');
                simbolo.setGroupingSeparator(',');
                DecimalFormat f = new DecimalFormat("###,##0.00", simbolo);
                String parametro = request.getParameter("tipobalanza");

                //inicio de la creacion del documento
                Document documento = new Document();
                PdfWriter writer = PdfWriter.getInstance(documento, out);
                HeaderFooterPageEvent event = new HeaderFooterPageEvent();
                writer.setPageEvent(event);
                documento.open();
                event.setHeader(String.valueOf(sdf.format(new Date())));
                //imagen del documento  C:\xampp\htdocs\Contables
                String rutaImagen = "C:\\xampp\\htdocs\\Contables\\web\\img\\logo.png";
                Image imagen = Image.getInstance(rutaImagen);
                imagen.scaleAbsolute(90, 100);//tamaño imagen
                imagen.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
                //imagen.setAlignment(Element.ALIGN_TOP);//alineacion de la imagen 
                imagen.setBorder(0);
                imagen.setSpacingAfter(0);//antes de la imagen
                imagen.setSpacingBefore(10);//despues de la imagen
                documento.add(imagen);

                Paragraph par1;//para el titulo
                //definiendo el titulo del documento
                Font fonttitulo;
                fonttitulo = new Font(Font.FontFamily.UNDEFINED, 16, Font.BOLD, BaseColor.BLACK);
                par1 = new Paragraph(new Phrase("TECH S.A DE C.V", fonttitulo));
                par1.setAlignment(Element.ALIGN_CENTER);
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                documento.add(par1);
                fonttitulo = new Font(Font.FontFamily.UNDEFINED, 11, Font.BOLD, BaseColor.BLACK);
                par1 = new Paragraph(new Phrase("BALANZA DE COMPROBACIÓN DE SUMAS Y SALDOS", fonttitulo));
                par1.setAlignment(Element.ALIGN_CENTER);
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                documento.add(par1);
                fonttitulo = new Font(Font.FontFamily.UNDEFINED, 11, Font.BOLD, BaseColor.BLACK);
                par1 = new Paragraph(new Phrase("EXPRESADO EN DOLARES DE LOS ESTADOS UNIDOS DE AMERICA", fonttitulo));
                par1.setAlignment(Element.ALIGN_CENTER);
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                documento.add(par1);
                Calendar fecha = Calendar.getInstance();//obtener fecha actual
                fonttitulo = new Font(Font.FontFamily.UNDEFINED, 11, Font.BOLD, BaseColor.BLACK);
                par1 = new Paragraph(new Phrase("DEL 1 DE ENERO AL 31 DE DICIEMBRE DE " + fecha.get(Calendar.YEAR), fonttitulo));
                par1.setAlignment(Element.ALIGN_CENTER);
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                documento.add(par1);

                //inicio de la creación de la tablas
                PdfPTable tabla = new PdfPTable(7);
                PdfPCell cell;
                tabla.setWidths((new float[]{Float.parseFloat(String.valueOf(0.40)), Float.parseFloat(String.valueOf(0.80)), Float.parseFloat(String.valueOf(2.50)), 1, 1, 1, 1
                }));

                //encabezado
                cell = new PdfPCell(new Paragraph("N°", FontFactory.getFont("undefined", 9, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setRowspan(2);
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph("CÓDIGO", FontFactory.getFont("undefined", 9, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setRowspan(2);
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph("CONCEPTO", FontFactory.getFont("undefined", 9, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setRowspan(2);
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph("MOVIMIENTOS", FontFactory.getFont("undefined", 9, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph("SALDOS", FontFactory.getFont("undefined", 9, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph("Debe", FontFactory.getFont("undefined", 9, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph("Haber", FontFactory.getFont("undefined", 9, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph("Deudor", FontFactory.getFont("undefined", 9, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph("Acreedor", FontFactory.getFont("undefined", 9, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cell);

                if (parametro.equals("antescierre")) {
                    int i = 1;
                    BigDecimal tdebe = new BigDecimal("0.00");
                    BigDecimal thaber = new BigDecimal("0.00");
                    BigDecimal tdeudor = new BigDecimal("0.00");
                    BigDecimal tacreeedor = new BigDecimal("0.00");
                    ArrayList<Mayor> balanza = (ArrayList<Mayor>) lc.listadoMayor(4);
                    for (Mayor b : balanza) {
                        b.issdsa();
                        if (b.getSaldoAcreedor().floatValue() > 0 || b.getSaldoDeudor().floatValue() > 0) {
                            if (b.getDh().size() > 0) {
                                //contador
                                cell = new PdfPCell(new Paragraph(String.valueOf(i), FontFactory.getFont("undefined", 8, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                tabla.addCell(cell);
                                //codigo
                                cell = new PdfPCell(new Paragraph(b.getCuentamayor().getCodigocuenta(), FontFactory.getFont("undefined", 8, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                tabla.addCell(cell);
                                //concepto
                                cell = new PdfPCell(new Paragraph(b.getCuentamayor().getNombrecuenta(), FontFactory.getFont("undefined", 8, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                tabla.addCell(cell);
                                //debe
                                cell = new PdfPCell(new Paragraph(((b.getDebe().floatValue() > 0) ? f.format(b.getDebe().floatValue()) : ""), FontFactory.getFont("undefined",7, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                //haber
                                cell = new PdfPCell(new Paragraph(((b.getHaber().floatValue() > 0) ? f.format(b.getHaber().floatValue()) : ""), FontFactory.getFont("undefined",7, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                //deudor
                                cell = new PdfPCell(new Paragraph(((b.getSaldoDeudor().floatValue() > 0) ? f.format(b.getSaldoDeudor().floatValue()) : ""), FontFactory.getFont("undefined",7, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                //acreedor
                                cell = new PdfPCell(new Paragraph(((b.getSaldoAcreedor().floatValue() > 0) ? f.format(b.getSaldoAcreedor().floatValue()) : ""), FontFactory.getFont("undefined",7, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);

                                //acumulando los saldos
                                tdebe = tdebe.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getDebe()))).setScale(2, RoundingMode.HALF_UP));
                                tdeudor = tdeudor.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP));
                                thaber = thaber.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getHaber()))).setScale(2, RoundingMode.HALF_UP));
                                tacreeedor = tacreeedor.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP));
                                i++;
                            }
                        }
                    }
                    cell = new PdfPCell(new Paragraph("     TOTALES", FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(3);
                    tabla.addCell(cell);
                    //agregando las sumatorias
                    if ((tdebe.equals(thaber) && tdeudor.equals(tacreeedor)) && (tdebe.floatValue() > 0 && thaber.floatValue() > 0 && tdeudor.floatValue() > 0 && tacreeedor.floatValue() > 0)) {
                        cell = new PdfPCell(new Paragraph(f.format(tdebe.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(thaber.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(tdeudor.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(tacreeedor.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Paragraph(f.format(tdebe.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.WHITE)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.RED);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(thaber.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.WHITE)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.RED);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(tdeudor.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.WHITE)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.RED);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(tacreeedor.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.WHITE)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.RED);
                        tabla.addCell(cell);
                    }

                } else if (parametro.equals("despuescierre")) {

                    int iff = 1;
                    BigDecimal tdebef = new BigDecimal("0.00");
                    BigDecimal thaberf = new BigDecimal("0.00");
                    BigDecimal tdeudorf = new BigDecimal("0.00");
                    BigDecimal tacreeedorf = new BigDecimal("0.00");
                    ArrayList<Mayor> balanzaf = (ArrayList<Mayor>) lc.listadoMayor(4);
                    for (Mayor b : balanzaf) {
                        b.liquidacion();
                        if (b.getSaldoAcreedor().floatValue() >= 0 || b.getSaldoDeudor().floatValue() >= 0) {
                            if (b.getDh().size() > 0) {
                                //contador
                                cell = new PdfPCell(new Paragraph(String.valueOf(iff), FontFactory.getFont("undefined", 8, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                tabla.addCell(cell);
                                //codigo
                                cell = new PdfPCell(new Paragraph(b.getCuentamayor().getCodigocuenta(), FontFactory.getFont("undefined", 8, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                tabla.addCell(cell);
                                //concepto
                                cell = new PdfPCell(new Paragraph(b.getCuentamayor().getNombrecuenta(), FontFactory.getFont("undefined", 8, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                tabla.addCell(cell);
                                //debe
                                cell = new PdfPCell(new Paragraph(((b.getDebe().floatValue() > 0) ? f.format(b.getDebe().floatValue()) : ""), FontFactory.getFont("undefined",7, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                //haber
                                cell = new PdfPCell(new Paragraph(((b.getHaber().floatValue() > 0) ? f.format(b.getHaber().floatValue()) : ""), FontFactory.getFont("undefined",7, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                //deudor
                                cell = new PdfPCell(new Paragraph(((b.getSaldoDeudor().floatValue() > 0) ? f.format(b.getSaldoDeudor().floatValue()) : ""), FontFactory.getFont("undefined",7, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                //acreedor
                                cell = new PdfPCell(new Paragraph(((b.getSaldoAcreedor().floatValue() > 0) ? f.format(b.getSaldoAcreedor().floatValue()) : ""), FontFactory.getFont("undefined",7, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);

                                //acumulando los saldos
                                tdebef = tdebef.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getDebe()))).setScale(2, RoundingMode.HALF_UP));
                                tdeudorf = tdeudorf.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP));
                                thaberf = thaberf.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getHaber()))).setScale(2, RoundingMode.HALF_UP));
                                tacreeedorf = tacreeedorf.add(new BigDecimal(Float.parseFloat(String.valueOf(b.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP));
                                iff++;
                            }
                        }
                    }
                    cell = new PdfPCell(new Paragraph("     TOTALES", FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(3);
                    tabla.addCell(cell);
                    //agregando las sumatorias
                    if ((tdebef.equals(thaberf) && tdeudorf.equals(tacreeedorf)) && (tdebef.floatValue() > 0 && thaberf.floatValue() > 0 && tdeudorf.floatValue() > 0 && tacreeedorf.floatValue() > 0)) {
                        cell = new PdfPCell(new Paragraph(f.format(tdebef.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(thaberf.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(tdeudorf.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(tacreeedorf.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Paragraph(f.format(tdebef.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.WHITE)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.RED);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(thaberf.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.WHITE)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.RED);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(tdeudorf.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.WHITE)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.RED);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(tacreeedorf.floatValue()), FontFactory.getFont("undefined", 8, Font.BOLD, BaseColor.WHITE)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.RED);
                        tabla.addCell(cell);
                    }
                }

                documento.add(tabla);
                documento.close();
            } catch (Exception e) {
                e.getMessage();
            }
        } finally {
            out.close();
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
