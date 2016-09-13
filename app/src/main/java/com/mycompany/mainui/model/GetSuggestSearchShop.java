package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 06-May-16.
 */
public class GetSuggestSearchShop extends  ResponseFromServerData{

    private List<SuggestShop> suggestionShops;

    public List<SuggestShop> getSuggestionShops() {
        return suggestionShops;
    }

    public void setSuggestionShops(List<SuggestShop> suggestionShops) {
        this.suggestionShops = suggestionShops;
    }
}
