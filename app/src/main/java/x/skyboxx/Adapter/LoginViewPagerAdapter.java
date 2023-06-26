package x.skyboxx.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import x.skyboxx.Fragment.SignInFragment;
import x.skyboxx.Fragment.SignUpFragment;

public class LoginViewPagerAdapter extends FragmentPagerAdapter {
    public LoginViewPagerAdapter(FragmentManager fm) {
        super( fm );
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        if (position == 0) {

            fragment = new SignInFragment();

        } else if (position == 1) {

            fragment = new SignUpFragment();

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Sign in";
        } else if (position == 1) {
            title = "Sign Up";
        }
        return title;
    }
}
