package br.uefs.ecomp.util.exception;

public class IncorrectFormatException extends Exception {

    /**
     * Necessario para a serializacao
     */
    private static final long serialVersionUID = -5742240435414446236L;

    /**
     * Constrói uma exceção do tipo IncorrectFormatException indicando qual o campo dos dados inválido
     *
     * @param e - Campo inválido
     */
    public IncorrectFormatException(String e) {
        super("O campo " + e + " não constitui um argumento válido!");
    }

    /**
     * Constrói uma exceção do tipo IncorrectFormatException indicando qual a linha do arquivo que possui campos dos dados inválido
     *
     * @param lineNumber - Linha do arquivo onde foi encontrado o erro
     */
    public IncorrectFormatException(int lineNumber) {
        super("Na linha " + lineNumber + " do arquivo de entrada possui dado(s) inválido(s)!");
    }

    /**
     * Constrói uma exceção do tipo IncorrectFormatException
     */
    public IncorrectFormatException() {
        super("Argumento válido!");
    }

}