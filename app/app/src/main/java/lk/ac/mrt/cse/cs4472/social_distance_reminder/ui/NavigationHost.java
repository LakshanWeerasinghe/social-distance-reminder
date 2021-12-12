package lk.ac.mrt.cse.cs4472.social_distance_reminder.ui;

import androidx.fragment.app.Fragment;

/**
 *
 * display fragments and knows how to respond to navigation events
 *
 */
public interface NavigationHost {

    /**
     *
     * @param fragment fragment
     * @param addToBackStack optional field
     *
     * Trigger a navigation to the specified fragment, optionally adding a transaction to the back
     * stack to make this navigation reversible
     *
     */
    void navigateTo(Fragment fragment, boolean addToBackStack);
}
