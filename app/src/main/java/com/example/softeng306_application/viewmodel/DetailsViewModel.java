package com.example.softeng306_application.viewmodel;

import android.app.Application;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.softeng306_application.entity.Restaurant;
import com.example.softeng306_application.entity.Review;
import com.example.softeng306_application.usecase.AddFavouriteUseCase;
import com.example.softeng306_application.usecase.AddReviewUseCase;
import com.example.softeng306_application.usecase.CheckFavouriteUseCase;
import com.example.softeng306_application.usecase.GetCurrentUserUseCase;
import com.example.softeng306_application.usecase.GetReviewUseCase;
import com.example.softeng306_application.usecase.RemoveFavouriteUseCase;

import java.util.List;

public class DetailsViewModel extends AndroidViewModel {
    private GetReviewUseCase getReviewUseCase;
    private GetCurrentUserUseCase getCurrentUserUseCase;
    private CheckFavouriteUseCase checkFavouriteUseCase;
    private AddReviewUseCase addReviewUseCase;
    private RemoveFavouriteUseCase removeFavouriteUseCase;
    private AddFavouriteUseCase addFavouriteUseCase;
    private MutableLiveData<Integer> averageScore =  new MutableLiveData<>();

    private MutableLiveData<List<Restaurant>> favouritesList =  new MutableLiveData<>();
    private MutableLiveData<List<Review>> reviewsCurrentList =  new MutableLiveData<>();
    private Boolean isFavourite;
    private Restaurant restaurant;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        checkFavouriteUseCase = checkFavouriteUseCase.getInstance();
        addFavouriteUseCase = addFavouriteUseCase.getInstance();
        removeFavouriteUseCase = removeFavouriteUseCase.getInstance();
        addReviewUseCase = addReviewUseCase.getInstance();
        getReviewUseCase = getReviewUseCase.getInstance();
        getCurrentUserUseCase = getCurrentUserUseCase.getInstance();
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public LiveData<List<Review>> getReviewsList(String restaurantID) {
        return getReviewUseCase.getReviewUse(restaurantID);
    }

    public void setIsFavourite(Boolean value) {
        this.isFavourite = value;
    }

    public Boolean isFavourite() {
        return isFavourite;
    }

    public LiveData<Boolean> checkFavourite(Restaurant restaurant) {
        return checkFavouriteUseCase.checkFavourite(restaurant);
    }
    public LiveData<String> getUsername(){
        return getCurrentUserUseCase.getUsername();
    }
    public LiveData<List<Review>> getReviewsCurrentList() {
        return this.reviewsCurrentList;
    }
    public void updateCurrentList(List<Review> reviewList) {
        this.reviewsCurrentList.setValue(reviewList);
    }
    public void addToReviewsList(String reviewComment, String name, float reviewScore) {
        Review review = new Review(name,reviewComment,reviewScore);
        List<Review> currentReviews = this.reviewsCurrentList.getValue();
        currentReviews.add(review);
        updateCurrentList(currentReviews);
        calculateAverageReviewScoreFromList(currentReviews);
    }

    /**
     * This method would locally get the three background images for each restaurant
     * @param context
     * @return an array of background images
     */
    public int[] getBackgroundImages(Context context) {
        Restaurant currentRestaurant = restaurant;
        String id = currentRestaurant.getRestaurantID();
        int restaurantId = Integer.parseInt(id) - 1;
        String resId = String.valueOf(restaurantId);
        int[] images = new int[3];
        images[0] = context.getResources().getIdentifier("back" + resId + "_1", "drawable", context.getPackageName());
        images[1] = context.getResources().getIdentifier("back" + resId + "_2", "drawable", context.getPackageName());
        images[2] = context.getResources().getIdentifier("back" + resId + "_3", "drawable", context.getPackageName());
        return images;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void addFavourite() {
        addFavouriteUseCase.addFavourite(this.restaurant);
    }

    public void removeFavourite() {
        removeFavouriteUseCase.removeFavourite(this.restaurant);
    }

    /**
     * This method would return the corresponding id of every restaurant image which is one
     * less than the  id
     * @return The background image or logo image id that corresponds to the restaurant
     */
    public String getRestaurantIDMinusOne() {
        Restaurant currentRestaurant = this.restaurant;
        String id = currentRestaurant.getRestaurantID();
        int restaurantId = Integer.parseInt(id) - 1;
        String resId = String.valueOf(restaurantId);

        return resId;

    }

    public LiveData<Integer> getAverageReviewScore() {
        return averageScore;
    }

    public void calculateAverageReviewScoreFromList(List<Review> reviews){
        float sum = 0;
        float average;

        Integer num = reviews.size();
        for (Review review :reviews) {
            sum += review.getReviewScore();
        }
        average = sum/num;
        this.averageScore.setValue(Math.round(average));
    }

    public void addReviews(String restaurantID, String reviewComment, float reviewScore) {
         addReviewUseCase.addReviewUse(restaurantID,reviewComment, reviewScore);
    }
}
