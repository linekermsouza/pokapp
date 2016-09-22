package br.com.uarini.pogapp.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import br.com.uarini.pogapp.R;

public class ViewRemoteService extends Service {

    private WindowManager windowManager;
    private ImageView chatHead;

    @Override public void onCreate() {
        super.onCreate();

        this.windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        this.chatHead = new ImageView(this);
        this.chatHead.setImageResource(R.drawable.ic_notification);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.BOTTOM | Gravity.LEFT;
        params.x = 0;
       // params.y = 100;

        final FloatingActionMenu actionMenu = new FloatingActionMenu(this);
        final FloatingActionButton actionButton = new FloatingActionButton(this);
        actionMenu.addMenuButton(actionButton);

        //actionMenu.toggleMenu(true);
        actionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("", "onClick");
            }
        });
        this.windowManager.addView(actionMenu, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.chatHead != null) this.windowManager.removeView(chatHead);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
