package br.uefs.ecomp.util.exception;

public class DataOverlayException extends Exception {

    /**
     * Necessario para a serializacao
     */
    private static final long serialVersionUID = -4746027482244477395L;

    /**
     * Constrói uma exceção do tipo DataOverlayException com a mensagem passada como parâmetro
     *
     * @param error - Mensagem de erro
     */
    public DataOverlayException(String error) {
        super(error);
    }

}