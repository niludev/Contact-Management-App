import config.ApplicationContext;
import db.DatabaseInitializer;
import view.ContactConsoleApp;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {

        boolean useDataBase = true;

        DatabaseInitializer.initialize();

        ApplicationContext ctx = ApplicationContext.getInstance();

        ContactConsoleApp app = new ContactConsoleApp(
                ctx.getContactServiceImpl(useDataBase)
        );

        app.run();
    }
}