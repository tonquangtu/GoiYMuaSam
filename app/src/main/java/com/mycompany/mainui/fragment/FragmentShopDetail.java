package com.mycompany.mainui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.mycompany.mainui.R;
import com.mycompany.mainui.SpaceItem;
import com.mycompany.mainui.actiivity.MapActivity;
import com.mycompany.mainui.actiivity.ShopDetailActivity;
import com.mycompany.mainui.adapter.RelationShopAdapter;
import com.mycompany.mainui.adapter.SlidingImageProductAdapter;
import com.mycompany.mainui.map.InfoWindow;
import com.mycompany.mainui.model.ConfigData;
import com.mycompany.mainui.model.InfoAccount;
import com.mycompany.mainui.model.ShopDetail;
import com.mycompany.mainui.model.ShortShop;
import com.mycompany.mainui.model.StringUtil;
import com.mycompany.mainui.network.LoadShopDetail;
import com.mycompany.mainui.networkutil.ConnectServer;
import com.mycompany.mainui.networkutil.ServerLink;
import com.mycompany.mainui.networkutil.StatusInternet;
import com.mycompany.mainui.view.DisplayNotification;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

/**
 * Created by Dell on 08-May-16.
 */
public class FragmentShopDetail extends Fragment {

    public static String ARG_LOAD_SHOP = "ARG_LOAD_SHOP";
    public static String TITLE_LIKE = " Thích";
    public static final int LIKED = 1;
    LoadShopDetail loadShopDetail;
    ShopDetail shopDetail;
    List<ShortShop> relationShops;
    Activity activity;
    int like;
    View view;
    ViewPager viewPagerImage;
    int numOfLike;


    public static FragmentShopDetail newInstance(LoadShopDetail loadShopDetail) {

        FragmentShopDetail fragmentShopDetail = new FragmentShopDetail();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOAD_SHOP, loadShopDetail);
        fragmentShopDetail.setArguments(args);
        return  fragmentShopDetail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        this.loadShopDetail = (LoadShopDetail) getArguments().getSerializable(ARG_LOAD_SHOP);
        this.shopDetail = loadShopDetail.getShopDetail();
        this.relationShops = loadShopDetail.getShortShops();
        this.like = loadShopDetail.getLike();
        numOfLike = shopDetail.getNumOfLike();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(shopDetail != null && relationShops != null ) {
            view = inflater.inflate(R.layout.fragment_shop_detail, container, false);
            initViewPager();
            initInfoLikeAndView();
            initInfoShop();
            initRelationShop();

            String username = InfoAccount.getUsername(activity);
            if(username != null && username.length() > 0) {
                loadShopDetail.sentUserViewShop( username, shopDetail.getIdShop());
            }

        }else {
            view = inflater.inflate(R.layout.error_layout, container, false);
        }

