package tm.salam.gpstracker.helper;

import java.util.Objects;

public class CheckToken {

    public static boolean Check(String message, String date){

            if(Objects.equals("gps tracker success",message) && Objects.equals(date,"Tue Jun 07 15:20:21 TMT 2022")){

            return true;
        }else{
            return false;
        }
    }
}
