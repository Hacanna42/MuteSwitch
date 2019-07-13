package nna.ca.ha.muteswitch;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.apply)
    Button apply;
    @BindView(R.id.resetting)
    Button resetting;
    @BindView(R.id.developer)
    TextView developer;
    @BindView(R.id.layout)
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        AudioManager audioManager = (AudioManager) getLayoutInflater().getContext().getSystemService(Context.AUDIO_SERVICE);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        SharedPreferences pref = getSharedPreferences("MuteSwitch", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //FIRST APP SETTING
        if (pref.getInt("first_setting", 0) == 0) {
            new MaterialDialog.Builder(this)
                    .content("이 앱은 빅스비 버튼을 아이폰의 무음 스위치 버튼처럼 바꿔주는 어플입니다.\n앱을 설정하신 뒤 빅스비 버튼을 스위치 처럼 무음 모드를 껐다 켰다 하실 수 있습니다.\n주의 : 앱에서 말하는 무음 모드란 완벽한 무음을 의미합니다.\n설정해놓은 알람도 울리지 않습니다.")
                    .positiveText("확인")
                    .canceledOnTouchOutside(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            firstsetting();
                        }
                    })
                    .show();
        } else {
            // 1 when not setted
            // 4 when none
            if (mNotificationManager.getCurrentInterruptionFilter() != NotificationManager.INTERRUPTION_FILTER_ALL) {
                try {
                    if (pref.getInt("old_ringer_mode",0)==1) {
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    }
                    mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                } catch (Exception e) {
                    Toast.makeText(this, "오류 발생! 앱을 다시 설치하신 뒤, 앱 권한 설정을 정확히 해주세요.", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                try {
                    vibrator.vibrate(100);
                    mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
                    if (audioManager.getRingerMode()==AudioManager.RINGER_MODE_VIBRATE) {
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        editor.putInt("old_ringer_mode", 1);
                        editor.apply();
                    }
                    else {
                        editor.putInt("old_ringer_mode", 0);
                        editor.apply();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "오류 발생! 앱을 다시 설치하신 뒤, 앱 권한 설정을 정확히 해주세요.", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

        }

    }

    public void firstsetting() {
        new MaterialDialog.Builder(this)
                .title("1 - 빅스비 버튼 설정")
                .content("다음 설정 화면에서\n\n[유용한 기능 - 빅스비 버튼 - 두번 눌러서 빅스비 열기에 체크 - 한 번 누르기 사용 - 앱 열기 - Mute Switch 클릭]\n\n을 완료해주세요. 이를 설정하지 않으면 앱이 동작하지 않습니다.")
                .positiveText("확인")
                .canceledOnTouchOutside(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                        setting2();
                    }
                })
                .show();
    }

    public void setting2() {
        new MaterialDialog.Builder(this)
                .title("2 - 방해 금지 설정")
                .content("다음 설정 화면에서 Mute Switch 앱의 권한을 켜주세요.\n핸드폰의 소리를 조절하기 위해 필요합니다.\n이를 설정하지 않으면 앱이 동작하지 않습니다.")
                .positiveText("확인")
                .canceledOnTouchOutside(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivity(intent);

                        layout.setVisibility(View.VISIBLE);
                    }
                })
                .show();
    }

    @OnClick(R.id.apply)
    public void apply() {
        SharedPreferences pref = getSharedPreferences("MuteSwitch", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("first_setting", 1);
        editor.apply();
        finish();
    }

    @OnClick(R.id.resetting)
    public void resetting() {
        firstsetting();
    }

}