        return  view;

    }


    public void initViewPager() {

        CirclePageIndicator pageIndicator;
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
                new SlidingImageProductAdapter(activity, shopDetail.getImageURLs(), listener);
        viewPagerImage.setAdapter(imageProductAdapter);
        pageIndicator.setViewPager(viewPagerImage);

        final float density = activity.getResources().getDisplayMetrics().density;

        pageIndicator.setRadius(5 * density);
        pageIndicator.setPageColor(activity.getResources().getColor(R.color.blue_200));
        pageIndicator.setFillColor(activity.getResources().getColor(R.color.white));
        pageIndicator.setStrokeWidth(0);

    }

    public void initInfoLikeAndView() {

        Button btnNumView;
        btnNumView = (Button)view.findViewById(R.id.btn_num_view);
        btnNumView.setText(shopDetail.getNumOfView() + "");

        final Button btnLike = (Button)view.findViewById(R.id.btn_like);
        ShareButton btnShare = (ShareButton) view.findViewById(R.id.btn_share);
        final Button btnPin = (Button)view.findViewById(R.id.btn_pin);

        btnLike.setText(numOfLike + TITLE_LIKE);
        if(like == LIKED) {
            Drawable drawable = activity.getResources().getDrawable(R.drawable.icon_like2);
            btnLike.setCompoundDrawables(drawable, null, null, null);
        }

        final Animation animScale = AnimationUtils.loadAnimation(activity, R.anim.scale);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(like == 0) {
                    numOfLike ++;
                    like = 1;
                    Drawable drawable = activity.getResources().getDrawable(R.drawable.icon_like2);
                    int h = drawable.getIntrinsicHeight();
                    int w = drawable.getIntrinsicWidth();
                    drawable.setBounds( 0, 0, w, h );
                    btnLike.setCompoundDrawables(drawable, null, null, null);
                }else {
                    numOfLike --;
                    like = 0;
                    Drawable drawable = activity.getResources().getDrawable(R.drawable.icon_no_like);
                    int h = drawable.getIntrinsicHeight();
                    int w = drawable.getIntrinsicWidth();
                    drawable.setBounds( 0, 0, w, h );
                    btnLike.setCompoundDrawables(drawable, null, null, null);
                }
                btnLike.setText(numOfLike + TITLE_LIKE);
                v.startAnimation(animScale);

                String username = InfoAccount.getUsername(activity);
                if(username != null && username.length() > 0) {
                    loadShopDetail.sentLikeShop(username, shopDetail.getIdShop(), like);
                }

                // gui yeu cau like den server
                //////////////////////
                /////////////////////
            }
        });


        btnShare.setShareContent(getShareContent());
        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something as sent to server to save
            }
        });

        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(InfoAccount.isLogin(activity)) {
                    if(StatusInternet.isInternet(activity)) {
                        loadShopDetail.pinShop(shopDetail.getIdShop(), InfoAccount.getUsername(activity), activity);
                        btnPin.setEnabled(false);
                    }
                }
            }
        });

    }

    public ShareLinkContent getShareContent() {

        String title = shopDetail.getShopName();
        if(title == null) {
            title = "GOSHOPPING.VN";
        }else {
            title = title.toUpperCase();
        }
        String description = "";
        if(shopDetail.getSales() > 0.0) {
            description = shopDetail.getNote() + " + Khuyến mại :"
                    + shopDetail.getSales() + " % \nChỉ có trên GoShopping";
        }
        // sau nay o day thay bang link dan den trang chua noi dung cua cua hang nay
        Uri uri;
        if(shopDetail.getImageURLs().size() > 0) {
            uri = Uri.parse(StringUtil.convertURL(shopDetail.getImageURLs().get(0)));
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

    public void initInfoShop() {

        TextView txtShopName = (TextView)view.findViewById(R.id.txt_shop_name);
        TextView txtShopNote = (TextView)view.findViewById(R.id.txt_shop_sales_note);
        TextView txtShopAddress = (TextView)view.findViewById(R.id.txt_shop_address);

        txtShopName.setText(shopDetail.getShopName());
        txtShopAddress.setText(shopDetail.getAddress());

        String note = shopDetail.getNote();
        String titleNote = "";
        if(shopDetail.getSales() <= 0.0) {
            if(note != null && note.length() > 0) {
                txtShopNote.setText(shopDetail.getNote());
            }else {
                txtShopNote.setText(titleNote);
            }
        }else {
            txtShopNote.setText(note + " + Khuyến mại : " + shopDetail.getSales() + " %");
        }

        //-------------------------------------------------------------------------------------------
        Button btnChatShop = (Button)view.findViewById(R.id.btn_chat_shop);
        Button btnPhone = (Button)view.findViewById(R.id.btn_phone);
        Button btnViewAllProduct = (Button)view.findViewById(R.id.btn_view_all_product_in_shop);
        Button btnViewMap = (Button)view.findViewById(R.id.btn_view_map);

        final String phone = shopDetail.getPhone();
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
                    DisplayNotification.toast(activity, "Không có SĐT !");
                }
            }
        });

        btnChatShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle chat shop, check login
                // check internet

            }
        });

        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StatusInternet.isInternet(activity)) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String name = shopDetail.getShopName();
                            String address = shopDetail.getAddress();
                            double latitude = shopDetail.getLocation().getLocationX();
                            double longitude = shopDetail.getLocation().getLocationY();

                            String url = StringUtil.convertURL(shopDetail.getImageURLs().get(0));
                            InfoWindow infoWindow = new InfoWindow(name,address, url, latitude, longitude);
                            Intent intent = new Intent(activity, MapActivity.class);
                            intent.putExtra("Info Window",infoWindow);
                            startActivity(intent);

                        }
                    }).start();
                }else {
                    DisplayNotification.toast(activity, "Không có kết nối mạng !");
                }
            }
        });

        btnViewAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // view all product in shop
                // load data from server
                // reuse layout product in catalog
            }
        });
    }

    public void initRelationShop() {


        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_relation_shop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        SpaceItem decoration =
                new SpaceItem(15, SpaceItem.HORIZONTAL);
        recyclerView.addItemDecoration(decoration);

        RelationShopAdapter adapter = new RelationShopAdapter(activity, relationShops);
        adapter.setListener(new RelationShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(StatusInternet.isInternet(activity)) {
                    ShortShop shortShop = relationShops.get(position);
                    Intent intent = new Intent(activity, ShopDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(ConfigData.ARG_ID_SHOP, shortShop.getIdShop());
                    bundle.putString(ConfigData.ARG_SHOP_NAME, shortShop.getShopName());
                    intent.putExtra(ConfigData.ARG_PACKAGE, bundle);
                    activity.startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);

    }



}
