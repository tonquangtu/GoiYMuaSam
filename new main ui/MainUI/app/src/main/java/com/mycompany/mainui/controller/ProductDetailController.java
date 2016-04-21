package com.mycompany.mainui.controller;

/**
 * Created by Dell on 4/15/2016.
 */
public class ProductDetailController {

//
//    private List<ShortProduct> relationProducts;
//    private ProductDetail product;
//    private Activity context;
//    private ShortShop shortShop;
//    private IProductDetailView productDetailView;
//    private boolean isComplete = false;
//    public static final String ID_PRODUCT = "ID_PRODUCT";
//    public static final String PRODUCT_NAME = "PRODUCT_NAME";
//    private int like;
//
//    public ProductDetailController(Activity context) {
//
//        this.context = context;
//    }
//
//    // bat su kien o day, chu y la khi du lieu chua duoc load ve thi khong cho tuong tac
//    // hay la se co 1 cai man hinh
//
//
//
//    public void loadProduct(String idProduct) {
//
//        if(StatusInternet.isInternet(context)) {
//            Map<String , String> tag = new HashMap<>();
//            tag.put(RequestId.ID, RequestId.ID_GET_PRODUCT);
//            tag.put(RequestId.TAG_ID_PRODUCT, idProduct);
//            tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(context));
//
//            loadProductFromServer(tag);
//
//        }else {
//            DisplayNotification.displayNotifiNoNetwork(context);
//        }
//    }
//
//
//    private void loadProductFromServer(Map<String, String> tag) {
//
//        Call<GetProductData> call = ConnectServer.getResponseAPI().callGetProduct(tag);
//        call.enqueue(new Callback<GetProductData>() {
//            @Override
//            public void onResponse(Call<GetProductData> call, Response<GetProductData> response) {
//                if(response != null && response.isSuccessful()) {
//
//                    GetProductData data = response.body();
//                    if(data != null) {
//                        if(data.getSuccess() == RequestId.SUCCESS) {
//
//                            product = data.getProduct();
//                            like = data.getLike();
//                            shortShop = data.getShop();
//                            relationProducts = data.getRelationProducts();
//                            isComplete = true;
//                        }else {
//                            DisplayNotification.toast(context, data.getMessage());
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetProductData> call, Throwable t) {
//                DisplayNotification.displayNotifiNoNetwork(context);
//                DisplayNotification.toast(context, t.getMessage());
//            }
//        });
//    }
//
//    // neu nhu like roi thi huy like, nguoc lai neu chua like thi like
//    public void handleOnClickLike() {
//
//        if(StatusUser.isLogin) {
//
//            if(StatusInternet.isInternet(context)) {
//
//                if(isComplete) {
//                    Map<String, String> tag = new HashMap<>();
//                    tag.put(RequestId.ID, RequestId.ID_LIKE_PRODUCT);
//                    tag.put(RequestId.TAG_ID_PRODUCT, product.getIdProduct());
//                    tag.put(RequestId.TAG_USERNAME, InfoAccount.getUsername(context));
//
//                    sentRequestLikeToServer(tag);
//                    productDetailView.changeStatusLikeButton();
//                }
//
//            }else {
//                DisplayNotification.displayNotifiNoNetwork(context);
//            }
//        }else {
//            DisplayNotification.notifiLogin(context);
//        }
//    }
//
//    //load comment
//    public void handleOnClickComment() {
//
//        if(StatusUser.isLogin) {
//            if(StatusInternet.isInternet(context)) {
//
//               Intent intent = new Intent(context, CommentActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(ID_PRODUCT, product.getIdProduct());
//                bundle.putString(PRODUCT_NAME, product.getProductName());
//                intent.putExtra("package",bundle);
//
//                context.startActivity(intent);
//            }else {
//                DisplayNotification.displayNotifiNoNetwork(context);
//            }
//        }else {
//            DisplayNotification.notifiLogin(context);
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    public void sentRequestLikeToServer(Map<String, String> tag) {
//
//        Call<ResponseFromServerData> call = ConnectServer
//                .getResponseAPI().callLikeProduct(tag);
//
//        call.enqueue(new Callback<ResponseFromServerData>() {
//            @Override
//            public void onResponse(Call<ResponseFromServerData> call, Response<ResponseFromServerData> response) {
//
//                if (response != null && response.isSuccessful()) {
//
//                    ResponseFromServerData data = response.body();
//                    if (data != null || data.getSuccess() != RequestId.SUCCESS) {
//                        productDetailView.changeStatusLikeButton();
//                    }
//                }else {
//                    productDetailView.changeStatusLikeButton();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseFromServerData> call, Throwable t) {
//                productDetailView.changeStatusLikeButton();
//            }
//        });
//
//
//
//    }
//
//
//
//
//





}
