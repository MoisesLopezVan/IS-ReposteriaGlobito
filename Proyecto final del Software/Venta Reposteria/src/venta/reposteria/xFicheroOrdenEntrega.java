/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package venta.reposteria;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class xFicheroOrdenEntrega {

    private JFileChooser fileChooser;
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivo de Texto","txt");
    private File file = null;
    private boolean isopen =false;//bandera de control para saber si se abrio un archivo
    private ArrayList contenido = new ArrayList();//almacena los registros leidos de *.txt
    private int index = 0; //lleva control del registro actualmente visible
    //controles swing
    private JTextField colonia;
    private JTextField ciudad;
    private JTextField telefonoFijo;
    private JTextField telefonoMovil;
    private JTextField correo;
    private JTextArea observaciones;
    private JTextField fechaEntrega;
    private JTextField horaEntrega;
    private JTextField referencias;
    private JTextField descuento;
    private JTextField anticipo;
    private JTextField totalPagar;
    private JTextField importe;
    private JTextField cambio;
    private JTextField precio;
    private JTextField identificacion;
    private JTextField producto;
    private JTextField nombreCliente;
    //Constructor de clase
    public xFicheroOrdenEntrega(){}


    public xFicheroOrdenEntrega(String text, String text0, String text1, String text2, String text3, String text4, String text5, String text6, String text7, String text8, String text9, String text10, String text11, String text12, String text13, String text14, String text15) {
        
        this.nombreCliente = nombreCliente;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.telefonoFijo = telefonoFijo;
        this.telefonoMovil = telefonoMovil;
        this.correo = correo;
        this.observaciones = observaciones;
        this.fechaEntrega = fechaEntrega;
        this.horaEntrega = horaEntrega;
        this.referencias = referencias;
        this.descuento = descuento;
        this.anticipo = anticipo;
        this.totalPagar = totalPagar;
        this.importe = importe;
        this.cambio = cambio;
        this.precio = precio;
        this.identificacion = identificacion;
        this.producto = producto;
    }


    //Retorna el nombre del archivo abierto
    public String getFileName()
    {
        if( file != null)
            return file.getName();
        else
            return "Sin Titulo";
    }

    /* Abre la cja de dialogo Guardar como
        Input: String de la forma "campo1,campo2,campo3"
     */
    public void GuardarComo(String texto)
    {       
       fileChooser = new JFileChooser();
       fileChooser.setFileFilter(filter);
       int result = fileChooser.showSaveDialog(null);
       if ( result == JFileChooser.APPROVE_OPTION ){
                this.isopen = false;
                this.contenido.clear();
                this.index=1;
                if ( escribir( fileChooser.getSelectedFile(),  texto) )
                {
                    JOptionPane.showMessageDialog(null, "Archivo ' " + fileChooser.getSelectedFile().getName() + "' guardado ");
                    this.isopen=true;
                }
        }
    }

    /* Actualiza nuevo registro al final de la lista
     * input: String de la forma "campo1,campo2,campo3"
     */
    public void Actualizar(String texto)
    {
        //Si existe archivo abierto
        if( this.file != null)
        {
            if ( escribir( this.file ,  texto) )
            {
                JOptionPane.showMessageDialog(null, "Archivo '" + this.file.getName() + "' actualizado ");
            }                        
        }
        else //sino crear nuevo archivo
        {            
            GuardarComo( texto );
        }
    }

    /* Muestra la ventana de dialogo Abrir archivo
     */
    public void Abrir()
    {
     fileChooser = new JFileChooser();
       fileChooser.setFileFilter(filter);
       //fileChooser.setCurrentDirectory(new java.io.File("e:/"));       
       int result = fileChooser.showOpenDialog(null);
       if ( result == JFileChooser.APPROVE_OPTION ){
                this.file = fileChooser.getSelectedFile();                
                leer( this.file );
                this.isopen=true;
        }
    }

    /* Función que escribe un registro en el archivo de texto
     * Si el archivo ya contaba con registros re-escribe estos y al final
     * escribe el nuevo registro
     */
    private boolean escribir(File fichero, String texto)
    {
        boolean res=false;        
        PrintWriter writer = null;
        try {
            String f = fichero.toString();
            //verifica que extension exista sino lo agrega
            if(!f.substring( f.length()-4, f.length()).equals(".txt") )
            {
                f = f + ".txt";
                fichero = new File(f);
            }            
            writer = new PrintWriter( fichero );
            //si hay un archivo abierto
            if( this.isopen )
            {   //añade primero linea por linea conenido anterior
                Iterator It = contenido.iterator();
                while (It.hasNext())
                {
                    writer.println( It.next() );
                }
                //se añade fila de texto al archivo
                writer.println( texto );
                this.contenido.add(texto);
            }
            else //esta guardando por primera vez
            {
                this.contenido.add(texto);
                writer.println( texto );    
            }            
            this.file = fichero;
            writer.close();            
            res = true;
        } catch (FileNotFoundException ex) {
            System.out.println("Error:" + ex);
        } finally {
            writer.close();
        }
        return res;
    }

    /* Lee linea por linea un archivo de texto y almacena los registros
     * en un ArrayList segun orden de lectura
     * input: File
     */
    public boolean leer( File fichero )
    {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fichero));
            this.contenido.clear();
            String linea;
            while ( (linea = reader.readLine() ) != null) {                
                this.contenido.add( linea );
            }
            //muestra el primer registro en la interfaz
            Siguiente();
            return true;
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }  finally {
            try {
                reader.close();
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
            }
        }
        return false;
    }

    /* funcion qye avanza al siguiente registro del ArrayList y lo muestra en pantalla
     */
    public void Siguiente()
    {
        if( this.file != null )
        {
        //incrementa en 1 la variable "index", si se supera el tamaño de lineas, vuelve a valor 1
        this.index = (index>=contenido.size())? 1 : index + 1;
        int count =1;
        Iterator It = contenido.iterator();
        //comienza busqueda
        while (It.hasNext())
        {
            String tmp = It.next().toString();
            if( count == index)//si lo encuentra asiga valores
            {   //separa el registro por campos. Separador = ","
                String[] datos = tmp.split(",");                                
                this.nombreCliente.setText(datos[0]);
                this.identificacion.setText(datos[1]);
                this.producto.setText(datos[2]);
                break;
            }
            count ++;
        }
        }
    }

}

