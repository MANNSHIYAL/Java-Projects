package commands;

public class InvalidCommand extends Exception implements Command {
    @Override
    public void execute() {
        System.out.println("Invalid command. Valid commands: /connect <peer>, /disconnect <peer>, /exit");
    }
    public InvalidCommand(){
        super("Invalid command. Valid commands: /connect <peer>, /disconnect <peer>, /exit");
    }
}
