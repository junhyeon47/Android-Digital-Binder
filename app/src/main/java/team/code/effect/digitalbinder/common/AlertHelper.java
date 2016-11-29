package team.code.effect.digitalbinder.common;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

public class AlertHelper {
    public static AlertDialog.Builder getAlertDialog(Activity activity, String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setMessage(message);
        return alert;
    }
}
