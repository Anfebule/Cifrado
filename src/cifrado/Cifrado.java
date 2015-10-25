/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrado;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Software que cifra y descifra un archivo
 * 
 * A tener en cuenta: Character.getNumericValue
 * 
 * @author Andres Felipe Buitrago Lesmes
 */
public class Cifrado {
    
    //public static int[][] matrizTextoBits = new int [8][8];
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Ingrese la ruta del archivo: ");
//        String rutaArchivo = sc.next();
        
        Cifrado c = new Cifrado();
        c.cifrar();
    }
    
    /**
     * Método que cifra un archivo
     * 
     * @throws IOException 
     */
    public void cifrar() throws IOException{

        //Lee el archivo
        String clave = "Hola mundo";
        FileReader fr = new FileReader("C:\\Users\\Andres\\Dropbox\\Universidad\\Criptografia\\Proyecto_Final\\TextoClaro.txt");
        BufferedReader br = new BufferedReader(fr);
        String lineaTexto;
        char[] lineaTextoBits;
        char[] lineaClaveBits;
        int[][] matrizTextoBits = new int [8][8];
        int[][] matrizClaveBits = new int [clave.length()][8];
        int cont = 0;
        //System.out.println("Length clave: "+clave.length());
        //Convierte la clave en bits
        for (char letraClave : clave.toCharArray()){
                
            //cada letra se convierte en un array de bits
            String bits = Integer.toBinaryString(letraClave);
            lineaClaveBits = bits.toCharArray();
            lineaClaveBits = completaFila(lineaClaveBits);
            //System.out.println(Arrays.toString(lineaClaveBits));
            matrizClaveBits = lineaClaveEnMatriz(lineaClaveBits, matrizClaveBits, cont);
            cont++;
        }
        
        while ((lineaTexto = br.readLine()) != null){
            
            //Convierte la linea en array
            char[] lineaTextoEnBytes = lineaTexto.toCharArray();
            System.out.println(Arrays.toString(lineaTextoEnBytes));
            //System.out.println("Tamaño: "+lineaTextoEnBytes.length);
            cont = 0;
            for (char letraTexto : lineaTextoEnBytes){
                
                //cada letra se convierte en un array de bits
                String bits = Integer.toBinaryString(letraTexto);
                lineaTextoBits = bits.toCharArray();
                lineaTextoBits = completaFila(lineaTextoBits);
                //System.out.println(Arrays.toString(lineaTextoBits));
                matrizTextoBits = lineaTextoEnMatriz(lineaTextoBits, matrizTextoBits, cont);
                cont++;
                
                //Cuando se completa el bloque de 64 bits
                if (cont == 7){
                    
                }
            }
        }
        
    }
    
    /**
     * Método que llena la linea con ceros al inicio si no tiene 8 bits
     * 
     * @param lineaBits
     * @return 
     */
    public char[] completaFila (char[] lineaBits){
        
        char[] lineaBitsAgregado = new char[8];
        
        if(lineaBits.length == 7){
            
            lineaBitsAgregado[0] = '0';
            for(int i=1; i<8; i++){
                lineaBitsAgregado[i] = lineaBits[i-1];
            }
            return lineaBitsAgregado;
        } else if (lineaBits.length == 6) {
            
            lineaBitsAgregado[0] = '0';
            lineaBitsAgregado[1] = '0';
            for(int i=2; i<8; i++){
                lineaBitsAgregado[i] = lineaBits[i-2];
            }
            return lineaBitsAgregado;
        }
        return lineaBits;
    }
    
    public int[][] lineaTextoEnMatriz(char[] lineaTextoBits, int[][] matrizTextoBits, int fila){
        for(int i = 0; i<lineaTextoBits.length; i++){
            matrizTextoBits[fila][i] = Character.getNumericValue(lineaTextoBits[i]);
        }
        
        System.out.println("Texto en bits: ");
        for(int i = 0; i<8; i++){
            for(int j= 0; j<8; j++){
                System.out.print(matrizTextoBits[i][j]+" ");
            }
            System.out.println();
        }
        return matrizTextoBits;
    }
    
    public int[][] lineaClaveEnMatriz(char[] lineaClaveBits, int[][] matrizClaveBits, int fila){
        for(int i = 0; i<lineaClaveBits.length; i++){
            matrizClaveBits[fila][i] = Character.getNumericValue(lineaClaveBits[i]);
        }
//        System.out.println("Clave en bits: ");
//        for(int i = 0; i<10; i++){
//            for(int j= 0; j<8; j++){
//                System.out.print(matrizClaveBits[i][j]+" ");
//            }
//            System.out.println();
//        }
        return matrizClaveBits;
    }
}
