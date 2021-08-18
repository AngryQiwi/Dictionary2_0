package dictionary4;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DictionaryConfig.class);
        Program program = (Program) context.getBean("program");
        program.run();
    }
}