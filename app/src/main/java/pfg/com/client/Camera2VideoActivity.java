package pfg.com.client;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by FPENG3 on 2019/4/16.
 */

public class Camera2VideoActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2VideoFragment.newInstance())
                    .commit();
        }
    }
}
