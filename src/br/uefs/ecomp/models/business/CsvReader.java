package br.uefs.ecomp.models.business;

import br.uefs.ecomp.util.exception.IncorrectFormatException;
import br.uefs.ecomp.util.exception.InputInformationIncorrectException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class CsvReader {

    private LinkedList<String[]> items;

    public CsvReader() {
        this.items = new LinkedList<>();
    }

    public String[] readInputFile(String fileName) throws IOException {
        StringBuilder returnString = new StringBuilder(); //String para armazenar erros encontrados
        int lineNumber = 1; //Contador de linhas do arquivo
        FileReader readingFile = null;
        BufferedReader readingNow = null;
        try {
            readingFile = new FileReader(fileName); //Tenta abrir o arquivo
            readingNow = new BufferedReader(readingFile); //Buffer para realizar a leitura

            String lineReaded = null;
            while ((lineReaded = readingNow.readLine()) != null) { //Lê cada linha do arquivo até chegar no final
                try {
                    registerItem(lineReaded); //Verifica a validade dos dados e tenta registrar o item
                } catch (InputInformationIncorrectException e) { //Erro na quantidade de dados passados
                    returnString.append(new InputInformationIncorrectException(lineNumber)).append(";");
                } catch (IncorrectFormatException e) { //Erro no formato dos dados de entrada
                    returnString.append(new IncorrectFormatException(lineNumber)).append(";");
                } finally {
                    lineNumber++;
                }
            }
        } catch (IOException exceptionFound) { //Erro na abertura do arquivo
            returnString.append(exceptionFound.getMessage());
            throw exceptionFound;
        } finally { //Fecha o arquivo
            if (readingFile != null) {
                readingFile.close();
                if (readingNow != null)
                    readingNow.close();
            }
        }
        return returnString.toString().split(";"); //Retorna um vetor de String com os erros encontrados
    }

    private void registerItem(String attributes) throws InputInformationIncorrectException, IncorrectFormatException {
        String[] splitedString = attributes.split(";"); //Quebra a String recebida para separar os dados
        if (splitedString.length != 3) { //Verifica se a quantidade de dados está correta
            throw new InputInformationIncorrectException();
        }
        if (!splitedString[0].matches("[0-9]+")) { //Verifica se o id do item contém o formato certo
            throw new IncorrectFormatException(splitedString[0]);
        }
        if (!splitedString[2].matches("^[ ]?\\d+$")) { //Verifica se a quantidade do item contém o formato certo
            throw new IncorrectFormatException(splitedString[2]);
        }
        splitedString[2] = splitedString[2].replace(" ", "");
        this.items.add(splitedString);
    }

    public LinkedList<String[]> getItems() {
        return this.items;
    }
}
