package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

    public Logger(){
        simpleDateFormat.applyPattern("dd-M-yyyy hh:mm:ss");
    }

    public void writeToConsole(Object obj){
        System.out.println(simpleDateFormat.format(new Date()) + " LOGGER " +
                           obj.toString());
    }
}
