package nna.ca.ha.muteswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

public class MediaButtonReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = ("액션 발생! " + intent.getAction());
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

//        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
//            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
//            if (KeyEvent.KEYCODE_VOLUME_DOWN == event.getKeyCode()) {
//                // Handle key press.
//            }
    }
}
