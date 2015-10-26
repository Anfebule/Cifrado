/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrado;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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

//        Scanner sc = new Scanner(System.in);
//        System.out.println("Ingrese la ruta del archivo: ");
//        String rutaArchivo = sc.next();
        String clave = "Hola mundo";
        String ubicacionArchivo = "C:\\Users\\Andres\\Dropbox\\Universidad\\Criptografia\\Proyecto_Final\\TextoClaro.txt";
        //String ubicacionArchivo = "C:\\Users\\Andres\\Documents\\NetBeansProjects\\Cifrado\\TextoCifrado.txt";
        char cifrar = 's';
        Cifrado c = new Cifrado();
        if(cifrar == 's'){
            c.cifrar(clave, ubicacionArchivo);
        } else {
            c.descifrar(clave, ubicacionArchivo);
        }
        
    }
    
    /**
     * Método que cifra un archivo
     * 
     * @param clave
     * @param ubicacionArchivo
     * @throws IOException 
     */
    public void cifrar(String clave, String ubicacionArchivo) throws IOException{

        //Lee el archivo
        FileReader fr = new FileReader(ubicacionArchivo);
        BufferedReader br = new BufferedReader(fr);
        String lineaTexto;
        char[] letraTextoBits;
        int[][] matrizTextoBits = new int [8][8];
        int[][] matrizClaveBits = new int [clave.length()][8];
        int[][] matrizTextoCifradoBits = new int [8][8];
        //System.out.println("Length clave: "+clave.length());
        //Convierte la clave en bits
        matrizClaveBits = claveEnBits(matrizClaveBits, clave);
        
        while ((lineaTexto = br.readLine()) != null){
            
            //Convierte la linea en array
            char[] lineaTextoEnBytes = lineaTexto.toCharArray();
            System.out.println(Arrays.toString(lineaTextoEnBytes));
            //System.out.println("Tamaño: "+lineaTextoEnBytes.length);
            int cont = 0;
            for (char letraTexto : lineaTextoEnBytes){
                
                //cada letra se convierte en un array de bits
                String bits = Integer.toBinaryString(letraTexto);
                letraTextoBits = bits.toCharArray();
                letraTextoBits = completaFila(letraTextoBits);
                //System.out.println(Arrays.toString(lineaTextoBits));
                
                matrizTextoBits = letraTextoEnMatriz(letraTextoBits, matrizTextoBits, cont);
                cont++;
                
                //Cuando se completa el bloque de 64 bits se cifra
                if (cont == 8){
                    System.out.println("cierra bloque.");
                    cifrarBloque(matrizTextoBits, matrizClaveBits, matrizTextoCifradoBits);
                    matrizTextoBits = new int [8][8];
                    cont = 0;
                }
            }
        }
        
    }
    
    public void descifrar(String clave, String ubicacionArchivo) throws IOException{
        FileReader fr = new FileReader(ubicacionArchivo);
        BufferedReader br = new BufferedReader(fr);
        String lineaTexto;
        char[] letraTextoBits;
        int[][] matrizTextoBits = new int [8][8];
        int[][] matrizClaveBits = new int [clave.length()][8];
        int[][] matrizTextoClaroBits = new int [8][8];
        
        matrizClaveBits = claveEnBits(matrizClaveBits, clave);
        
        //Lee el archivo
        while ((lineaTexto = br.readLine()) != null){
            //Convierte la linea en array
            char[] lineaTextoEnBytes = lineaTexto.toCharArray();
            System.out.println(Arrays.toString(lineaTextoEnBytes));
            //System.out.println("Tamaño: "+lineaTextoEnBytes.length);
            int cont = 0;
            for (char letraTexto : lineaTextoEnBytes){
                String bits = Integer.toBinaryString(letraTexto);
                letraTextoBits = bits.toCharArray();
                letraTextoBits = completaFila(letraTextoBits);
                matrizTextoBits = letraTextoEnMatriz(letraTextoBits, matrizTextoBits, cont);
                cont++;
                
                //Cuando se completa el bloque de 64 bits se descifra
                if (cont == 8){
                    System.out.println("cierra bloque a descifrar.");
                    descifrarBloque(matrizTextoBits, matrizClaveBits, matrizTextoClaroBits);
                    matrizTextoBits = new int [8][8];
                    cont = 0;
                }
            }
        }
    }
    
    public int[][] claveEnBits (int[][] matrizClaveBits, String clave){
        
        int cont = 0;
        char[] letraClaveBits;
        for (char letraClave : clave.toCharArray()){
                
            //cada letra se convierte en un array de bits
            String bits = Integer.toBinaryString(letraClave);
            letraClaveBits = bits.toCharArray();
            letraClaveBits = completaFila(letraClaveBits);
            //System.out.println(Arrays.toString(lineaClaveBits));
            matrizClaveBits = letraClaveEnMatriz(letraClaveBits, matrizClaveBits, cont);
            cont++;
        }
        
        return matrizClaveBits;
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
    
    public int[][] letraTextoEnMatriz(char[] letraTextoBits, int[][] matrizTextoBits, int fila){
        for(int i = 0; i<8; i++){
            matrizTextoBits[fila][i] = Character.getNumericValue(letraTextoBits[i]);
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
    
    public int[][] letraClaveEnMatriz(char[] letraClaveBits, int[][] matrizClaveBits, int fila){
        for(int i = 0; i<letraClaveBits.length; i++){
            matrizClaveBits[fila][i] = Character.getNumericValue(letraClaveBits[i]);
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
    
    public void cifrarBloque(int[][] matrizTextoBits, int[][] matrizClaveBits, int[][] matrizTextoCifradoBits) throws IOException{
        //Realiza XOR con cada bit
        for(int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                matrizTextoCifradoBits[i][j] = matrizTextoBits[i][j] ^ matrizClaveBits[i][j];
            }
        }
        
//        System.out.println("Matriz texto claro: ");
//        for(int i=0; i<8; i++){
//            for (int j=0; j<8; j++){
//                System.out.print(matrizTextoBits[i][j]+" ");
//            }
//            System.out.println();
//        }
//        
//        System.out.println("Matriz clave: ");
//        for(int i=0; i<8; i++){
//            for (int j=0; j<8; j++){
//                System.out.print(matrizClaveBits[i][j]+" ");
//            }
//            System.out.println();
//        }
//        
//        System.out.println("Matriz texto cifrado: ");
//        for(int i=0; i<8; i++){
//            for (int j=0; j<8; j++){
//                System.out.print(matrizTextoCifradoBits[i][j]+" ");
//            }
//            System.out.println();
//        }
        
        //Escribe el texto cifrado en un archivo
        char[] letraCifradaBits = new char[8];
        FileWriter fw = new FileWriter("textoCifrado.txt", true);
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            for(int i=0; i<8; i++){
                for (int j=0; j<8; j++){
                    letraCifradaBits[j] = Character.forDigit(matrizTextoCifradoBits[i][j], 2) ;
                }
                StringBuilder sb = new StringBuilder();
                String letraCifrada = new String(letraCifradaBits);
                sb.append((char)Integer.parseInt(letraCifrada, 2));
                bw.write(sb.toString());
                //System.out.print(sb.toString());
                //System.out.println();
            }
        }
    }
    
    public void descifrarBloque(int[][] matrizTextoBits, int[][] matrizClaveBits, int[][] matrizTextoClaroBits) throws IOException{
        for(int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                matrizTextoClaroBits[i][j] = matrizTextoBits[i][j] ^ matrizClaveBits[i][j];
            }
        }
        
        //Escribe el texto descifrado en un archivo
        char[] letraCifradaBits = new char[8];
        FileWriter fw = new FileWriter("textoDescifrado.txt", true);
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            for(int i=0; i<8; i++){
                for (int j=0; j<8; j++){
                    letraCifradaBits[j] = Character.forDigit(matrizTextoClaroBits[i][j], 2) ;
                }
                StringBuilder sb = new StringBuilder();
                String letraCifrada = new String(letraCifradaBits);
                sb.append((char)Integer.parseInt(letraCifrada, 2));
                bw.write(sb.toString());
                //System.out.print(sb.toString());
                //System.out.println();
            }
        }
    }
}
