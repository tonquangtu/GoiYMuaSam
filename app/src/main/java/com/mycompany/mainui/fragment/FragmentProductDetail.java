package com.mycompany.mainui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.mycompany.mainui.R;
import com.mycompany.mainui.SpaceItem;
import com.mycompany.mainui.actiivity.LoginNormalActivity;
import com.mycompany.mainui.actiivity.ProductDetailActivity;
import com.mycompany.mainui.actiivity.ShopDetailActivity;
import com.mycompany.mainui.adapter.RelationProductAdapter;
import com.mycompany.mainui.adapter.SlidingImageProductAdapter;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.ProductDetail;
import com.mycompany.mainui.model.ShortProduct;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.network.LoadProductDetail;
import com.mycompany.mainui.networkutil.ConnectServer;
import com.mycompany.mainui.networkutil.LoadImageFromUrl;
import com.mycompany.mainui.networkutil.ServerLink;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

/**
 * Created by Dell on 07-May-16.
 */
public class FragmentProductDetail extends Fragment {

    public static final String ARG_LOAD_PRODUCT = "ARG_LOAD_PRODUCT";
    public static final String HAVE_PRODUCT = "Còn hàng";
    public static final String NO_PRODUCT = "Hết hàng";
    public static final String NO_NOTE = "Thỏa sức mua sắm cùng ";
    public static final String TITLE_SALES = "Khuyến mại";
    public static final String TITLE_VIEW = " lượt xem";
    public static final String TITLE_VND = " đ";
    public static final String TITLE_PERCENT = " %";
    public static final String TITLE_NUM_LIKE = " Thích";
    public static final String TITLE_NUM_COMMENT = " Bình luận";
    public static final int LIKED = 1;
    LoadProductDetail loadProductDetail;
    ProductDetail productDetail;
    ShortShop shortShop;
    List<ShortProduct> relationProducts;
    int like;
    String phone;
    int numOfLike;


    ProductDetailActivity mActivity;

    View view;

    ViewPager viewPagerImage;
    CirclePageIndicator pageIndicator;
    //------------------------------


    public static FragmentProductDetail newInstance(LoadProductDetail loadProductDetail) {

        FragmentProductDetail fragmentProductDetail = new FragmentProductDetail();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOAD_PRODUCT, loadProductDetail);
        fragmentProductDetail.setArguments(args);

        return fragmentProductDetail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (ProductDetailActivity)getActivity();
        Bundle args = getArguments();
        loadProductDetail = (LoadProductDetail) args.getSerializable(ARG_LOAD_PRODUCT);
        productDetail = loadProductDetail.getProduct();
        shortShop = loadProductDetail.getShortShop();
        like = loadProductDetail.getLike();
        phone = loadProductDetail.getPhone();
        relationProducts = loadProductDetail.getRelationProducts();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(productDetail != null && shortShop != null) {
            view = inflater.inflate(R.layout.fragment_product_detail, container, false);
            numOfLike = productDetail.getNumOfLike();

            initViewPager();
            initViewInfoProduct();
            initShop();
            initRelationProduct();

            String username = InfoAccount.getUsername(mActivity);
            if(username != null && username.length() > 0) {
                loadProductDetail.sentUserViewProduct( username, productDetail.getIdProduct());
            }

        }else {
            view = inflater.inflate(R.layout.error_layout, container, false);
        }

