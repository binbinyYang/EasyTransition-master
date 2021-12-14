package com.hzn.lib;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;


public class BinTransition {

    public static final String EASY_TRANSITION_OPTIONS = "easy_transition_options";
    public static final long DEFAULT_TRANSITION_ANIM_DURATION = 1000;

    /**
     * 开始的时候
     */
    public static void startActivity(Intent intent, BinTransitionOptions options) {
        options.update();
        intent.putParcelableArrayListExtra(EASY_TRANSITION_OPTIONS, options.getAttrs());
        Activity activity = options.getActivity();
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    /**
     * 带结果返回的开始
     */
    public static void startActivityForResult(Intent intent, int requestCode, BinTransitionOptions options) {
        options.update();
        intent.putParcelableArrayListExtra(EASY_TRANSITION_OPTIONS, options.getAttrs());
        Activity activity = options.getActivity();
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(0, 0);
    }

    /**
     * 调用此方法开始输入转换动画
     *
     * @param activity     The Activity entering
     * @param duration     The duration of enter transition animation
     * @param interpolator The TimeInterpolator of enter transition animation
     * @param listener     Animator listener, normally you can do your initial after animation end
     */
    public static void enter(Activity activity, long duration, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        Intent intent = activity.getIntent();
        ArrayList<BinTransitionOptions.ViewAttrs> attrs =
                intent.getParcelableArrayListExtra(EASY_TRANSITION_OPTIONS);
        runEnterAnimation(activity, attrs, duration, interpolator, listener);
    }


    public static void enter(Activity activity, long duration, Animator.AnimatorListener listener) {
        enter(activity, duration, null, listener);
    }


    public static void enter(Activity activity, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        enter(activity, DEFAULT_TRANSITION_ANIM_DURATION, interpolator, listener);
    }


    public static void enter(Activity activity, Animator.AnimatorListener listener) {
        enter(activity, DEFAULT_TRANSITION_ANIM_DURATION, null, listener);
    }


    public static void enter(Activity activity) {
        enter(activity, DEFAULT_TRANSITION_ANIM_DURATION, null, null);
    }

    private static void runEnterAnimation(Activity activity,
                                          ArrayList<BinTransitionOptions.ViewAttrs> attrs,
                                          final long duration,
                                          final TimeInterpolator interpolator,
                                          final Animator.AnimatorListener listener) {
        if (null == attrs || attrs.size() == 0)
            return;

        for (final BinTransitionOptions.ViewAttrs attr : attrs) {
            final View view = activity.findViewById(attr.id);

            if (null == view)
                continue;

            view.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            view.getViewTreeObserver().removeOnPreDrawListener(this);

                            int[] location = new int[2];
                            view.getLocationOnScreen(location);
                            view.setPivotX(0);
                            view.setPivotY(0);
                            view.setScaleX(attr.width / view.getWidth());
                            view.setScaleY(attr.height / view.getHeight());
                            view.setTranslationX(attr.startX - location[0]); // xDelta
                            view.setTranslationY(attr.startY - location[1]); // yDelta

                            view.animate()
                                    .scaleX(1)
                                    .scaleY(1)
                                    .translationX(0)
                                    .translationY(0)
                                    .setDuration(duration)
                                    .setInterpolator(interpolator)
                                    .setListener(listener);
                            return true;
                        }
                    });
        }
    }

    /**
     * 退出活动，调用此方法以启动退出转换动画
     */
    public static void exit(Activity activity, long duration, TimeInterpolator interpolator) {
        Intent intent = activity.getIntent();
        ArrayList<BinTransitionOptions.ViewAttrs> attrs = intent.getParcelableArrayListExtra(EASY_TRANSITION_OPTIONS);
        runExitAnimation(activity, attrs, duration, interpolator);
    }


    public static void exit(Activity activity, TimeInterpolator interpolator) {
        exit(activity, DEFAULT_TRANSITION_ANIM_DURATION, interpolator);
    }


    public static void exit(Activity activity, long duration) {
        exit(activity, duration, null);
    }



    private static void runExitAnimation(final Activity activity,
                                         ArrayList<BinTransitionOptions.ViewAttrs> attrs,
                                         long duration,
                                         TimeInterpolator interpolator) {
        if (null == attrs || attrs.size() == 0)
            return;

        for (final BinTransitionOptions.ViewAttrs attr : attrs) {
            View view = activity.findViewById(attr.id);
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            view.setPivotX(0);
            view.setPivotY(0);

            view.animate()
                    .scaleX(attr.width / view.getWidth())
                    .scaleY(attr.height / view.getHeight())
                    .translationX(attr.startX - location[0])
                    .translationY(attr.startY - location[1])
                    .setInterpolator(interpolator)
                    .setDuration(duration);
        }

        activity.findViewById(attrs.get(0).id).postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        }, duration);
    }
}
