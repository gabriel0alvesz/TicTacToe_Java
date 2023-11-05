package Classes;
import java.util.Random;
public class Tools {

    public static void shuffle(Pacote c1pack, Pacote c2pack) {
        Random random = new Random();
        int i = random.nextInt(2);

        if(i == 1){
            c1pack.alerta = EnumMSG.VOCE_COMECA;
            c1pack.simbolo = 1;

            c2pack.alerta = EnumMSG.ESPERE_SUA_VEZ;
            c2pack.simbolo = -1;
        }else{
            c1pack.alerta = EnumMSG.ESPERE_SUA_VEZ;
            c1pack.simbolo = -1;

            c2pack.alerta = EnumMSG.VOCE_COMECA;
            c2pack.simbolo = 1;
        }
    }
}
