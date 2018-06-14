package com.kslau.nexus.mydeliveries.UI.DeliveryDetail;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.kslau.nexus.mydeliveries.R;
import com.squareup.picasso.Picasso;

public class DeliveryDetailBindings {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(view);
    }
}
