package com.mycompany.mainui.model;

import java.util.List;

/**
 * Created by Dell on 06-May-16.
 */
public class GetSuggestSearchPro extends ResponseFromServerData{

    private List<SuggestProduct> suggestionProducts;

    public List<SuggestProduct> getSuggestionProducts() {
        return suggestionProducts;
    }

    public void setSuggestionProducts(List<SuggestProduct> suggestionProducts) {
        this.suggestionProducts = suggestionProducts;
    }
}
