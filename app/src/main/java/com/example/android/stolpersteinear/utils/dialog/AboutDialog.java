package com.example.android.stolpersteinear.utils.dialog;

import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.android.stolpersteinear.R;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

/**
 * Created by bjoern on 10.08.17.
 *
 * @author <a href="mailto:mail@bjoern.cologne">Bjoern Gam</a>
 * @link <a href="http://bjoern.cologne">Webpage </a>
 * <p>
 * Description: Creating the nice about slide in the app.
 */
public class AboutDialog extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.slide_background)
                        .buttonsColor(R.color.slide_buttons)
                        .image(R.drawable.stolpersteine)
                        .title(getString(R.string.about_dialog_title_first_slide))
                        .description(getString(R.string.about_dialog_description_first_slide))
                        .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.slide_background)
                .buttonsColor(R.color.slide_buttons)
                .image(R.drawable.osm_logo)
                .title(getString(R.string.about_dialog_title_second_slide))
                .description(getString(R.string.about_dialog_description_second_slide))
                .build());

        addSlide(new LicenceDialog());
    }
}

