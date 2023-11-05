import ThreadsClasses.ThInput;
import ThreadsClasses.ThOutput;

public class Client2 {
    public static void main(String[] args){

        ThInput rec_c1 = new ThInput();
        rec_c1.start();

        ThOutput env_c1 = new ThOutput();
        env_c1.start();
    }
}
