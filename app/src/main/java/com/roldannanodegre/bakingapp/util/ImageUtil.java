package com.roldannanodegre.bakingapp.util;

import android.view.View;

import com.roldannanodegre.bakingapp.R;
import com.roldannanodegre.bakingapp.databinding.ImageComponentBinding;
import com.squareup.picasso.Picasso;

public class ImageUtil {

    private ImageUtil() { }

    public static void setImage(final ImageComponentBinding imageComponentBinding, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageComponentBinding.pbDownloadProgress.setVisibility(View.GONE);
            imageComponentBinding.ivImage.setImageResource(R.drawable.ic_recipe);
        } else {
            imageComponentBinding.pbDownloadProgress.setVisibility(View.VISIBLE);
            loadImage(imageComponentBinding, imageUrl);
        }
    }

    private static void loadImage(final ImageComponentBinding imageComponentBinding, String imageUrl) {
        Picasso.with(imageComponentBinding.ivImage.getContext()).load(imageUrl)
                .into(imageComponentBinding.ivImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        imageComponentBinding.pbDownloadProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        imageComponentBinding.pbDownloadProgress.setVisibility(View.GONE);
                        imageComponentBinding.ivImage.setImageResource(R.drawable.ic_recipe);
                    }
                });
    }

}
