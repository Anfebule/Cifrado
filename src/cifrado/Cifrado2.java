/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrado;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * Software para cifrar y descifrar un texto mediante operaciones XOR con bloques de 64 bits
 * 
 * @author Andres
 */
public class Cifrado2 {
    public static void main(String args[]) throws FileNotFoundException, IOException{
        String clave = "#@$%&@23";
        //String ubicacionArchivo = "C:\\Users\\Andres\\Dropbox\\Universidad\\Criptografia\\Proyecto_Final\\TextoClaro.txt";
        //String ubicacionArchivo = "C:\\Users\\Andres\\Documents\\NetBeansProjects\\Cifrado\\TextoCifrado.txt";
        char cifrar = 'n';
        Cifrado2 c = new Cifrado2();
        if(cifrar == 's'){
            File f = new File("textoCifrado.txt");
            if(f.exists() && !f.isDirectory()) { 
                f.delete();
            }
            String ubicacionArchivo = "C:\\Users\\Andres\\Dropbox\\Universidad\\Criptografia\\Proyecto_Final\\TextoClaro.txt";
            c.cifrar(clave, ubicacionArchivo);
            
        } else {
            File f = new File("textoDescifrado.txt");
            if(f.exists() && !f.isDirectory()) { 
                f.delete();
            }
            String ubicacionArchivo = "C:\\Users\\Andres\\Documents\\NetBeansProjects\\Cifrado\\textoCifrado.txt";
            c.descifrar(clave, ubicacionArchivo);
        }
    }
    
    /**
     * M{etodo que cifra un texto
     * 
     * @param clave
     * @param ubicacionArchivo
     * @throws FileNotFoundException
     * @throws IOException 
     */
    protected void cifrar(String clave, String ubicacionArchivo) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(ubicacionArchivo);
        BufferedReader br = new BufferedReader(fr);
        String lineaTexto;
        int cont = 0;
        int[][] matrizTextoBits = new int [8][8];
        int[][] matrizClaveBits = new int [clave.length()][8];
        FileWriter fw = new FileWriter("textoCifrado.txt", true);
        
        matrizClaveBits = claveEnBits(matrizClaveBits, clave);