        return view;
    }


    public void initViewPager() {

        viewPagerImage = (ViewPager)view.findViewById(R.id.image_pager);
        pageIndicator = (CirclePageIndicator)view.findViewById(R.id.indicator);

        // init adapter
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // do something when click image
                int position = viewPagerImage.getCurrentItem();
            }
        };

        SlidingImageProductAdapter imageProductAdapter =
                new SlidingImageProductAdapter(mActivity, productDetail.getImageURLs(), listener);
        viewPagerImage.setAdapter(imageProductAdapter);
        pageIndicator.setViewPager(viewPagerImage);
        final float density = mActivity.getResources().getDisplayMetrics().density;

        pageIndicator.setRadius(5 * density);
        pageIndicator.setPageColor(mActivity.getResources().getColor(R.color.blue_200));
        pageIndicator.setFillColor(mActivity.getResources().getColor(R.color.white));
        pageIndicator.setStrokeWidth(0);

    }

    public void initViewInfoProduct() {

        TextView txtNewPrice = (TextView)view.findViewById(R.id.txt_new_price_product);
        TextView txtPrice = (TextView)view.findViewById(R.id.txt_price_product);
        TextView txtProductName = (TextView)view.findViewById(R.id.txt_product_name);
        TextView txtProductColor = (TextView)view.findViewById(R.id.txt_product_color);
        TextView txtStatus = (TextView)view.findViewById(R.id.txt_product_state);
        TextView txtSize = (TextView)view.findViewById(R.id.txt_product_size);
        TextView txtSales = (TextView)view.findViewById(R.id.txt_product_sales);

        final Button btnLike = (Button)view.findViewById(R.id.btn_like);
        Button btnTitleNumView = (Button)view.findViewById(R.id.btn_num_view);
        Button btnComment = (Button)view.findViewById(R.id.btn_comment);
        ShareButton btnShare = (ShareButton) view.findViewById(R.id.btn_share);
        Button btnDetail = (Button)view.findViewById(R.id.btn_product_detail);

        txtNewPrice.setText(productDetail.getNewPrice() + TITLE_VND);
        txtPrice.setText(productDetail.getPrice() + TITLE_VND);
        txtPrice.setPaintFlags(txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        btnTitleNumView.setText(productDetail.getNumOfView() + "");
        txtProductName.setText(productDetail.getProductName());
        txtSales.setText(productDetail.getPromotionPercent() + TITLE_PERCENT);

        //-------------------------------------------------
        if(productDetail.getStatus() == 1) {
            txtStatus.setText(HAVE_PRODUCT);
        }else {
            txtStatus.setText(NO_PRODUCT);
        }
        StringBuilder size = new StringBuilder();
        List<String> sizes = productDetail.getSize();
        int n = sizes.size();
        for(int i = 0; i < n; i++) {
           if(i != n -1) {
               size.append(sizes.get(i) + ", ");
           }else {
               size.append(sizes.get(i));
           }
        }
        txtSize.setText(size.toString());

        //--------------------------------------------
        List<String> colors = productDetail.getColor();
        StringBuilder color = new StringBuilder();
        n = colors.size();
        for(int i = 0; i < n; i++) {
            if(i != n -1) {
                color.append(colors.get(i) + ", ");
            }else {
                color.append(colors.get(i));
            }
        }
        txtProductColor.setText(color.toString());
        //----------------------------------------------
        btnLike.setText(numOfLike + TITLE_NUM_LIKE);
        if(like == LIKED) {
            Drawable drawable = mActivity.getResources().getDrawable(R.drawable.icon_like2);
            btnLike.setCompoundDrawables(drawable, null, null, null);
        }
        final Animation animScale = AnimationUtils.loadAnimation(mActivity, R.anim.scale);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isLogin = InfoAccount.isLogin(mActivity);
                if(isLogin) {
                    if(like == 0) {
                        numOfLike ++;
                        like = 1;
                        Drawable drawable = mActivity.getResources().getDrawable(R.drawable.icon_like2);
                        int h = drawable.getIntrinsicHeight();
                        int w = drawable.getIntrinsicWidth();
                        drawable.setBounds( 0, 0, w, h );
                        btnLike.setCompoundDrawables(drawable, null, null, null);
                    }else {
                        numOfLike --;
                        like = 0;
                        Drawable drawable = mActivity.getResources().getDrawable(R.drawable.icon_no_like);
                        int h = drawable.getIntrinsicHeight();
                        int w = drawable.getIntrinsicWidth();
                        drawable.setBounds( 0, 0, w, h );
                        btnLike.setCompoundDrawables(drawable, null, null, null);
                    }

                    btnLike.setText(numOfLike + TITLE_NUM_LIKE);
                    v.startAnimation(animScale);
                    String username = InfoAccount.getUsername(mActivity);
                    loadProductDetail.sentLike(username, productDetail.getIdProduct(), like);
                }else {
                    notifiLogin();
                }

                // gui yeu cau like den server

            }
        });

        btnComment.setText( productDetail.getNumOfComment() + TITLE_NUM_COMMENT);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // handle click comment
                // open 1 activity contain comment
            }
        });


        btnShare.setShareContent(getShareContent());

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayNotification.toast(mActivity, "Xem chi tiet");
            }
        });
    }

    public void initShop() {

        CardView cardView = (CardView)view.findViewById(R.id.shop);
        ImageView imageShop = (ImageView)view.findViewById(R.id.image_view_shop_icon);
        TextView txtShopName = (TextView)view.findViewById(R.id.txt_shop_name);
        TextView txtShopAddress = (TextView)view.findViewById(R.id.txt_shop_address);
        TextView txtShopNote = (TextView)view.findViewById(R.id.txt_shop_note);
        TextView txtShopNumView = (TextView)view.findViewById(R.id.txt_shop_number_view);

        String url = StringUtil.convertURL(shortShop.getImageURL());
        LoadImageFromUrl.loadImageFromURL(url, imageShop, mActivity);
        txtShopName.setText(shortShop.getShopName());
        txtShopAddress.setText(shortShop.getAddress());
        txtShopNumView.setText(shortShop.getNumOfView() + " lượt xem");
        String note = shortShop.getNote();
        String titleNote = "Thỏa sức mua sắm cùng " + shortShop.getShopName();
        if(shortShop.getSales() <= 0.0) {
            if(note != null && note.length() > 0) {
                txtShopNote.setText(shortShop.getNote());
            }else {
                txtShopNote.setText(titleNote);
            }
        }else {
            txtShopNote.setText(note + " + Khuyến mại : " + shortShop.getSales() + " %");
        }


        Button btnGoShop = (Button)view.findViewById(R.id.btn_go_shop);
        Button btnPhone = (Button)view.findViewById(R.id.btn_phone);

        btnGoShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               handleGoShop();
                // do something to go shop
            }
        });

        if(phone == null || phone.trim().length() == 0) {
            btnPhone.setText("Không có SĐT");
            btnPhone.setClickable(false);
        }else {
            btnPhone.setText(phone);
        }

        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Long phoneNumber = Long.parseLong(phone);
                    String dialer = "tel:" + phoneNumber;
                    Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse(dialer));
                    startActivity(intentCall);
                }catch (NumberFormatException e) {
                    DisplayNotification.toast(mActivity, "Không có SĐT !");
                }
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleGoShop();
            }
        });
    }

    public void initRelationProduct() {

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_relation_product);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        SpaceItem decoration =
                new SpaceItem(15, SpaceItem.HORIZONTAL);
        recyclerView.addItemDecoration(decoration);

        RelationProductAdapter adapter = new RelationProductAdapter(mActivity, relationProducts);
        adapter.setListener(new RelationProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                // view detail product with id of its product
                if(StatusInternet.isInternet(mActivity)) {

                    ShortProduct shortProduct = relationProducts.get(position);
                    if(shortProduct != null) {
                        Intent intent = new Intent(mActivity, ProductDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(ConfigData.ARG_ID_PRODUCT, shortProduct.getIdProduct());
                        bundle.putString(ConfigData.ARG_PRODUCT_NAME, shortProduct.getProductName());
                        intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
                        mActivity.startActivity(intent);
                    }
                }else {
                    DisplayNotification.displayNotifiNoNetwork(mActivity);
                }
            }
        });

        recyclerView.setAdapter(adapter);

    }


    public ShareLinkContent getShareContent() {

        String title = productDetail.getProductName().toUpperCase();
        String description = "Giá :" + productDetail.getNewPrice() +
                " VNĐ - Mua tại Shop " + shortShop.getShopName() + " trên GoShopping";

        // sau nay o day thay bang link dan den trang chua noi dung cua san pham nay
        Uri uri;
        if(productDetail.getImageURLs().size() > 0) {
             uri = Uri.parse(StringUtil.convertURL(productDetail.getImageURLs().get(0)));
        }else {
            uri = Uri.parse(ConnectServer.LINK + "/" + ServerLink.LINK_LOGIN);
        }

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle(title)
                .setContentDescription(description)
                .setContentUrl(uri)
                .build();
        return linkContent;
    }

    public void handleGoShop() {
        if(StatusInternet.isInternet(mActivity)) {
            Intent intent = new Intent(mActivity, ShopDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ConfigData.ARG_ID_SHOP, shortShop.getIdShop());
            bundle.putString(ConfigData.ARG_SHOP_NAME, shortShop.getShopName());
            intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
            mActivity.startActivity(intent);
        }else {
            DisplayNotification.displayNotifiNoNetwork(mActivity);
        }
    }

    public void notifiLogin() {

        String title = "Thông báo";
        String message = "Bạn cần đăng nhập để thực hiện chức năng này !";

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Đăng nhập", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mActivity, LoginNormalActivity.class);
                mActivity.startActivity(intent);
            }
        });

        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
}
