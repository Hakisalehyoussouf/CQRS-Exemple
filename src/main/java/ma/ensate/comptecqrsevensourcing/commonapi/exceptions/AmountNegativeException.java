package ma.ensate.comptecqrsevensourcing.commonapi.exceptions;

public class AmountNegativeException extends RuntimeException {
    public AmountNegativeException(String message) {
        super(message);
    }
}
