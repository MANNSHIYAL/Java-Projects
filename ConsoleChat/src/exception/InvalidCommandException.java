package exception;

public class InvalidCommandException {
    public InvalidCommandException() throws Exception{
        throw new Exception("This entered command is not valid!");
    }
}
