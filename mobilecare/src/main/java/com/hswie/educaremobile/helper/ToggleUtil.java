//package com.hswie.educaremobile.helper;
//
//import android.content.Context;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//
//import com.hswie.educaremobile.R;
//
///**
// * Created by hswie on 12/17/2015.
// */
//public class ToggleUtil {
//
//    public static void toggleContentsUp(Context context, View layout){
//
//        if(layout.isShown())
//            slideUp(context, layout);
//    }
//
//    public static void toggleContentsDown(Context context, View layout){
//
//        if(!layout.isShown()) {
//            layout.setVisibility(View.VISIBLE);
//            slideDown(context, layout);
//        }
//    }
//
//
//    public static void toggleContents(Context context, View layout) {
//
//        if (layout.isShown()) {
//            slideUp(context, layout);
//
//        } else {
//            layout.setVisibility(View.VISIBLE);
//            slideDown(context, layout);
//        }
//    }
//
//    private static void slideDown(Context ctx, View v) {
//
//        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
//        if (a != null) {
//            a.reset();
//            if (v != null) {
//                v.clearAnimation();
//                v.startAnimation(a);
//            }
//        }
//    }
//
//    private static void slideUp(Context ctx, final View v) {
//
//        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
//        a.setAnimationListener(new Animation.AnimationListener() {
//
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                v.setVisibility(View.GONE);
//            }
//        });
//        if (a != null) {
//            a.reset();
//            if (v != null) {
//                v.clearAnimation();
//                v.startAnimation(a);
//            }
//        }
//    }
//}
