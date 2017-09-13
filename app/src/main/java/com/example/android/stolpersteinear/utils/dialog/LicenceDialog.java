package com.example.android.stolpersteinear.utils.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.stolpersteinear.R;

import agency.tango.materialintroscreen.SlideFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bjoern on 10.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: Creating the nice licence dialog.
 */

public class LicenceDialog extends SlideFragment{

    @BindView(R.id.licenceTextView)
    TextView mLicenceText;

    @SuppressWarnings("deprecation")
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.licence_dialog, container, false);
            ButterKnife.bind(this, view);
        // Cant reproduce the error oof the review hopefully this will work:
        // Thanks to: https://stackoverflow.com/questions/37904739/html-fromhtml-deprecated-in-android-n
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(getString(R.string.licenceslide_text), Html.FROM_HTML_MODE_LEGACY);
        } else {
            Html.fromHtml(getString(R.string.licenceslide_text));
        }
            return view;
        }

        @Override
        public int backgroundColor() {
            return R.color.slide_background;
        }

        @Override
        public int buttonsColor() {
            return R.color.slide_buttons;
        }

        @Override
        public String cantMoveFurtherErrorMessage() {
            return "Error";
        }

}

