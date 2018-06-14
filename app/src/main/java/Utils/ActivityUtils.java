package Utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.Fade;
import android.support.transition.TransitionInflater;
import android.support.transition.TransitionSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActivityUtils {

    private static final long MOVE_DEFAULT_TIME = 1000;
    private static final long FADE_DEFAULT_TIME = 300;

    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void replaceFragmentTransitionInActivity(@NonNull FragmentManager fragmentManager,
                                          @NonNull Fragment previousFragment,
                                          @NonNull Fragment nextFragment,
                                          int frameId,
                                          View viewToTransition,
                                          Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            replaceFragmentInActivity(fragmentManager, nextFragment, frameId);
            return;

        } else {
            checkNotNull(fragmentManager);
            checkNotNull(previousFragment);
            checkNotNull(nextFragment);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if (isDestroyed())
//        {
//            return;
//        }
//        Fragment previousFragment = mFragmentManager.findFragmentById(R.id.fragment_container);
//        Fragment nextFragment = Fragment2.newInstance();
//
//        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

            // 1. Exit for Previous Fragment
            Fade exitFade = new Fade();
            exitFade.setDuration(FADE_DEFAULT_TIME);
            previousFragment.setExitTransition(exitFade);

            // 2. Shared Elements Transition
            TransitionSet enterTransitionSet = new TransitionSet();
            enterTransitionSet.addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move));

            enterTransitionSet.setDuration(MOVE_DEFAULT_TIME);
            enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME);
            nextFragment.setSharedElementEnterTransition(enterTransitionSet);

            // 3. Enter Transition for New Fragment
            Fade enterFade = new Fade();
            enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME);
            enterFade.setDuration(FADE_DEFAULT_TIME);
            nextFragment.setEnterTransition(enterFade);

            transaction.addSharedElement(viewToTransition, viewToTransition.getTransitionName());
            transaction.replace(frameId, nextFragment);
            transaction.commitAllowingStateLoss();
        }
    }
}
