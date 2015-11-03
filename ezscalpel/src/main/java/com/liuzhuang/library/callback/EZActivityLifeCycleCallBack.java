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
import android.view.ViewPropertyAnimator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;

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
    private ImageView switcherEntrance;
    private Switch switch3D;
    private Switch switchViewID;
    private View switchViewIdLayout;
    private Switch switchWireFrame;
    private View switchWireframeLayout;
    private View switcherParent;
    private Switch switchCustomBg;
    private float dX, dY;
    private float startX, startY;
    private ViewGroup container;
    private View rootView;
    private Activity mCurrentActivity;
    private View bgView;
    private PopupWindow popupWindow;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        EZLog.logi("CallBack_activity_intent", activity.toString());
        addRoot(activity);
    }

    private void addRoot(Activity activity) {
        reset();
        container = (ViewGroup)activity.findViewById(android.R.id.content);
        mCurrentActivity = activity;
        if (container == null) {
            EZLog.loge("addRoot", "container view is null");
            return;
        }
        rootView = container.getChildAt(0);
        if (rootView == null) {
            EZLog.loge("addRoot", "root view is null");
            return;
        }
        initScalpel();
        addEntrance();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        reset();
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    private void addEntrance() {
        if (container == null || mCurrentActivity == null) {
            return;
        }
        switcherEntrance = new ImageView(mCurrentActivity);
        final int DEFAULT_SPACE_Y = EZDeviceUtil.dp2px(mCurrentActivity, 50);
        switcherEntrance.setImageDrawable(mCurrentActivity.getResources().getDrawable(R.drawable.ic_3d_rotation_black_48dp));
        switcherEntrance.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                switcherEntrance.setX(EZDeviceUtil.getScreenWidth(mCurrentActivity) - switcherEntrance.getWidth());
                switcherEntrance.setY(EZDeviceUtil.getScreenHeight(mCurrentActivity) - EZDeviceUtil.getNavigationBarHeight(mCurrentActivity) - DEFAULT_SPACE_Y - switcherEntrance.getHeight());
            }
        });
        switcherEntrance.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean isMove = false;

                switch (event.getActionMasked()) {

                    case MotionEvent.ACTION_DOWN:
                        startX = switcherEntrance.getX();
                        startY = switcherEntrance.getY();
                        dX = switcherEntrance.getX() - event.getRawX();
                        dY = switcherEntrance.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:

                        switcherEntrance.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(startX - switcherEntrance.getX()) >= 10 || Math.abs(startY - switcherEntrance.getY()) >= 10) {
                            isMove = true;
                        } else {
                            return false;
                        }
                        ViewPropertyAnimator animator = switcherEntrance.animate();
                        if (switcherEntrance.getX() > (EZDeviceUtil.getScreenWidth(mCurrentActivity) - switcherEntrance.getWidth()) * 0.5) {
                            animator.x(EZDeviceUtil.getScreenWidth(mCurrentActivity) - switcherEntrance.getWidth());
                        } else {
                            animator.x(0);
                        }
                        if (switcherEntrance.getY() < DEFAULT_SPACE_Y) {
                            animator.y(DEFAULT_SPACE_Y);
                        } else if ((switcherEntrance.getY() + switcherEntrance.getHeight()) > (EZDeviceUtil.getScreenHeight(mCurrentActivity) - EZDeviceUtil.getNavigationBarHeight(mCurrentActivity) - DEFAULT_SPACE_Y)) {
                            animator.y(EZDeviceUtil.getScreenHeight(mCurrentActivity) - EZDeviceUtil.getNavigationBarHeight(mCurrentActivity) - DEFAULT_SPACE_Y - switcherEntrance.getHeight());
                        }
                        animator.setDuration(500).start();
                        break;
                    default:
                }
                return isMove;
            }
        });
        container.addView(switcherEntrance, new ViewGroup.LayoutParams(EZDeviceUtil.dp2px(mCurrentActivity, 48), EZDeviceUtil.dp2px(mCurrentActivity, 48)));
        switcherEntrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupWindow();
                popupWindow.showAtLocation(container, Gravity.CENTER, 0, 0);
            }
        });
    }

    private void initScalpel() {
        if (mCurrentActivity == null || container == null || rootView == null) {
            EZLog.logi("addScalpel", "add failed!");
            return;
        }
        if (rootView instanceof ScalpelFrameLayout) {
            EZLog.logi("addScalpel", "already added");
            return;
        }
        if (scalpelFrameLayout == null) {
            scalpelFrameLayout = new EZScalpelFrameLayout(mCurrentActivity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            scalpelFrameLayout.setLayoutParams(lp);
        }
        container.removeView(rootView);
        scalpelFrameLayout.removeView(rootView);
        scalpelFrameLayout.addView(rootView);
        container.removeView(scalpelFrameLayout);
        container.addView(scalpelFrameLayout);
    }

    private void removeScalpel() {
        if (mCurrentActivity == null || container == null || rootView == null) {
            EZLog.logi("removeScalpel", "remove failed!");
            return;
        }
        container.removeView(scalpelFrameLayout);
        scalpelFrameLayout.removeView(rootView);
        container.addView(rootView);
    }

    private void initPopupWindow() {
        initSwitcher(mCurrentActivity);
        if (popupWindow == null && switcherParent != null && mCurrentActivity != null) {
            popupWindow = new PopupWindow(switcherParent, (int) (EZDeviceUtil.getScreenWidth(mCurrentActivity) * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(mCurrentActivity.getResources().getDrawable(R.color.pop_up_window_bg_color));
        }
    }

    private void initSwitcher(Context context) {
        if (switcherParent == null) {
            switcherParent = LayoutInflater.from(context).inflate(R.layout.alert_switcher, null);
        }
        if (switch3D == null) {
            switch3D = (Switch) switcherParent.findViewById(R.id.switcher_3D);
            switch3D.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isScalpel3DChecked = isChecked;
                    displayViewIDAndWireFrame();
                    scalpelFrameLayout.setLayerInteractionEnabled(isChecked);
                }
            });
        }
        if (switchViewID == null) {
            switchViewID = (Switch) switcherParent.findViewById(R.id.switcher_view_id);
            switchViewID.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (scalpelFrameLayout != null) {
                        scalpelFrameLayout.setDrawIds(isChecked);
                    }
                }
            });
        }
        if (switchWireFrame == null) {
            switchWireFrame = (Switch) switcherParent.findViewById(R.id.switcher_wireframe);
            switchWireFrame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (scalpelFrameLayout != null) {
                        scalpelFrameLayout.setDrawViews(!isChecked);
                    }
                }
            });
        }
        if (switchCustomBg == null) {
            switchCustomBg = (Switch) switcherParent.findViewById(R.id.switcher_custom_bg);
            switchCustomBg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    addBackground(isChecked);
                }
            });
        }
        displayViewIDAndWireFrame();
    }

    private void displayViewIDAndWireFrame() {
        if (switchViewIdLayout == null) {
            switchViewIdLayout = switcherParent.findViewById(R.id.switcher_view_id_layout);
        }
        if (switchWireframeLayout == null) {
            switchWireframeLayout = switcherParent.findViewById(R.id.switcher_wireframe_layout);
        }
        if (!isScalpel3DChecked) {
            switchViewIdLayout.setVisibility(View.GONE);
            switchWireframeLayout.setVisibility(View.GONE);
        } else {
            switchViewIdLayout.setVisibility(View.VISIBLE);
            switchWireframeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void addBackground(boolean add) {
        if (container == null || mCurrentActivity == null) {
            return;
        }
        if (bgView == null) {
            bgView = new View(mCurrentActivity);
            bgView.setBackgroundColor(mCurrentActivity.getResources().getColor(R.color.main_bg_color));
        }
        container.removeView(bgView);
        if (add) {
            container.addView(bgView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    private void reset() {
        if (scalpelFrameLayout == null || mCurrentActivity == null || container == null || rootView == null) {
            return;
        }

        container.removeAllViews();
        scalpelFrameLayout.removeView(rootView);
        container.addView(rootView);
        isScalpel3DChecked = false;
        bgView = null;
        popupWindow = null;
        switcherEntrance = null;
        switch3D = null;
        switchViewID = null;
        switchWireFrame = null;
        switchWireframeLayout = null;
        switchViewIdLayout = null;
        switcherParent = null;
        scalpelFrameLayout = null;
        container = null;
        rootView = null;
        mCurrentActivity = null;
        switchCustomBg = null;
        EZLog.logi("reset", "reset_finish");
    }
}
