package com.anandniketan.skool360teacher.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anandniketan.skool360teacher.Activities.DashBoardActivity;
import com.anandniketan.skool360teacher.Adapter.ImageAdapter;
import com.anandniketan.skool360teacher.AsyncTasks.GetStaffProfileAsyncTask;
import com.anandniketan.skool360teacher.AsyncTasks.LoginAsyncTask;
import com.anandniketan.skool360teacher.Models.LoginModel;
import com.anandniketan.skool360teacher.Models.UserProfileModel;
import com.anandniketan.skool360teacher.R;
import com.anandniketan.skool360teacher.Utility.AppConfiguration;
import com.anandniketan.skool360teacher.Utility.Utility;
import com.anandniketan.skool360teacher.databinding.FragmentHomeBinding;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding bindinghome;
    private View rootView;
    private Button btnMenu, btn_notification, menu_linear;
    private GridView grid_view;
    private ImageView logo;
    private LinearLayout header;
    private Context mContext;
    private Fragment fragment = null;
    private FragmentManager fragmentManager = null;
    //    Change Megha 04-09-2017
    private CircleImageView profile_image;
    private ImageLoader imageLoader;
    private TextView student_name_txt, student_classname_txt;
    private ProgressDialog progressDialog;
    private ArrayList<UserProfileModel> userProfileModels = new ArrayList<>();
    private GetStaffProfileAsyncTask getStaffProfileAsyncTask = null;
    boolean flag = false;


    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity().getApplicationContext();
        initViews();
        setListners();
        if (Utility.isNetworkConnected(mContext)) {
        getUserProfile();
        } else {
            Utility.ping(mContext, "Network not available");

        }
        return rootView;
    }

    public void initViews() {
        menu_linear = (Button) rootView.findViewById(R.id.menu_linear);
        btnMenu = (Button) rootView.findViewById(R.id.btnMenu);
        grid_view = (GridView) rootView.findViewById(R.id.grid_view);
        grid_view.setAdapter(new ImageAdapter(mContext));
        grid_view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.bounce));
        student_name_txt = (TextView) rootView.findViewById(R.id.student_name_txt);
        student_classname_txt = (TextView) rootView.findViewById(R.id.student_classname_txt);
        header = (LinearLayout) rootView.findViewById(R.id.header);

        profile_image = (CircleImageView) rootView.findViewById(R.id.profile_image);
        imageLoader = ImageLoader.getInstance();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .threadPriority(Thread.MAX_PRIORITY)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)// .enableLogging()
                .build();
        imageLoader.init(config.createDefault(mContext));
    }

    //change Megha 04-09-2017

    public void setListners() {
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashBoardActivity.onLeft();
            }
        });
        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    fragment = new AttendanceFragment();
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(0, 0)
                            .replace(R.id.frame_container, fragment).commit();
                }
            }
        });
        menu_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == false) {
                    header.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    flag = true;
                }else{
                    header.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        });
    }

    public void getUserProfile() {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please wait...");
            progressDialog.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("StaffID", Utility.getPref(mContext, "StaffID"));
                        getStaffProfileAsyncTask = new GetStaffProfileAsyncTask(params);
                        userProfileModels = getStaffProfileAsyncTask.execute().get();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                if (userProfileModels.size() > 0) {
                                    fillData();
                                }

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

    }

    public void fillData() {
        if (userProfileModels.get(0).getImage().equalsIgnoreCase("")) {
            imageLoader.displayImage(String.valueOf(R.drawable.profile_pic_holder), profile_image);
        } else {
            imageLoader.displayImage(userProfileModels.get(0).getImage(), profile_image);
        }

        student_name_txt.setText(userProfileModels.get(0).getEmp_Name());
        student_classname_txt.setText(userProfileModels.get(0).getDesignation());
        for (int i = 0; i < userProfileModels.get(0).getGetclassDetailsArrayList().size(); i++) {
            AppConfiguration.rows.add(userProfileModels.get(0).getGetclassDetailsArrayList().get(i));
        }


    }
}

