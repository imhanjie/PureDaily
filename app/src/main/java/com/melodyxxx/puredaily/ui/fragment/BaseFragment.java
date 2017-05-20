package com.melodyxxx.puredaily.ui.fragment;

import android.support.v4.app.Fragment;

import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.utils.PrefUtils;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/5/31.
 * Description:
 */
public class BaseFragment extends Fragment {

    protected String getCurrentUserName() {
        return PrefUtils.getString(getContext(), PrefConstants.USER_NAME, null);
    }

}
