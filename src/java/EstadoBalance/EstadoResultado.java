/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EstadoBalance;

import Procesos.EstadodeResultado;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
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
public class EstadoResultado extends HttpServlet {

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
                Periodo actual = lc.PeriodoenCurso();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
                simbolo.setDecimalSeparator('.');
                simbolo.setGroupingSeparator(',');
                DecimalFormat f = new DecimalFormat("###,##0.00", simbolo);
                if (actual != null) {
                    Inventario i = lc.recuperaInventario("Inventario Final", actual.getIdperiodo());
                    if (i != null) {
                        EstadodeResultado er = new EstadodeResultado(i.getValor());
                        //variables para la creacion del EstadoResultado
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
                        par1 = new Paragraph(new Phrase("ESTADO DE RESULTADOS", fonttitulo));
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

                        PdfPTable tabla = new PdfPTable(3);//tabla
                        PdfPCell cell;
                        tabla.setWidths(new int[]{3, 1, 1});
                        //PdfPCell celda1=new PdfPCell(new Paragraph(""));
                        //ventas
                        cell = new PdfPCell(new Paragraph("    Ventas", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setColspan(2);
                        cell.setBorder(0);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getVentas().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //rebajas sobre ventas
                        cell = new PdfPCell(new Paragraph("(-) Rebajas sobre ventas", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getRebajassobreventas().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //devoluciones sobre ventas
                        cell = new PdfPCell(new Paragraph("(-) Devoluciones sobre ventas", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setColspan(2);
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getDevolucionessobreventas().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //ventas netas
                        cell = new PdfPCell(new Paragraph("(=) Ventas netas", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setColspan(2);
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.ventasNetas().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //costo de ventas
                        cell = new PdfPCell(new Paragraph("(-) Costo de ventas", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setColspan(2);
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.costoVentas().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //compras
                        cell = new PdfPCell(new Paragraph("    Compras", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getCompras().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //gastos sobre compras
                        cell = new PdfPCell(new Paragraph("(+) Gastos sobre compras", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getGastosobrecompras().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //compras totales
                        cell = new PdfPCell(new Paragraph("(=) Compras totales", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.comprasTotales().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //rebajas sobre compras
                        cell = new PdfPCell(new Paragraph("(-) Rebajas sobre compras", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getRebajassobrecompras().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //devoluciones sobre compras
                        cell = new PdfPCell(new Paragraph("(-) Devoluciones sobre compras", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getDevolucionessobrecompras().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //compras netas
                        cell = new PdfPCell(new Paragraph("(=) Compras netas", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.comprasNetas().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //inventario inicial
                        cell = new PdfPCell(new Paragraph("(+) Inventario inicial", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getInventarioinicial().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //mercaderia disponible
                        cell = new PdfPCell(new Paragraph("(=) Mercadería disponible", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.mercaderiaDisponible().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //inventario final
                        cell = new PdfPCell(new Paragraph("(-) Inventario final", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getInventariofinal().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //costo de ventas
                        cell = new PdfPCell(new Paragraph("(=) Costo de ventas", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.costoVentas().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //utilidad bruta
                        cell = new PdfPCell(new Paragraph("(=) Utilidad bruta", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.utilidadBruta().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //total gastos de operación
                        cell = new PdfPCell(new Paragraph("(-) Gastos de operación", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.gastosOperacion().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //detalle de los gastos
                        cell = new PdfPCell(new Paragraph("    Gastos de administración", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getGastosadmon().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph("    Gastos de venta", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getGastosventa().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph("    Gastos financieros", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getGastosfinancieros().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        cell = new PdfPCell();
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //utilidad de operación
                        cell = new PdfPCell(new Paragraph("(=) Utilidad de operación", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.utilidadOperacion().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //otros gastos
                        cell = new PdfPCell(new Paragraph("(-) Otros gastos", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getOtrosgastos().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //otros ingresos
                        cell = new PdfPCell(new Paragraph("(+) Otros ingresos", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.getOtrosingresos().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //utlidad antes de impuesto y reserva
                        cell = new PdfPCell(new Paragraph("(=) Utilidad antes de impuesto y reservas", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.utilidadAntesImpuestoReservas().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //reserva legal
                        cell = new PdfPCell(new Paragraph("(-) Reserva legal 7%", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.reservaLegal().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //utilidad antes de impuesto
                        cell = new PdfPCell(new Paragraph("(=) Utilidad antes del impuesto", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.utilidadAntesdelImpuesto().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tabla.addCell(cell);
                        //impuesto sobre la renta
                        cell = new PdfPCell(new Paragraph("(-) Impuesto sobre la renta", FontFactory.getFont("courier", 12, Font.NORMAL, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.impuestoSobreLaRenta().toString())), FontFactory.getFont("courier", 11, Font.NORMAL, BaseColor.BLACK)));
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        cell.setBorder(0);
                        tabla.addCell(cell);
                        //utilidad del ejercicio
                        cell = new PdfPCell(new Paragraph("(=) Utilidad del ejercicio", FontFactory.getFont("courier", 12, Font.BOLD, BaseColor.BLACK)));
                        cell.setBorder(0);
                        cell.setColspan(2);
                        tabla.addCell(cell);
                        if (er.utilidadDelEjercicio().floatValue() > 0) {
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.utilidadDelEjercicio().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.WHITE)));
                            cell.setBorder(0);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setBackgroundColor(BaseColor.GREEN);
                            tabla.addCell(cell);
                        } else {
                            cell = new PdfPCell(new Paragraph(f.format(Double.parseDouble(er.utilidadDelEjercicio().toString())), FontFactory.getFont("courier", 11, Font.BOLD, BaseColor.WHITE)));
                            cell.setBorder(0);
                            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            cell.setBackgroundColor(BaseColor.RED);
                            tabla.addCell(cell);
                        }

                        //agregando la tabla al documento
                        documento.add(tabla);
                        documento.close();
                    }
                }

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



//foter 
