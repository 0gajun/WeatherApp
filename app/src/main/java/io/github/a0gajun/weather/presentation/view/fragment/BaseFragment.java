/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather.presentation.view.fragment;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import io.github.a0gajun.weather.R;
import io.github.a0gajun.weather.presentation.di.HasComponent;

/**
 * Base Fragment class.
 *
 * Created by Junya Ogasawara on 1/9/17.
 */

public abstract class BaseFragment extends Fragment {

    protected void showSnackBar(final String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
