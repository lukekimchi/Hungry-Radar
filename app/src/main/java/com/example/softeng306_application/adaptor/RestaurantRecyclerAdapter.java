package com.example.softeng306_application.adaptor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softeng306_application.entity.Restaurant;
import com.example.softeng306_application.R;
import com.example.softeng306_application.view.DetailsActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class RestaurantRecyclerAdapter extends RecyclerView.Adapter<RestaurantRecyclerAdapter.RestaurantViewHolder> {
    Context context;
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Restaurant> favouriteList = new ArrayList<>();

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        notifyDataSetChanged();
    }

    public void setFavouriteRestaurants(List<Restaurant> restaurants) {
        this.favouriteList = restaurants;
        notifyDataSetChanged();
    }
    public RestaurantRecyclerAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RestaurantViewHolder(LayoutInflater.from(context).inflate(R.layout.restaurant_list_item, parent, false));
    }
    @Override
    public void onBindViewHolder(RestaurantRecyclerAdapter.RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);

        holder.restaurantName.setText(restaurant.getName());
        holder.price.setText(restaurant.getPrice());
        holder.logoImage.setImageResource(showImage(restaurant));

        // Set colours according to category
        String colourHex = restaurant.getCategory().getBorderColour();
        holder.logoImageCardView.setStrokeColor(Color.parseColor(colourHex));
        holder.categoryLabelCard.setCardBackgroundColor(Color.parseColor(colourHex));
        holder.categoryLabelText.setText(restaurant.getCategory().getCategoryType());

        if (favouriteList.contains(restaurant)){
            holder.favouriteHeart.setVisibility(View.VISIBLE);
        } else {
            holder.favouriteHeart.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRestaurantClick(restaurant);
            }
        });
    }

    private int showImage(Restaurant restaurant) {
        int i = context.getResources().getIdentifier(restaurant.getLogoImage(), "drawable", context.getPackageName());
        return i;
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    private void onRestaurantClick(Restaurant restaurant){
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("RESTAURANT", restaurant);
        context.startActivity(intent);
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder{
        ImageView favouriteHeart, logoImage;
        MaterialCardView logoImageCardView;
        TextView restaurantName, price, categoryLabelText;
        MaterialCardView categoryLabelCard;
        RelativeLayout header;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            logoImage = itemView.findViewById(R.id.img_logo);
            logoImageCardView = itemView.findViewById(R.id.img_logo_card);
            restaurantName = itemView.findViewById(R.id.txt_restaurant_name);
            price = itemView.findViewById(R.id.txt_price);
            favouriteHeart = itemView.findViewById(R.id.img_favourite);
            categoryLabelCard = itemView.findViewById(R.id.mtrlcardview_categoryLabel);
            categoryLabelText = itemView.findViewById(R.id.txt_categoryLabel);
        }

    }
}
