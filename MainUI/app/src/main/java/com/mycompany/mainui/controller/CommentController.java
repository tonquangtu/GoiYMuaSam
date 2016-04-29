package com.mycompany.mainui.controller;

/**
 * Created by Dell on 4/16/2016.
 */
public class CommentController {

//    private Activity context;
//    private String idProduct;
//    private ICommentView commentView;
//
//    public CommentController(Activity context, String idProduct, String productName) {
//        this.context = context;
//        this.idProduct = idProduct;
//        commentView.displayTitle(productName);
//
//        commentView.setSentCommentOnClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleSentCommentOnClick();
//            }
//        });
//    }
//
//    public void loadComment() {
//
//        Map<String, String> tag = new HashMap<>();
//        tag.put(RequestId.ID, RequestId.TAG_GET_COMMENT);
//        tag.put(RequestId.TAG_ID_PRODUCT, idProduct);
//
//        Call<GetCommentData> call = ConnectServer.getResponseAPI().callGetComments(tag);
//        call.enqueue(new Callback<GetCommentData>() {
//            @Override
//            public void onResponse(Call<GetCommentData> call, Response<GetCommentData> response) {
//
//                if (response != null && response.isSuccessful()) {
//                    GetCommentData data = response.body();
//                    if (data != null) {
//
//                        if (data.getSuccess() == RequestId.SUCCESS) {
//                            //hien thi comment len man hinh
//                            commentView.displayComments(data.getComments());
//                        } else {
//                            DisplayNotification.toast(context, data.getMessage());
//                        }
//                    }
//                } else {
//                    DisplayNotification.errorLoaddata(context);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetCommentData> call, Throwable t) {
//                DisplayNotification.displayNotifiNoNetwork(context);
//            }
//        });
//    }
//
//    public void handleSentCommentOnClick() {
//
//        String comment = commentView.getComment();
//        if(StatusUser.isLogin) {
//            if(StatusInternet.isInternet(context)) {
//                if(comment != null && comment.trim().equals("") && comment.trim().length() > 1) {
//
//                    comment = comment.trim();
//                    String username = InfoAccount.getUsername(context);
//
//                    Map<String, String> tag = new HashMap<>();
//                    tag.put(RequestId.ID, RequestId.ID_SENT_COMMENT);
//                    tag.put(RequestId.TAG_ID_PRODUCT, idProduct);
//                    tag.put(RequestId.TAG_USERNAME, username);
//                    tag.put(RequestId.TAG_COMMENT, comment);
//
//                    sentCommentToServer(tag, new Comment(comment, username));
//                }else {
//                    DisplayNotification.toast(context,
//                            "Binh luan khong duoc rong hoac chua it nhat 2 ky tu");
//                }
//            }
//
//        }
//    }
//
//    public void sentCommentToServer(Map<String, String> tag, final Comment comment) {
//
//        Call<ResponseFromServerData> call = ConnectServer.getResponseAPI().callSentComment(tag);
//        call.enqueue(new Callback<ResponseFromServerData>() {
//            @Override
//            public void onResponse(Call<ResponseFromServerData> call, Response<ResponseFromServerData> response) {
//
//                if(response != null && response.isSuccessful()) {
//
//                    ResponseFromServerData data = response.body();
//                    if(data != null && data.getSuccess() == RequestId.SUCCESS) {
//                        commentView.appendComment(comment);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseFromServerData> call, Throwable t) {
//
//            }
//        });
//
//
//
//    }
//


}
