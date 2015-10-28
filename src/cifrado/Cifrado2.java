/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifrado;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 *
 * @author Andres
 */
public class Cifrado2 {
    public static void main(String args[]) throws FileNotFoundException, IOException{
        String clave = "Hola mundo";
        //String ubicacionArchivo = "C:\\Users\\Andres\\Dropbox\\Universidad\\Criptografia\\Proyecto_Final\\TextoClaro.txt";
        String ubicacionArchivo = "C:\\Users\\Andres\\Documents\\NetBeansProjects\\Cifrado\\TextoCifrado.txt";
        char cifrar = 'n';
        Cifrado2 c = new Cifrado2();
        if(cifrar == 's'){
            File f = new File("textoCifrado.txt");
            if(f.exists() && !f.isDirectory()) { 
                f.delete();
            }
            c.cifrar(clave, ubicacionArchivo);
        } else {
            c.descifrar(clave, ubicacionArchivo);
        }
    }
    
    public void cifrar(String clave, String ubicacionArchivo) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(ubicacionArchivo);
        BufferedReader br = new BufferedReader(fr);
        String lineaTexto;
        int cont = 0;
        int[][] matrizTextoBits = new int [8][8];
        int[][] matrizClaveBits = new int [clave.length()][8];
        
        matrizClaveBits = claveEnBits(matrizClaveBits, clave);
        
        while ((lineaTexto = br.readLine()) != null){
            byte[] lineaEnBytes = lineaTexto.getBytes("UTF-8");
            StringBuilder binario = new StringBuilder();
            for(byte letra: lineaEnBytes){
                int val = letra;
                for (int i = 0; i < 8; i++)
                {
                   binario.append((val & 128) == 0 ? 0 : 1);
                   val <<= 1;
                }
                //System.out.println("binario: ");
                //System.out.println(binario.toString());
                
                String[] letraTextoBits = binario.toString().split("");
                
                letraTextoEnMatriz(letraTextoBits, matrizTextoBits, cont);
                cont++;
                
                if (cont == 8){
                    System.out.println("cierra bloque.");
                    cifrarBloque(matrizTextoBits, matrizClaveBits);
                    matrizTextoBits = new int [8][8];
                    cont = 0;
                }
                binario.delete(0, binario.length());
            }
            
        }
    }
    
    public void descifrar(String clave, String ubicacionArchivo) throws FileNotFoundException, IOException{
        FileReader fr = new FileReader(ubicacionArchivo);
        BufferedReader br = new BufferedReader(fr);
        String lineaTexto;
        int cont = 0;
        int[][] matrizTextoBits = new int [8][8];
        int[][] matrizClaveBits = new int [clave.length()][8];
        
        matrizClaveBits = claveEnBits(matrizClaveBits, clave);
        
        while ((lineaTexto = br.readLine()) != null){
            byte[] lineaEnBytes = lineaTexto.getBytes(StandardCharsets.UTF_8);
            System.out.println("LineaEnBytes"+Arrays.toString(lineaEnBytes));
            StringBuilder binario = new StringBuilder();
            for(byte letra: lineaEnBytes){
                int val = letra;
                for (int i = 0; i < 8; i++)
                {
                   binario.append((val & 128) == 0 ? 0 : 1);
                   val <<= 1;
                }
                System.out.println("binario: ");
                System.out.println(binario.toString());
                
                String[] letraTextoBits = binario.toString().split("");
                
                letraTextoEnMatriz(letraTextoBits, matrizTextoBits, cont);
                cont++;
                
                if (cont == 8){
                    //System.out.println("cierra bloque.");
                    descifrarBloque(matrizTextoBits, matrizClaveBits);
                    matrizTextoBits = new int [8][8];
                    cont = 0;
                }
                binario.delete(0, binario.length());
            }
            
        }
    }
    
    public int[][] letraTextoEnMatriz(String[] letraTextoBits, int[][] matrizTextoBits, int fila){
        for(int i = 0; i<letraTextoBits.length; i++){
            matrizTextoBits[fila][i] = Integer.parseInt(letraTextoBits[i]);
        }
        
//        System.out.println("Texto en bits: ");
//        for(int i = 0; i<8; i++){
//            for(int j= 0; j<8; j++){
//                System.out.print(matrizTextoBits[i][j]+" ");
//            }
//            System.out.println();
//        }
        return matrizTextoBits;
    }
    
    public int[][] claveEnBits (int[][] matrizClaveBits, String clave) throws UnsupportedEncodingException{
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
            //System.out.println("binario: ");
            //System.out.println(binario.toString());

            String[] letraClaveBits = binario.toString().split("");
            matrizClaveBits = letraTextoEnMatriz(letraClaveBits, matrizClaveBits, cont);
            cont++;
            binario.delete(0, binario.length());
        }
        return matrizClaveBits;
    }
    
    public void cifrarBloque(int[][] matrizTextoBits, int[][] matrizClaveBits) throws IOException{
        
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
        int[] letraCifradaBits = new int[8];
        FileWriter fw = new FileWriter("textoCifrado.txt", true);
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            for(int i=0; i<8; i++){
                StringBuilder sb = new StringBuilder();
                for (int j=0; j<8; j++){
                    //letraCifradaBits[j] = matrizTextoCifradoBits[i][j];
                    sb.append(Integer.toString(matrizTextoCifradoBits[i][j]));
                }
                
                System.out.println(sb.toString());
                System.out.println("Ascii: "+Integer.parseInt(sb.toString(), 2));
                
                StringBuffer sbf = new StringBuffer();
                for (int k = 0;k < sb.toString().length();k += 8) {
                    sbf.append(Character.toString((char) Integer.parseInt(sb.toString(), 2)));
                }
                System.out.println(sbf);
                
                
                //byte[] letraCifradaByte = int2byte(letraCifradaBits);
//                for (int j=0; j<letraCifradaByte.length; j++){
//                    System.out.print(letraCifradaByte[j]);
//                }
//                System.out.println();
                //System.out.println(new String(letraCifradaByte, "UTF-8"));
                bw.write(sbf.toString());
                //System.out.print(sb.toString());
                //System.out.println();
            }
        }
    }
    
    public void descifrarBloque(int[][] matrizTextoBits, int[][] matrizClaveBits) throws IOException{
        int[][] matrizTextoCifradoBits = new int[8][8];
        for(int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                matrizTextoCifradoBits[i][j] = matrizTextoBits[i][j] ^ matrizClaveBits[i][j];
            }
        }
        
        //Escribe el texto descifrado en un archivo
        char[] letraCifradaBits = new char[8];
        FileWriter fw = new FileWriter("textoDescifrado.txt", true);
        
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            for(int i=0; i<8; i++){
                for (int j=0; j<8; j++){
                    letraCifradaBits[j] = Character.forDigit(matrizTextoCifradoBits[i][j], 2) ;
                }
                StringBuilder sb = new StringBuilder();
                String letraCifrada = new String(letraCifradaBits);
                sb.append((char)Integer.parseUnsignedInt(letraCifrada, 2));
                bw.write(sb.toString());
                //System.out.print(sb.toString());
                //System.out.println();
            }
        }
    }
    
    public static byte[] int2byte(int[] src) {
        byte[] dst = new byte[8];

        for (int i=0; i<src.length; i++) {
            dst[i] = (byte) (src[i] & (0xff));           
        }
        return dst;
    }
}
