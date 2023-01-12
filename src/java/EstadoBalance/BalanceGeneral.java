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
public class BalanceGeneral extends HttpServlet {

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
        logicaContables lc = new logicaContables();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator('.');
        simbolo.setGroupingSeparator(',');
        DecimalFormat f = new DecimalFormat("###,##0.00", simbolo);
        String tipobalance = request.getParameter("tipobalance");

        //inicio del pdf
        try {
            try {
                Document documento = new Document();
                PdfWriter writer = PdfWriter.getInstance(documento, out);
                HeaderFooterPageEvent event = new HeaderFooterPageEvent();
                writer.setPageEvent(event);
                documento.open();
                event.setHeader(String.valueOf(sdf.format(new Date())));
                //imagen del documento
                String rutaImagen = "C:\\Users\\Xom\\Documents\\NetBeansProjects\\Contables\\web\\img\\logo.png";
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
                fonttitulo = new Font(Font.FontFamily.COURIER, 16, Font.BOLD, BaseColor.BLACK);
                par1 = new Paragraph(new Phrase("TECH S.A DE C.V", fonttitulo));
                par1.setAlignment(Element.ALIGN_CENTER);
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                documento.add(par1);
                fonttitulo = new Font(Font.FontFamily.COURIER, 11, Font.BOLD, BaseColor.BLACK);
                par1 = new Paragraph(new Phrase("BALANCE GENERAL", fonttitulo));
                par1.setAlignment(Element.ALIGN_CENTER);
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                documento.add(par1);
                fonttitulo = new Font(Font.FontFamily.COURIER, 11, Font.BOLD, BaseColor.BLACK);
                par1 = new Paragraph(new Phrase("EXPRESADO EN DOLARES DE LOS ESTADOS UNIDOS DE AMERICA", fonttitulo));
                par1.setAlignment(Element.ALIGN_CENTER);
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                documento.add(par1);
                Calendar fecha = Calendar.getInstance();//obtener fecha actual
                fonttitulo = new Font(Font.FontFamily.COURIER, 11, Font.BOLD, BaseColor.BLACK);
                par1 = new Paragraph(new Phrase("DEL 1 DE ENERO AL 31 DE DICIEMBRE DE " + fecha.get(Calendar.YEAR), fonttitulo));
                par1.setAlignment(Element.ALIGN_CENTER);
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                par1.add(new Phrase(Chunk.NEWLINE));//salto de linea
                documento.add(par1);

                PdfPTable tabla = new PdfPTable(4);//tabla
                PdfPCell cell;//para generar las celdas
                tabla.setWidths(new int[]{1, 3, 1, 1});
                //encabezado
                /*cell = new PdfPCell(new Paragraph("\tConcepto", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph("Debe", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cell);
                cell = new PdfPCell(new Paragraph("Haber", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(cell);*/
                if (tipobalance.equals("antescierre")) {

                    Periodo actual = lc.PeriodoenCurso();
                    ArrayList<Periodo> lsiatperiodos = (ArrayList<Periodo>) lc.listaPeriodos();
                    Inventario i = lc.recuperaInventario("Inventario Final", actual.getIdperiodo());
                    String inf = "0.00";
                    if (i != null) {
                        inf = lc.recuperaInventario("Inventario Final", actual.getIdperiodo()).getValor();
                    } else {
                        inf = "0.00";
                    }
                    /*acumuladores*/
                    BigDecimal totActivo = new BigDecimal("0.00");
                    BigDecimal totActivoC = new BigDecimal("0.00");
                    BigDecimal totActivoNC = new BigDecimal("0.00");
                    BigDecimal totPasivoC = new BigDecimal("0.00");
                    BigDecimal totPasivoNC = new BigDecimal("0.00");
                    BigDecimal totPatrimonio = new BigDecimal("0.00");
                    BigDecimal totPT = new BigDecimal("0.00");
                    EstadodeResultado er = new EstadodeResultado(inf);
                    /*cuentas de mayorizacion*/
                    ArrayList<Mayor> activoC = (ArrayList<Mayor>) lc.listadoMayorBG(4, "11");
                    ArrayList<Mayor> activoNC = (ArrayList<Mayor>) lc.listadoMayorBG(4, "12");
                    ArrayList<Mayor> pasivoC = (ArrayList<Mayor>) lc.listadoMayorBG(4, "21");
                    ArrayList<Mayor> pasivoNC = (ArrayList<Mayor>) lc.listadoMayorBG(4, "22");
                    ArrayList<Mayor> patrimonio = (ArrayList<Mayor>) lc.listadoMayorBG(4, "31");
                    //acumulando todo el activo corriente
                    for (Mayor mac : activoC) {
                        mac.issdsa();
                        if (mac.getSaldoDeudor().floatValue() != 0 && mac.getSaldoAcreedor().floatValue() == 0 && !mac.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {
                            totActivoC = totActivoC.add(new BigDecimal(String.valueOf(mac.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP);
                        } else if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0 && !mac.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {
                            totActivoC = totActivoC.subtract(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                        } else if (mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") || mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {/*estas se restaran del total debido a que su
                                                                comportamiento es como la cuenta de un pasivo se procede a restar el saldo (naturaleza del saldo= ACREEDOR)*/
                            totActivoC = totActivoC.subtract(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                    totActivoC = totActivoC.add(er.getInventariofinal()).setScale(2, RoundingMode.HALF_UP);
                    //acumulando todo el activo no corriente
                    for (Mayor manc : activoNC) {
                        manc.issdsa();
                        if (manc.getSaldoDeudor().floatValue() != 0 && manc.getSaldoAcreedor().floatValue() == 0 && !manc.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA") && !manc.getCuentamayor().getNombrecuenta().equals("DEPRECIACIÓN ACUMULADA")) {
                            totActivoNC = totActivoNC.add(new BigDecimal(String.valueOf(manc.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP);
                        } else if (manc.getCuentamayor().getNombrecuenta().equals("DEPRECIACIÓN ACUMULADA")) {/*se restara de la cuenta propiedad planta y equipo debido a que su  naturaleza del saldo es Acreedor*/
                            totActivoNC = totActivoNC.subtract(new BigDecimal(String.valueOf(manc.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                    //sumando los activos
                    totActivo = totActivo.add(new BigDecimal(String.valueOf(totActivoC.add(new BigDecimal(String.valueOf(totActivoNC)).setScale(2, RoundingMode.HALF_UP)))).setScale(2, RoundingMode.HALF_UP));
                    //acumulando el pasivo corriente
                    for (Mayor mac : pasivoC) {
                        mac.issdsa();
                        if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0) {
                            totPasivoC = totPasivoC.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                    totPasivoC = totPasivoC.add(er.impuestoSobreLaRenta()).setScale(2, RoundingMode.HALF_UP);
                    //acumulando el  pasivo no corriente
                    for (Mayor mac : pasivoNC) {
                        mac.issdsa();
                        if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0) {
                            totPasivoNC = totPasivoNC.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                    //acumulando el patrimonio
                    for (Mayor mac : patrimonio) {
                        mac.issdsa();
                        if (mac.getSaldoDeudor().floatValue() != 0 || mac.getSaldoAcreedor().floatValue() != 0) {
                            totPatrimonio = totPatrimonio.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                        }
                    }
                    totPatrimonio = totPatrimonio.add(er.reservaLegal().add(er.utilidadDelEjercicio())).setScale(2, RoundingMode.HALF_UP);
                    //sumando los totales de los pasivos + patrimonio
                    totPT = totPT.add(new BigDecimal(String.valueOf(totPatrimonio.add(new BigDecimal(String.valueOf(totPasivoC.add(new BigDecimal(String.valueOf(totPasivoNC))))))))).setScale(2, RoundingMode.HALF_UP);

                    //totales activo
                    cell = new PdfPCell(new Paragraph("  Activo", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    //activo corriente
                    cell = new PdfPCell(new Paragraph("    Corriente", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totActivoC.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tabla.addCell(cell);
                    //generando cuentas de activo corriente
                    for (Mayor mac : activoC) {
                        if (mac.getSaldoDeudor().floatValue() != 0 && mac.getSaldoAcreedor().floatValue() == 0 && !mac.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA")) {
                            cell = new PdfPCell(new Paragraph("      " + mac.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                            cell.setColspan(2);
                            tabla.addCell(cell);
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(mac.getSaldoDeudor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            tabla.addCell(cell);
                            cell = new PdfPCell();
                            tabla.addCell(cell);
                        } else if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0 && !mac.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA")) {
                            cell = new PdfPCell(new Paragraph("      " + mac.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                            cell.setColspan(2);
                            tabla.addCell(cell);
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(mac.getSaldoAcreedor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            tabla.addCell(cell);
                            cell = new PdfPCell();
                            tabla.addCell(cell);
                        }
                    }
                    cell = new PdfPCell(new Paragraph("      INVENTARIO FINAL", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getInventariofinal().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);

                    //Activo no corriente
                    cell = new PdfPCell(new Paragraph("    No corriente", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totActivoNC.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tabla.addCell(cell);
                    //generando cuentas de activo no corriente
                    for (Mayor mancc : activoNC) {
                        if (mancc.getSaldoDeudor().floatValue() != 0 && mancc.getSaldoAcreedor().floatValue() == 0 && !mancc.getCuentamayor().getNombrecuenta().equals("INVENTARIO DE MERCADERIA")) {
                            cell = new PdfPCell(new Paragraph("      " + mancc.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                            cell.setColspan(2);
                            tabla.addCell(cell);
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(mancc.getSaldoDeudor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            tabla.addCell(cell);
                            cell = new PdfPCell();
                            tabla.addCell(cell);
                        }
                    }
                    //total activo corrinte + no corriente
                    cell = new PdfPCell(new Paragraph("  Total Activo", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    if (totActivo.equals(totPT)) {
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totActivo.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totActivo.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.WHITE)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.RED);
                        tabla.addCell(cell);

                    }
                    //totales pasivo
                    cell = new PdfPCell(new Paragraph("  Pasivo", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    //pasivo corriente
                    cell = new PdfPCell(new Paragraph("      Corriente", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPasivoC.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tabla.addCell(cell);
                    //generando cuentas de pasivo corriente
                    for (Mayor pac : pasivoC) {
                        if (pac.getSaldoDeudor().floatValue() == 0 && pac.getSaldoAcreedor().floatValue() != 0) {
                            cell = new PdfPCell(new Paragraph("      " + pac.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                            cell.setColspan(2);
                            tabla.addCell(cell);
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(pac.getSaldoAcreedor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            tabla.addCell(cell);
                            cell = new PdfPCell();
                            tabla.addCell(cell);
                        }
                    }
                    cell = new PdfPCell(new Paragraph("      IMPUESTO SOBRE LA RENTA", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.impuestoSobreLaRenta().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);

                    //pasivo no corriente
                    cell = new PdfPCell(new Paragraph("      No corriente", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPasivoNC.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tabla.addCell(cell);
                    //generando las cuentas de pasivo no corriente
                    for (Mayor panc : pasivoNC) {
                        if (panc.getSaldoDeudor().floatValue() == 0 && panc.getSaldoAcreedor().floatValue() != 0) {
                            cell = new PdfPCell(new Paragraph("      " + panc.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                            cell.setColspan(2);
                            tabla.addCell(cell);
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(panc.getSaldoAcreedor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            tabla.addCell(cell);
                            cell = new PdfPCell();
                            tabla.addCell(cell);
                        }
                    }

                    //total pasivos
                    cell = new PdfPCell(new Paragraph("  Total Pasivo", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPasivoC.add(totPasivoNC).setScale(2, RoundingMode.HALF_UP).toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tabla.addCell(cell);
                    //patrimonio
                    cell = new PdfPCell(new Paragraph("  Patrimonio", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPatrimonio.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tabla.addCell(cell);
                    //generando las cuentas de patrimonio

                    for (Mayor patri : patrimonio) {
                        if (patri.getSaldoDeudor().floatValue() != 0 || patri.getSaldoAcreedor().floatValue() != 0) {
                            if (patri.getSaldoDeudor().floatValue() != 0 && patri.getSaldoAcreedor().floatValue() == 0) {
                                cell = new PdfPCell(new Paragraph("      " + patri.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                cell.setColspan(2);
                                tabla.addCell(cell);
                                cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(patri.getSaldoDeudor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                cell = new PdfPCell();
                                tabla.addCell(cell);
                            } else if (patri.getSaldoDeudor().floatValue() == 0 && patri.getSaldoAcreedor().floatValue() != 0) {
                                cell = new PdfPCell(new Paragraph("      " + patri.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                cell.setColspan(2);
                                tabla.addCell(cell);
                                cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(patri.getSaldoAcreedor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                cell = new PdfPCell();
                                tabla.addCell(cell);
                            }
                        }
                    }

                    cell = new PdfPCell(new Paragraph("      RESERVA LEGAL", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.reservaLegal().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph("      UTILIDAD POR DISTRIBUIR", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.utilidadDelEjercicio().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);

                    //total pasivo mas patrimonio
                    cell = new PdfPCell(new Paragraph("  Total Pasivo + Patrimonio", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                    cell.setColspan(2);
                    tabla.addCell(cell);
                    cell = new PdfPCell();
                    tabla.addCell(cell);
                    if (totActivo.equals(totPT)) {
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPT.setScale(2, RoundingMode.HALF_UP).toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.GREEN);
                        tabla.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPT.setScale(2, RoundingMode.HALF_UP).toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.WHITE)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBackgroundColor(BaseColor.RED);
                        tabla.addCell(cell);
                    }

                } else {
                    Periodo terminado = lc.PeriodoenCurso();
                    if (terminado.getTerminado() == true) {
                        /*acumuladores del balance general despues del cierre*/
                        BigDecimal totActivof = new BigDecimal("0.00");
                        BigDecimal totActivoCf = new BigDecimal("0.00");
                        BigDecimal totActivoNCf = new BigDecimal("0.00");
                        BigDecimal totPasivoCf = new BigDecimal("0.00");
                        BigDecimal totPasivoNCf = new BigDecimal("0.00");
                        BigDecimal totPatrimoniof = new BigDecimal("0.00");
                        BigDecimal totPTf = new BigDecimal("0.00");
                        /*cuentas de mayorizacion despues del cierre*/
                        ArrayList<Mayor> activoCf = (ArrayList<Mayor>) lc.listadoMayorBG(4, "11");
                        ArrayList<Mayor> activoNCf = (ArrayList<Mayor>) lc.listadoMayorBG(4, "12");
                        ArrayList<Mayor> pasivoCf = (ArrayList<Mayor>) lc.listadoMayorBG(4, "21");
                        ArrayList<Mayor> pasivoNCf = (ArrayList<Mayor>) lc.listadoMayorBG(4, "22");
                        ArrayList<Mayor> patrimoniof = (ArrayList<Mayor>) lc.listadoMayorBG(4, "31");

                        //acumulando todo el activo corriente
                        for (Mayor mac : activoCf) {
                            mac.liquidacion();
                            if (mac.getSaldoDeudor().floatValue() != 0 && mac.getSaldoAcreedor().floatValue() == 0 && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {
                                totActivoCf = totActivoCf.add(new BigDecimal(String.valueOf(mac.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP);
                            } else if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0 && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") && !mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {
                                totActivoCf = totActivoCf.subtract(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                            } else if (mac.getCuentamayor().getNombrecuenta().equals("ESTIMACION PARA CUENTAS INCOBRABLES (CR)") || mac.getCuentamayor().getNombrecuenta().equals("ESTIMACIÓN PARA DETERIORO DE INVENTARIO")) {/*estas se restaran del total debido a que su
                                                                comportamiento es como la cuenta de un pasivo se procede a restar el saldo (naturaleza del saldo= ACREEDOR)*/
                                totActivoCf = totActivoCf.subtract(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                            }
                        }
                        //acumulando todo el activo no corriente
                        for (Mayor manc : activoNCf) {
                            manc.liquidacion();
                            if (manc.getSaldoDeudor().floatValue() != 0 && manc.getSaldoAcreedor().floatValue() == 0 && !manc.getCuentamayor().getNombrecuenta().equals("DEPRECIACIÓN ACUMULADA")) {
                                totActivoNCf = totActivoNCf.add(new BigDecimal(String.valueOf(manc.getSaldoDeudor()))).setScale(2, RoundingMode.HALF_UP);
                            } else if (manc.getCuentamayor().getNombrecuenta().equals("DEPRECIACIÓN ACUMULADA")) {/*se restara de la cuenta propiedad planta y equipo debido a que su  naturaleza del saldo es Acreedor*/
                                totActivoNCf = totActivoNCf.subtract(new BigDecimal(String.valueOf(manc.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                            }
                        }
                        //sumando los activos
                        totActivof = totActivof.add(new BigDecimal(String.valueOf(totActivoCf.add(new BigDecimal(String.valueOf(totActivoNCf)).setScale(2, RoundingMode.HALF_UP)))).setScale(2, RoundingMode.HALF_UP));
                        //acumulando el pasivo corriente
                        for (Mayor mac : pasivoCf) {
                            mac.liquidacion();
                            if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0) {
                                totPasivoCf = totPasivoCf.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                            }
                        }
                        //acumulando el  pasivo no corriente
                        for (Mayor mac : pasivoNCf) {
                            mac.liquidacion();
                            if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0) {
                                totPasivoNCf = totPasivoNCf.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                            }
                        }
                        //acumulando el patrimonio
                        for (Mayor mac : patrimoniof) {
                            mac.liquidacion();
                            if (mac.getSaldoDeudor().floatValue() != 0 || mac.getSaldoAcreedor().floatValue() != 0) {
                                totPatrimoniof = totPatrimoniof.add(new BigDecimal(String.valueOf(mac.getSaldoAcreedor()))).setScale(2, RoundingMode.HALF_UP);
                            }
                        }
                        //sumando los totales de los pasivos + patrimonio
                        totPTf = totPTf.add(new BigDecimal(String.valueOf(totPatrimoniof.add(new BigDecimal(String.valueOf(totPasivoCf.add(new BigDecimal(String.valueOf(totPasivoNCf))))))))).setScale(2, RoundingMode.HALF_UP);

                        //totales activo
                        cell = new PdfPCell(new Paragraph("  Activo", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        //activo corriente
                        cell = new PdfPCell(new Paragraph("    Corriente", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totActivoCf.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //generando cuentas de activo corriente
                        for (Mayor mac : activoCf) {
                            if (mac.getSaldoDeudor().floatValue() != 0 && mac.getSaldoAcreedor().floatValue() == 0) {
                                cell = new PdfPCell(new Paragraph("      " + mac.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                cell.setColspan(2);
                                tabla.addCell(cell);
                                cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(mac.getSaldoDeudor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                cell = new PdfPCell();
                                tabla.addCell(cell);
                            } else if (mac.getSaldoDeudor().floatValue() == 0 && mac.getSaldoAcreedor().floatValue() != 0) {
                                cell = new PdfPCell(new Paragraph("      " + mac.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                cell.setColspan(2);
                                tabla.addCell(cell);
                                cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(mac.getSaldoAcreedor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                cell = new PdfPCell();
                                tabla.addCell(cell);
                            }
                        }

                        //Activo no corriente
                        cell = new PdfPCell(new Paragraph("    No corriente", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totActivoNCf.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //generando cuentas de activo no corriente
                        for (Mayor mancc : activoNCf) {
                            if (mancc.getSaldoDeudor().floatValue() != 0 && mancc.getSaldoAcreedor().floatValue() == 0) {
                                cell = new PdfPCell(new Paragraph("      " + mancc.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                cell.setColspan(2);
                                tabla.addCell(cell);
                                cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(mancc.getSaldoDeudor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                cell = new PdfPCell();
                                tabla.addCell(cell);
                            }
                        }
                        //total activo corrinte + no corriente
                        cell = new PdfPCell(new Paragraph("  Total Activo", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        if (totActivof.equals(totPTf)) {
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totActivof.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setBackgroundColor(BaseColor.GREEN);
                            tabla.addCell(cell);
                        } else {
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totActivof.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.WHITE)));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setBackgroundColor(BaseColor.RED);
                            tabla.addCell(cell);

                        }
                        //totales pasivo
                        cell = new PdfPCell(new Paragraph("  Pasivo", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        //pasivo corriente
                        cell = new PdfPCell(new Paragraph("      Corriente", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPasivoCf.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //generando cuentas de pasivo corriente
                        for (Mayor pac : pasivoCf) {
                            if (pac.getSaldoDeudor().floatValue() == 0 && pac.getSaldoAcreedor().floatValue() != 0) {
                                cell = new PdfPCell(new Paragraph("      " + pac.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                cell.setColspan(2);
                                tabla.addCell(cell);
                                cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(pac.getSaldoAcreedor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                cell = new PdfPCell();
                                tabla.addCell(cell);
                            }
                        }

                        //pasivo no corriente
                        cell = new PdfPCell(new Paragraph("      No corriente", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPasivoNCf.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //generando las cuentas de pasivo no corriente
                        for (Mayor panc : pasivoNCf) {
                            if (panc.getSaldoDeudor().floatValue() == 0 && panc.getSaldoAcreedor().floatValue() != 0) {
                                cell = new PdfPCell(new Paragraph("      " + panc.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                cell.setColspan(2);
                                tabla.addCell(cell);
                                cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(panc.getSaldoAcreedor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                tabla.addCell(cell);
                                cell = new PdfPCell();
                                tabla.addCell(cell);
                            }
                        }

                        //total pasivos
                        cell = new PdfPCell(new Paragraph("  Total Pasivo", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPasivoCf.add(totPasivoNCf).setScale(2, RoundingMode.HALF_UP).toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //patrimonio
                        cell = new PdfPCell(new Paragraph("  Patrimonio", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPatrimoniof.toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //generando las cuentas de patrimonio

                        for (Mayor patri : patrimoniof) {
                            if (patri.getSaldoDeudor().floatValue() != 0 || patri.getSaldoAcreedor().floatValue() != 0) {
                                if (patri.getSaldoDeudor().floatValue() != 0 && patri.getSaldoAcreedor().floatValue() == 0) {
                                    cell = new PdfPCell(new Paragraph("      " + patri.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                    cell.setColspan(2);
                                    tabla.addCell(cell);
                                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(patri.getSaldoDeudor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                    tabla.addCell(cell);
                                    cell = new PdfPCell();
                                    tabla.addCell(cell);
                                } else if (patri.getSaldoDeudor().floatValue() == 0 && patri.getSaldoAcreedor().floatValue() != 0) {
                                    cell = new PdfPCell(new Paragraph("      " + patri.getCuentamayor().getNombrecuenta(), FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                                    cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                                    cell.setColspan(2);
                                    tabla.addCell(cell);
                                    cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(patri.getSaldoAcreedor().toString())), FontFactory.getFont("courier", 10, Font.NORMAL, BaseColor.BLACK)));
                                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                    tabla.addCell(cell);
                                    cell = new PdfPCell();
                                    tabla.addCell(cell);
                                }
                            }
                        }

                        //total pasivo mas patrimonio
                        cell = new PdfPCell(new Paragraph("  Total Pasivo + Patrimonio", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        tabla.addCell(cell);
                        if (totActivof.equals(totPTf)) {
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPTf.setScale(2, RoundingMode.HALF_UP).toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.BLACK)));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setBackgroundColor(BaseColor.GREEN);
                            tabla.addCell(cell);
                        } else {
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(totPTf.setScale(2, RoundingMode.HALF_UP).toString())), FontFactory.getFont("courier", 10, Font.BOLD, BaseColor.WHITE)));
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setBackgroundColor(BaseColor.RED);
                            tabla.addCell(cell);
                        }

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