        while ((lineaTexto = br.readLine()) != null){
            byte[] lineaEnBytes = lineaTexto.getBytes(StandardCharsets.UTF_8);
            StringBuilder binario = new StringBuilder();
            System.out.println("lineaTexto: "+lineaTexto);
            for(byte letra: lineaEnBytes){
                System.out.println((char)letra);
                int val = letra;
                for (int i = 0; i < 8; i++)
                {
                    binario.append((val & 128) == 0 ? 0 : 1);
                    val <<= 1;
                }

                String[] letraTextoBits = binario.toString().split("");

                letraTextoEnMatriz(letraTextoBits, matrizTextoBits, cont);
                cont++;

                if (cont == 8){
                    System.out.println("cierra bloque.");
                    cifrarBloque(matrizTextoBits, matrizClaveBits, fw, cont);
                    matrizTextoBits = new int [8][8];
                    cont = 0;
                }
                binario.delete(0, binario.length());
            }
            int validar = 0;
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    validar += matrizTextoBits[i][j];
                }
            }
            if(validar != 0){
                System.out.println("cierra bloque linea.");
                cifrarBloque(matrizTextoBits, matrizClaveBits, fw, cont);
            }
            matrizTextoBits = new int [8][8];
            binario.delete(0, binario.length());
            cont = 0;
            fw.write(System.lineSeparator());
        }
        fw.close();
    }
    
    /**
     * M{etodo que descifra el texto cifrado
     * 
     * @param clave
     * @param ubicacionArchivo
     * @throws FileNotFoundException
     * @throws IOException 
     */
    protected void descifrar(String clave, String ubicacionArchivo) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(ubicacionArchivo);
        BufferedReader br = new BufferedReader(fr);
        String lineaTexto;
        int cont = 0;
        int[][] matrizTextoBits = new int [8][8];
        int[][] matrizClaveBits = new int [clave.length()][8];
        
        matrizClaveBits = claveEnBits(matrizClaveBits, clave);
        FileWriter fw = new FileWriter("textoDescifrado.txt", true);
        
        while ((lineaTexto = br.readLine()) != null){
            byte[] lineaEnBytes = lineaTexto.getBytes(StandardCharsets.ISO_8859_1);
            StringBuilder binario = new StringBuilder();
            for(byte letra: lineaEnBytes){
                int val = letra;
                for (int i = 0; i < 8; i++)
                {
                   binario.append((val & 128) == 0 ? 0 : 1);
                   val <<= 1;
                }
                
                String[] letraTextoBits = binario.toString().split("");
                
                letraTextoEnMatriz(letraTextoBits, matrizTextoBits, cont);
                cont++;
                
                if (cont == 8){
                    //System.out.println("cierra bloque.");
                    descifrarBloque(matrizTextoBits, matrizClaveBits, fw, cont);
                    matrizTextoBits = new int [8][8];
                    cont = 0;
                }
                binario.delete(0, binario.length());
            }
            int validar = 0;
            for(int i = 0; i<8; i++){
                for(int j = 0; j<8; j++){
                    validar += matrizTextoBits[i][j];
                }
            }
            if(validar != 0){
                descifrarBloque(matrizTextoBits, matrizClaveBits, fw, cont);
            }
            fw.write(System.lineSeparator());
            matrizTextoBits = new int [8][8];
            binario.delete(0, binario.length());
            cont = 0;
        }
        fw.close();
    }
    
    /**
     * Método que agrega los bits de una letra en la matriz del bloque
     * 
     * @param letraTextoBits
     * @param matrizTextoBits
     * @param fila
     * @return 
     */
    protected int[][] letraTextoEnMatriz(String[] letraTextoBits, int[][] matrizTextoBits, int fila){
        for(int i = 0; i<letraTextoBits.length; i++){
            matrizTextoBits[fila][i] = Integer.parseInt(letraTextoBits[i]);
        }
        return matrizTextoBits;
    }
    
    /**
     * Método que convoerte la clave en bits y los agrega a una matriz de 8x8
     * 
     * @param matrizClaveBits
     * @param clave
     * @return
     * @throws UnsupportedEncodingException 
     */
    protected int[][] claveEnBits (int[][] matrizClaveBits, String clave) throws UnsupportedEncodingException{
        byte[] lineaEnBytes = clave.getBytes("UTF-8");
        StringBuilder binario = new StringBuilder();
        int cont = 0;
        for(byte letra: lineaEnBytes){
            int val = letra;
            for (int i = 0; i < 8; i++)
            {
               binario.append((val & 128) == 0 ? 0 : 1);
               val <<= 1;
            }
           
            String[] letraClaveBits = binario.toString().split("");
            matrizClaveBits = letraTextoEnMatriz(letraClaveBits, matrizClaveBits, cont);
            cont++;
            binario.delete(0, binario.length());
        }
        return matrizClaveBits;
    }
    
    /**
     * Método que cifra la matriz de bits del texto claro con la matriz de bits de la clave
     * 
     * @param matrizTextoBits
     * @param matrizClaveBits
     * @param fw
     * @param cont
     * @throws IOException 
     */
    protected void cifrarBloque(int[][] matrizTextoBits, int[][] matrizClaveBits, FileWriter fw, int cont) throws IOException{
        
        int[][] matrizTextoCifradoBits = new int [8][8];
        
        //Realiza XOR con cada bit
        for(int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                matrizTextoCifradoBits[i][j] = matrizTextoBits[i][j] ^ matrizClaveBits[i][j];
            }
        }
        
        System.out.println("Matriz texto claro: ");
        for(int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                System.out.print(matrizTextoBits[i][j]+" ");
            }
            System.out.println();
        }
        
        System.out.println("Matriz clave: ");
        for(int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                System.out.print(matrizClaveBits[i][j]+" ");
            }
            System.out.println();
        }
        
        System.out.println("Matriz texto cifrado: ");
        for(int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                System.out.print(matrizTextoCifradoBits[i][j]+" ");
            }
            System.out.println();
        }
        
        //Escribe el texto cifrado en un archivo
        try {
            for(int i=0; i<8; i++){
                StringBuilder sb = new StringBuilder();
                for (int j=0; j<8; j++){
                    sb.append(Integer.toString(matrizTextoCifradoBits[i][j]));
                }
                
                System.out.println(sb.toString());
                System.out.println("Ascii: "+Integer.parseInt(sb.toString(), 2));
                
                StringBuffer sbf = new StringBuffer();
                
                if((Integer.parseInt(sb.toString(), 2)) == 10){
                    sbf.append((char)Integer.parseInt("254"));
                } else if ((Integer.parseInt(sb.toString(), 2)) == 13){
                    sbf.append((char)Integer.parseInt("253"));
                } else {
                    
                    for (int k = 0;k < sb.toString().length();k += 8) {
                        sbf.append(Character.toString((char) Integer.parseInt(sb.toString(), 2)));
                    }
                }
                System.out.println(sbf);

                fw.write(sbf.toString());
            }
        } catch (NumberFormatException e){
        }
    }
    
    /**
     * Método que descifra la matriz de bits del texto cifrado con la matriz de bits de la clave
     * 
     * @param matrizTextoBits
     * @param matrizClaveBits
     * @param fw
     * @param cont
     * @throws IOException 
     */
    protected void descifrarBloque(int[][] matrizTextoBits, int[][] matrizClaveBits, FileWriter fw, int cont) throws IOException{
        int[][] matrizTextoCifradoBits = new int[8][8];
        for(int i=0; i<8; i++){
            
            //Verifica que el valor ingresado sea un ascii 253 o 254 y se transforman a los bits originales
            StringBuilder sb = new StringBuilder();
            for (int j=0; j<8; j++){
                sb.append(Integer.toString(matrizTextoBits[i][j]));
            }
            
            if((Integer.parseInt(sb.toString(), 2)) == 254){
                for(int j=0; j<8; j++){
                    if(j==0 || j==1 || j==2 || j==3 || j==5 || j==7){
                        matrizTextoBits[i][j]=0;
                    } else {
                        matrizTextoBits[i][j]=1;
                    }
                }
            } else if((Integer.parseInt(sb.toString(), 2)) == 253){
                for(int j=0; j<8; j++){
                    if(j==0 || j==1 || j==2 || j==3 || j==6){
                        matrizTextoBits[i][j]=0;
                    } else {
                        matrizTextoBits[i][j]=1;
                    }
                }
            }
        }
        
        //Se realiza la operación XOR con lo bloques de bits
        for(int i=0; i<8; i++){
            
            for (int j=0; j<8; j++){
                matrizTextoCifradoBits[i][j] = matrizTextoBits[i][j] ^ matrizClaveBits[i][j];
            }
        }
        
        System.out.println("Matriz texto cifrado: ");
        for(int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                System.out.print(matrizTextoBits[i][j]+" ");
            }
            System.out.println();
        }
        
        System.out.println("Matriz clave: ");
        for(int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                System.out.print(matrizClaveBits[i][j]+" ");
            }
            System.out.println();
        }
        
        System.out.println("Matriz texto descifrado: ");
        for(int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                System.out.print(matrizTextoCifradoBits[i][j]+" ");
            }
            System.out.println();
        }
        
        //Escribe el texto descifrado en un archivo
        for(int i=0; i<8; i++){
            StringBuilder sb = new StringBuilder();
            for (int j=0; j<8; j++){
                sb.append(Integer.toString(matrizTextoCifradoBits[i][j]));
            }

            System.out.println(sb.toString());
            System.out.println("Ascii: "+Integer.parseInt(sb.toString(), 2));

            StringBuffer sbf = new StringBuffer();
            for (int k = 0;k < sb.toString().length();k += 8) {
                sbf.append(Character.toString((char) Integer.parseInt(sb.toString(), 2)));
            }
            System.out.println(sbf);
            
            fw.write(sbf.toString());
        }
    }
}
