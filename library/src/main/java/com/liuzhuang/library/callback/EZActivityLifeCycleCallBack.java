package com.liuzhuang.library.callback;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.liuzhuang.library.R;
import com.liuzhuang.library.log.EZLog;
import com.liuzhuang.library.utils.EZDeviceUtil;
import com.liuzhuang.library.view.EZScalpelFrameLayout;

/**
 * activity 生命周期回调
 * Created by liuzhuang on 10/29/15.
 */
public class EZActivityLifeCycleCallBack implements Application.ActivityLifecycleCallbacks {
    private ScalpelFrameLayout scalpelFrameLayout;
    private boolean isScalpel3DChecked = false;
    private boolean isViewIdChecked = false;
    private boolean isWireframeChecked = false;
    private Switch switch3D;
    private Switch switchViewID;
    private TextView switchViewIdTv;
    private Switch switchWireFrame;
    private TextView switchWireframeTv;
    private View switcherParent;
    private float dX, dY;
    private float startX, startY;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        final String param = null == activity.getIntent() ? null : activity.getIntent().getDataString();
        EZLog.logi("CallBack_activity_intent", param);
        addRoot(activity);
    }

    private void addRoot(Activity activity) {
        ViewGroup content = (ViewGroup)activity.findViewById(android.R.id.content);
        if (content == null) {
            EZLog.loge("addRoot", "content view is null");
            return;
        }
        View rootView = content.getChildAt(0);
        if (rootView == null) {
            EZLog.loge("addRoot", "root view is null");
            return;
        }
        if (rootView instanceof ScalpelFrameLayout) {
            EZLog.logi("addRoot", "already added");
            return;
        }
        scalpelFrameLayout = new EZScalpelFrameLayout(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scalpelFrameLayout.setLayoutParams(lp);
        content.removeView(rootView);
        scalpelFrameLayout.addView(rootView);
        content.addView(scalpelFrameLayout);
        addEntrance(content, activity);
    }
    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        reset();
    }

    private void addEntrance(final ViewGroup parent, final Context context) {
        final ImageView switcher = new ImageView(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switcher.setLayoutParams(lp);
        switcher.setImageResource(R.drawable.ic_3d_rotation_black_48dp);
        switcher.setX(0);
        switcher.setY(EZDeviceUtil.getScreenHeight(context) - 200);
        switcher.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isMove = false;

                switch (event.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN:
                        startX = switcher.getX();
                        startY = switcher.getY();
                        dX = switcher.getX() - event.getRawX();
                        dY = switcher.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        switcher.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (startX != switcher.getX() || startY != switcher.getY()) {
                            isMove = true;
                        }
                        if (switcher.getX() > (EZDeviceUtil.getScreenWidth(context) - switcher.getWidth()) * 0.5) {
                            switcher.animate()
                                    .x(EZDeviceUtil.getScreenWidth(context) - switcher.getWidth())
                                    .setDuration(500)
                                    .start();
                        } else {
                            switcher.animate()
                                    .x(0)
                                    .setDuration(500)
                                    .start();
                        }
                        break;
                    default:
                }
                return isMove;
            }
        });
        parent.addView(switcher);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSwitcher(context);

                switch3D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (scalpelFrameLayout != null) {
                            isScalpel3DChecked = isChecked;
                            displayViewIDAndWireFrame();
                            scalpelFrameLayout.setLayerInteractionEnabled(isChecked);
                        }
                    }
                });

                switchViewID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (scalpelFrameLayout != null) {
                            isViewIdChecked = isChecked;
                            scalpelFrameLayout.setDrawIds(isChecked);
                        }
                    }
                });

                switchWireFrame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (scalpelFrameLayout != null) {
                            isWireframeChecked = isChecked;
                            scalpelFrameLayout.setDrawViews(!isChecked);
                        }
                    }
                });
                PopupWindow popupWindow = new PopupWindow(switcherParent, (int) (EZDeviceUtil.getScreenWidth(context) * 0.6), ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.color.pop_up_window_bg_color));
                popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
            }
        });
    }

    private void initSwitcher(Context context) {
        if (switcherParent == null) {
            switcherParent = LayoutInflater.from(context).inflate(R.layout.alert_switcher, null);
        }
        if (switch3D == null) {
            switch3D = (Switch) switcherParent.findViewById(R.id.switcher_3D);
        }
        if (switchViewID == null) {
            switchViewID = (Switch) switcherParent.findViewById(R.id.switcher_view_id);
            switchViewIdTv = (TextView) switcherParent.findViewById(R.id.view_id_tv);
        }
        if (switchWireFrame == null) {
            switchWireFrame = (Switch) switcherParent.findViewById(R.id.switcher_wireframe);
            switchWireframeTv = (TextView) switcherParent.findViewById(R.id.switcher_wireframe_tv);
        }
        switch3D.setChecked(isScalpel3DChecked);
        switchViewID.setChecked(isViewIdChecked);
        switchWireFrame.setChecked(isWireframeChecked);
        displayViewIDAndWireFrame();
    }

    private void displayViewIDAndWireFrame() {
        if (!isScalpel3DChecked) {
            switchViewID.setVisibility(View.GONE);
            switchViewIdTv.setVisibility(View.GONE);
            switchWireFrame.setVisibility(View.GONE);
            switchWireframeTv.setVisibility(View.GONE);
        } else {
            switchViewID.setVisibility(View.VISIBLE);
            switchViewIdTv.setVisibility(View.VISIBLE);
            switchWireFrame.setVisibility(View.VISIBLE);
            switchWireframeTv.setVisibility(View.VISIBLE);
        }
    }

    private void reset() {
        isScalpel3DChecked = false;
        isViewIdChecked = false;
        isWireframeChecked = false;
        switch3D = null;
        switchViewID = null;
        switchWireFrame = null;
        switchWireframeTv = null;
        switchViewIdTv = null;
        switcherParent = null;
        EZLog.logi("reset", "reset_finish");
    }
}
