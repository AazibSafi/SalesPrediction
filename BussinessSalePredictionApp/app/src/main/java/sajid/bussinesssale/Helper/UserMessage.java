package sajid.bussinesssale.Helper;

import android.widget.Toast;
import sajid.bussinesssale.Controller;

/**
 * Created by Aazib Safi Patoli on 8-jun-17.
 */

public class UserMessage {

    public UserMessage() { }

    public static void show(String msj) {
        Toast.makeText(Controller.activity,msj,Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String msj) {
        Toast.makeText(Controller.activity,msj,Toast.LENGTH_LONG).show();
    }
}