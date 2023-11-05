import ThreadsClasses.ThInput;
import ThreadsClasses.ThOutput;

public class Client1 {
    public static void main(String[] args){

        ThInput rec_c2 = new ThInput();
        rec_c2.start();

        ThOutput env_c2 = new ThOutput();
        env_c2.start();
    }
}
