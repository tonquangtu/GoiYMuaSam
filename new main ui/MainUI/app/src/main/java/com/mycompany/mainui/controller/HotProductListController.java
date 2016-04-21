package com.mycompany.mainui.controller;

/**
 * Created by Dell on 4/14/2016.
 */
public class HotProductListController {

//    private ITabHotProductListView tabHotProductListView;
//    private Activity context;
//    private List<HotProduct> hotProducts;
//
//
//    public HotProductListController(Activity context) {
//        this.context = context;
//        this.hotProducts = new ArrayList<>();
//    }
//
//    public void loadHotProduct() {
//
//        if(StatusInternet.isInternet(context)) {
//
//            Map<String, String> tag = new HashMap<>();
//            tag.put(RequestId.ID, RequestId.ID_GET_HOT_PRODUCT);
//            tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(context));
//
//            loadHotProductFromServer(tag);
//        }else {
//            DisplayNotification.displayNotifiNoNetwork(context);
//        }
//    }
//
//    public void loadHotProductFromServer(Map<String, String> tag) {
//
//        Call<GetHotProductData> call = ConnectServer
//                .getResponseAPI().callGetHotProduct(tag);
//        call.enqueue(new Callback<GetHotProductData>() {
//            @Override
//            public void onResponse(Call<GetHotProductData> call, Response<GetHotProductData> response) {
//
//                if(response != null) {
//
//                    GetHotProductData data = response.body();
//                    if(data != null) {
//                        if(data.getSuccess() == RequestId.SUCCESS) {
//                            tabHotProductListView.display(data.getHotProductList());
//                        }else {
//                            DisplayNotification.toast(context, data.getMessage());
//                        }
//                    }else {
//                        DisplayNotification.toast(context,"Khong nhan duoc du lieu");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetHotProductData> call, Throwable t) {
//                DisplayNotification.displayNotifiNoNetwork(context);
//            }
//        });
//
//    }


}
