package com.example.softeng306_application.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.softeng306_application.entity.Asian;
import com.example.softeng306_application.entity.Cafe;
import com.example.softeng306_application.entity.Category;
import com.example.softeng306_application.entity.European;
import com.example.softeng306_application.entity.FastFood;
import com.example.softeng306_application.entity.Restaurant;
import com.example.softeng306_application.usecase.GetAllRestaurantsUseCase;
import com.example.softeng306_application.usecase.GetFavouritesUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListViewModel extends AndroidViewModel {
    private GetFavouritesUseCase getFavouritesUseCase;
    private GetAllRestaurantsUseCase getAllRestaurantsUseCase;
    private List<Category> categoryList;
    private List<Restaurant> searchList;
    private List<Category> allCategories = new ArrayList<Category>() {
        {
            add(new Asian());
            add(new European());
            add(new FastFood());
            add(new Cafe());
        }
    };
    private Boolean isFavourite;

    private String prev = "";

    public ListViewModel(@NonNull Application application) {
        super(application);
        categoryList = getAllCategories();
        getFavouritesUseCase = getFavouritesUseCase.getInstance();
        getAllRestaurantsUseCase = getAllRestaurantsUseCase.getInstance();
    }

    public List<Category> getAllCategories() {
        return allCategories;
    }

    public List<String> getAllCategoryNameOptions() {
        List<String> allCategoryNames = new ArrayList<String>();
        allCategoryNames.add("ALL");
        for(Category category: getAllCategories()){
            allCategoryNames.add(category.getCategoryType());
        }
        return allCategoryNames;
    }
    public List<Restaurant> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<Restaurant> searchList) {
        this.searchList = searchList;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public List<Category> getCategory() {
        return categoryList;
    }

    public void setCategory(Category category) {
        List<Category> categoryList = new ArrayList<Category>(){
            {
                add(category);
            }
        };
        this.categoryList = categoryList;
    }

    /**
     * This method sets the category of the list activity.
     * This is reliant on the category name String value which it gets from either the user clicking on a category on Main activity or he dropdown menu
     * @param categoryName
     */
    public void setCategory(String categoryName) {
        List<Category> categoryList = new ArrayList<Category>();
        switch (categoryName){
            case "ALL":
                setAllCategories();
                break;
            case "FAST FOOD":
                categoryList.clear();
                categoryList.add(new FastFood());
                this.categoryList = categoryList;
                break;
            case "EUROPEAN":
                categoryList.clear();
                categoryList.add(new European());
                this.categoryList = categoryList;
                break;
            case "ASIAN":
                categoryList.clear();
                categoryList.add(new Asian());
                this.categoryList = categoryList;
                break;
            case "CAFE":
                categoryList.clear();
                categoryList.add(new Cafe());
                this.categoryList = categoryList;
                break;

        }
    }

    /**
     * This method sets all the categories to be filtered to ALL
     */
    public void setAllCategories() {
        this.categoryList = getAllCategories();
    }

    /**
     * This method gets the user's favourite restaurants by the current selected category
     * @return User's favourite restaurants filtered by category
     */
    public LiveData<List<Restaurant>> getFavouritesByCategory() {
        LiveData<List<Restaurant>> filteredLiveData = Transformations.map(getFavouritesUseCase.getFavouriteRestaurants(), restaurantList -> {
            // Filter condition
            List<Restaurant> filteredItems = restaurantList.stream()
                    .filter(restaurant -> getCategory().contains(restaurant.getCategory()))
                    .collect(Collectors.toList());

            setSearchList(filteredItems);
            return filteredItems;
        });
        return filteredLiveData;
    }
    public LiveData<List<Restaurant>> getFavouritesList() {
        return getFavouritesUseCase.getFavouriteRestaurants();
    }


    /**
     * This method gets the  restaurants by the current selected category
     * @return Restaurants filtered by category
     */
    public LiveData<List<Restaurant>> getRestaurantByCategoryList() {
        LiveData<List<Restaurant>> filteredLiveData = Transformations.map(getAllRestaurantsUseCase.getAllRestaurants(), restaurantList -> {
            // Filter condition
            List<Restaurant> filteredItems = restaurantList.stream()
                    .filter(restaurant -> getCategory().contains(restaurant.getCategory()))
                    .collect(Collectors.toList());

            setSearchList(filteredItems);
            return filteredItems;
        });
        return filteredLiveData;
    }

    /**
     * This method filters the current restaurant list on the List activity by the search term in the search bar
     * @param query The search query term
     * @return The filtered restaurant list
     */
    public LiveData<List<Restaurant>> filterList(String query) {
        LiveData<List<Restaurant>> filteredLiveData = Transformations.map(getRestaurantByCategoryList(), restaurantList -> {
            // Filter condition
            List<Restaurant> filteredRestaurants = new ArrayList<>();

            if(prev.length() >= query.length()) {
                for(Restaurant r: getSearchList()) {
                    if (r.getName().toLowerCase().contains(query)) {
                        filteredRestaurants.add(r);
                    }
                }
            } else {
                for(Restaurant r: restaurantList) {
                    if (r.getName().toLowerCase().contains(query)) {
                        filteredRestaurants.add(r);
                    }
                }
            }
            prev = query;
            return filteredRestaurants;
        });
        return filteredLiveData;
    }
}
