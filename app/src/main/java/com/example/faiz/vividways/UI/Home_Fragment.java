package com.example.faiz.vividways.UI;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.faiz.vividways.Adapters.RecyclerTouchListener;
import com.example.faiz.vividways.Adapters.ScrollingLinearLayout;
import com.example.faiz.vividways.Adapters.SectionListDataAdapter;
import com.example.faiz.vividways.Adapters.TouristSpotCardAdapter;
import com.example.faiz.vividways.Models.Axis;
import com.example.faiz.vividways.Models.FilterItem;
import com.example.faiz.vividways.Models.UserModel;
import com.example.faiz.vividways.Utils.AppLogs;
import com.example.faiz.vividways.Utils.FirebaseHandler;
import com.example.faiz.vividways.Models.ItemObject;
import com.example.faiz.vividways.R;
import com.example.faiz.vividways.UI.Activities.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Faiz on 7/20/2017.
 */

public class Home_Fragment extends android.support.v4.app.Fragment {

    private static final String TAG = "Home_Activity";
    public View view;
    public ViewPager mViewPager;
    private DatabaseReference firebase;
    private FirebaseAuth mAuth;
    private ArrayList<ItemObject> imageURL;
    public RecyclerView my_recycler_view;
    public static Home_Fragment home_fragment;
    public LinearLayoutManager layoutManager;
    private StorageReference rootStorageRef, imageRef, folderRef, fileStorageRef;
    private String imgPath;
    private static final int SELECTED_PICTURE = 9162;
    private ProgressDialog mProgressDialog;
    private String downloadURL;
    private String string_caption;
    public SectionListDataAdapter adapter1;
    private static final int CAMERA_REQUEST = 1888;
    private UserModel userModel;
    public FilterItem filterItemObj;
    private static final int REQUEST_READ_CONTACTS = 2;
    public Uri mCapturedImageURI;
    public Button leave_btn, take_btn;
    private static int displayedposition = 0;
    private static int firstVisibleInListview;
    private float itemWidth;
    private float padding;
    private float firstItemWidth;
    private float allPixels;
    private int mLastPosition;
    int counterOne = 0, counterTwo = 0;
    public int position = 0;
    public int i = 0;
    private Camera camera;
    // private CameraPreview mPreview;
    public float firstItemWidthDate;
    public float paddingDate;
    public float itemWidthDate;
    public int allPixelsDate;
    public int finalWidthDate;
    int counter = 0;
    int dummyPosition = 0;
    ScrollingLinearLayout scrollingLinearLayout;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    public boolean flag = false;
    public String tempCaption;
    public int index = 10;
    public Intent intent;
    private float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 70;
    //  TouristSpotCardAdapter adapter1;
    public ArrayList<String> placeHolderList;
    public ArrayList<Axis> axisArrayList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_layout, null);
        home_fragment = Home_Fragment.this;
        leave_btn = (Button) view.findViewById(R.id.leave_btn);
        take_btn = (Button) view.findViewById(R.id.take_btn);
        mAuth = FirebaseAuth.getInstance();
        firebase = FirebaseDatabase.getInstance().getReference();
        axisArrayList = new ArrayList<>();
        placeHolderList = new ArrayList<>();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        itemWidth = getResources().getDimension(R.dimen.item_width);
        padding = (size.x - itemWidth) / 2;
        firstItemWidth = getResources().getDimension(R.dimen.padding_item_width);

        allPixels = 0;

        //   camera = getCameraInstance();


        MainActivity.appbar_TextView.setText("Home");
        MainActivity.Uploadbutton.setVisibility(View.VISIBLE);
        MainActivity.getInstance().home_image.setImageResource(R.mipmap.sel_home_icon);
        MainActivity.getInstance().home_text.setTextColor(Color.parseColor("#da59a8"));
        //  MainActivity.getInstance().setting_image.setImageResource(R.mipmap.settings_icon);
        //   MainActivity.getInstance().setting_text.setTextColor(Color.parseColor("#bfbfbf"));
        imageURL = new ArrayList<ItemObject>();
        rootStorageRef = FirebaseStorage.getInstance().getReference();
        folderRef = rootStorageRef.child("postImages");
        //
        my_recycler_view = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(my_recycler_view);
        MainActivity.back_image.setVisibility(View.GONE);
        MainActivity.delete_image.setVisibility(View.GONE);
        MainActivity.report_image.setVisibility(View.GONE);

        // LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        //  recyclerView.setLayoutManager(mLayoutManager);

        //    Toast.makeText(getActivity(),"Your Location : \n California City, California",Toast.LENGTH_SHORT).show();
        //    my_recycler_view.setOnFlingListener(snapHelper);
        //  my_recycler_view.setHasFixedSize(true);
        //  my_recycler_view.stopNestedScroll();
        //   my_recycler_view.stopScroll();

        userModel = new UserModel();
        filterItemObj = new FilterItem();
        adapter1 = new SectionListDataAdapter(getActivity(), imageURL, placeHolderList);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        int duration = getResources().getInteger(R.integer.scroll_duration);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //   my_recycler_view.setLayoutManager();
        //  scrollingLinearLayout  = new ScrollingLinearLayout(getActivity(), LinearLayoutManager.HORIZONTAL, false, duration);
        my_recycler_view.setLayoutManager(layoutManager);
        my_recycler_view.setAdapter(adapter1);
//        my_recycler_view.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), my_recycler_view, new RecyclerTouchListener.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));

        //        my_recycler_view.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                 //Stop only scrolling.
//                return rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
//            }
//        });


        my_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View child = null;
                int pos = 0;
                //   if(dx<(-168)){
                //     child = recyclerView.findChildViewUnder(dx, dy);
                //   pos = recyclerView.getChildPosition(child);

                //      }
                //    }else if (dx==0){

                View view = snapHelper.findSnapView(layoutManager);
                pos = recyclerView.getChildAdapterPosition(view);


                //       child = recyclerView.findChildViewUnder(dx, dy);
                //    pos = recyclerView.getChildPosition(child);
//                if(pos>imageURL.size()){
//                    pos=1;
//                }
                ItemObject itemObject;

//                if (imageURL.get(0) == null && imageURL.get(imageURL.size()-1) ==null) {
//                  //  my_recycler_view.setOnTouchListener();
//                  //   flag = recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
//               //     my_recycler_view.stopScroll();
//                //    recyclerView.stopScroll();
//                    layoutManager.setSmoothScrollbarEnabled(false);
////
////                layoutManager = new LinearLayoutManager(getActivity()){
////                    @Override
////                    public boolean canScrollHorizontally() {
////                        return false;
////                    }
////                };
//             //       recyclerView.setLayoutManager(layoutManager);
//
//                }

//                if(dummyPosition==imageURL.size()-1){
//                    int sizelist = dummyPosition;
//                    while (sizelist > imageURL.size()) {
//                        if (imageURL.get(sizelist) != null) {
//                            imageURL.set(pos, imageURL.get(sizelist));
//                            imageURL.set(sizelist, null);
//                            break;
//                        }
//                        sizelist--;
//                    }
//                }

//                if(dummyPosition==0){
//                    for(int i=0;i<imageURL.size();i++){
//                        if (imageURL.get(i) != null) {
//                            imageURL.set(dummyPosition, imageURL.get(i));
//                            imageURL.set(i, null);
//                            break;
//                        }
//                    }
//                }

                //   if (!flag) {
                // if(imageURL.size()>)
                //    pos = pos / 2;
                //   pos = (pos/2) % imageURL.size();
                if (pos < imageURL.size()) {
                    if (imageURL.get(pos) == null) {
                        int sizelist = 0;
                        while (sizelist < imageURL.size()) {
                            if (imageURL.get(sizelist) != null) {
                                imageURL.set(pos, imageURL.get(sizelist));
                                imageURL.set(sizelist, null);
                                break;
                            }
                            sizelist++;
                        }
                    }
                } else {
                    int sizelist = 0;
                    while (sizelist < imageURL.size()) {
                        if (imageURL.get(sizelist) != null) {
                            imageURL.add(pos, imageURL.get(sizelist));
                            imageURL.set(sizelist, null);
                            //       imageURL.remove(sizelist);
                            //       pos = pos-1;
                            //     dummyPosition = dummyPosition-1;
                            break;
                        }
                        sizelist++;
                    }
                }

             //   if(imageURL.size()-1 == dummyPosition){
             //   }

//                    if(pos==imageURL.size()){
//                        int sizelist = dummyPosition;
//                        while (sizelist < imageURL.size()) {
//                            if (imageURL.get(sizelist) != null) {
//                                imageURL.add(pos, imageURL.get(sizelist));
//                                imageURL.add(sizelist, null);
//                                break;
//                            }
//                            sizelist--;
//                        }
//                    }


                TextView leave_num = (TextView) view.findViewById(R.id.leave_no);
                Button leave_btn = (Button) view.findViewById(R.id.leave_btn);
                TextView take_num = (TextView) view.findViewById(R.id.take_no);
                Button take_btn = (Button) view.findViewById(R.id.take_btn);
                ImageView itemImage = (ImageView) view.findViewById(R.id.itemImage);
                LinearLayout leaveIT = (LinearLayout) view.findViewById(R.id.itemleave);
                LinearLayout takeIT = (LinearLayout) view.findViewById(R.id.itemtake);
                TextView caption = (TextView) view.findViewById(R.id.caption_item);

//                Glide.with(getActivity()).load(R.mipmap.placeholder).asBitmap().placeholder(R.mipmap.placeholder).into(itemImage);
//                take_num.setText(String.valueOf(""));
//                leave_num.setText(String.valueOf(""));
//                caption.setText("No View Available");
                //   if (imageURL.toString() != null) {
                if (pos != dummyPosition && pos >= 0) {
                    axisArrayList.add(new Axis(dx, dy));
                    //   TextView textView = (TextView) child.findViewById(R.id.caption_item);

                    //      if(imageURL.get(pos)!=null) {
                    try {
                        if (imageURL.get(pos) != null) {
                            dummyPosition = pos;

//                            if (pos == dummyPosition + 1) {
//                                Toast.makeText(getActivity(), "+1", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(getActivity(), "-1", Toast.LENGTH_SHORT).show();
//                            }
                            //  pos = pos/2;
                         //   caption.setText(imageURL.get(pos).getCaption());
                            Glide.with(getActivity())
                                    .load(imageURL.get(pos).getItemImageURl()).asBitmap()
                                    .animate(R.anim.fade_in)
                                    .placeholder(R.mipmap.placeholder)
                                    .into(itemImage);
                            take_num.setText(String.valueOf(imageURL.get(pos).getTakeit_count()));
                            leave_num.setText(String.valueOf(imageURL.get(pos).getLeaveit_count()));
                            caption.setText(imageURL.get(pos).getCaption());

                            if (imageURL.get(pos) != null) {
                                imageURL.set(dummyPosition, null);
                            }
                            //  adapter1.notifyItemInserted(pos);
                      //      layoutManager.scrollToPosition(dummyPosition);
                            ///    adapter1.notifyItemInserted(dummyPosition+1);
                            ///   adapter1.notifyDataSetChanged();
                            //   adapter1.notifyItemRemoved(dummyPosition);
                            //    adapter1.notifyDataSetChanged();
                        } else {
                           // caption.setText("");
                            Glide.with(getActivity())
                                    .load(R.mipmap.placeholder).asBitmap()
                                    .placeholder(R.mipmap.placeholder)
                                    .animate(R.anim.fade_in)
                                    .into(itemImage);
                            take_num.setText(String.valueOf(""));
                            leave_num.setText(String.valueOf(""));
                            caption.setText("No View Available");
                            adapter1.notifyDataSetChanged();

                        }

                        //       }
                        //      }
                        //    }


                    } catch (Exception c) {
                        c.printStackTrace();
                      //  caption.setText("");
                        Glide.with(getActivity()).load(R.mipmap.placeholder)
                                .asBitmap()
                                .animate(R.anim.fade_in)
                                .placeholder(R.mipmap.placeholder)
                                .into(itemImage);
                        take_num.setText(String.valueOf(""));
                        leave_num.setText(String.valueOf(""));
                        caption.setText("No View Available");
                        adapter1.notifyDataSetChanged();

                    }
                }
//                    else{
//                        caption.setText("");
//                        Glide.with(getActivity()).load(R.mipmap.placeholder).asBitmap().placeholder(R.mipmap.placeholder).into(itemImage);
//                        take_num.setText(String.valueOf(""));
//                        leave_num.setText(String.valueOf(""));
//                        caption.setText("No View Available");
//                    }
                //  }
            }
            //  }
        });

        //    adapter1 = new TouristSpotCardAdapter(getActivity());


        //   scrollingLinearLayout.setSmoothScrollbarEnabled(false);

        //  my_recycler_view.setLayoutManager(layoutManager);


        //  my_recycler_view.setOnTouchListener(new RecyclerTouchListener());


//        firstVisibleInListview = layoutManager.findFirstVisibleItemPosition();
//        my_recycler_view.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                 //Stop only scrolling.
//                return rv.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING;
//            }
//        });

//        cardStackView = (CardStackView)view.findViewById(R.id.my_recycler_view);
//        cardStackView.setAdapter(adapter1);
//        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
//            @Override
//            public void onCardDragging(float percentX, float percentY) {
//                Log.d("CardStackView", "onCardDragging");
//            }
//
//            @Override
//            public void onCardSwiped(SwipeDirection direction) {
//                Log.d("CardStackView", "onCardSwiped: " + direction.toString());
//                Log.d("CardStackView", "topIndex: " + cardStackView.getTopIndex());
//                if (cardStackView.getTopIndex() == adapter1.getCount() - 5) {
//                    Log.d("CardStackView", "Paginate: " + cardStackView.getTopIndex());
//                 //   paginate();
//                }
//            }
//
//
//            @Override
//            public void onCardReversed() {
//                Log.d("CardStackView", "onCardReversed");
//            }
//
//            @Override
//            public void onCardMovedToOrigin() {
//                Log.d("CardStackView", "onCardMovedToOrigin");
//            }
//
//            @Override
//            public void onCardClicked(int index) {
//                Log.d("CardStackView", "onCardClicked: " + index);
//            }
//        });


//
//        my_recycler_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//     //           TextView caption = (TextView) recyclerView.findViewById(R.id.caption_item);
////                AppLogs.d("", caption.getText().toString());
//                int center = recyclerView.getWidth() / 2;
//                View centerView = recyclerView.findChildViewUnder(center, recyclerView.getTop());
//                position = recyclerView.getChildAdapterPosition(centerView);
//
//
//                //my_recycler_view.scrollToPosition(index);
//                //index--;
////                if(!flag){
////                    tempCaption = caption.getText().toString();
////                    for (ItemObject itemObject : imageURL) {
////                        if(itemObject.getCaption().equals(tempCaption)) {
////                            imageURL.remove(itemObject);
////                            break;
////                        }
////                    }
////                    adapter.notifyDataSetChanged();
////                flag = true;
////                }else{
////              //     if(tempCaption!=caption.getText().toString()){
////               //         flag = false;
////               //     }
////
////                }
//
//
//       //        position = position%imageURL.size();
//            if (position == counter) {
//
//               } else {
//                   counter = position;
////                if(dummyPosition==0) {
////                    dummyPosition = imageURL.size() / 2;
////               }
//                    if (position == dummyPosition + 1) {
//                        Toast.makeText(getActivity(), "Like"+position, Toast.LENGTH_SHORT).show();
//                               dummyPosition = position;
////                        //      }else if(position==dummyPosition--){
//////                        //      Toast.makeText(getActivity(),"Unlike",Toast.LENGTH_SHORT).show();
//////                        //
//                        int takit_count = 0;
//                        position = position-1;
//                        AppLogs.d(TAG, imageURL.get(position).getTakeit_count() + "");
//                        takit_count = imageURL.get(position).getTakeit_count() + 1;
//                        imageURL.get(position).setTakeit_count(takit_count);
//                        final int finalTakit_count = takit_count;
//                        FirebaseHandler.getInstance().getPostRef()
//                                .child(String.valueOf(imageURL.get(position).getItemID()))
//                                .child("takeit_count")
//                                .setValue(takit_count, new DatabaseReference.CompletionListener() {
//                                    @Override
//                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
////
//                                        //  ItemObject itemObject = new ItemObject(itemsList.get(i).getItemID(),itemsList.get(i).getItemImageURl(),true,false);
//                                        position = position-1;
//                                        FirebaseHandler.getInstance().getPostRef().child(String.valueOf(imageURL.get(position).getItemID())).child("take_it_check").setValue(true);
//                                        FirebaseHandler.getInstance().getUser_postRef().child(imageURL.get(position).getUserID()).child(imageURL.get(position).getItemID()).child("take_it_check").setValue(true);
//                                        FirebaseHandler.getInstance().getUser_postRef().child(imageURL.get(position).getUserID()).child(imageURL.get(position).getItemID()).child("takeit_count").setValue(finalTakit_count);
//
//                                  //      imageURL.remove(0);
//                                 //       position = position-1;
//
//                                    //    position = position-1;
//                                        FirebaseHandler.getInstance().getUser_leaveit_post()
//                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
//                                                .child("user-take-posts")
//                                                .child(imageURL.get(position).getItemID())
//                                                .setValue(new ItemObject(imageURL.get(position).getItemID(), imageURL.get(position).getItemImageURl(), true,
//                                                        imageURL.get(position).isLeave_it_check(), imageURL.get(position).getUserID(), imageURL.get(position).getCaption(),
//                                                        imageURL.get(position).getLeaveit_count(), imageURL.get(position).getTakeit_count(), imageURL.get(position).getCountry(),
//                                                        imageURL.get(position).getCan_see(), System.currentTimeMillis()), new DatabaseReference.CompletionListener() {
//
//                                                    @Override
//                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                          //              SectionListDataAdapter.itemsList.remove(position);
//                                        //                adapter.notifyItemRemoved(position-1);
//                                              //          position = position-1;
//                                                    //    imageURL.remove(position);
//                                                   //     adapter = new SectionListDataAdapter(getActivity(),imageURL);
//                                             //           adapter.SetPostion(position);
//                                                    //    recyclerView.setAdapter(adapter);
//                                              //          SectionListDataAdapter.itemsList.remove(position);
//                                                  }
//                                                });
//                                    }
//                                });
//
//                    } else if (position == dummyPosition - 1) {
//                        dummyPosition = position;
//
//                              Toast.makeText(getActivity(),"Unlike"+position,Toast.LENGTH_SHORT).show();
//                        int leave_it_count=0;
////                        //     Home_Fragment.getInstance().my_recycler_view.smoothScrollToPosition(i+1);
//                        position = position+1;
//                        AppLogs.d(TAG,imageURL.get(position).leaveit_count+"");
//                        //    Home_Fragment.getInstance().my_recycler_view.smoothScrollToPosition(i+1);
//                        AppLogs.d(TAG,imageURL.get(position).getLeaveit_count()+"");
//                        leave_it_count = imageURL.get(position).getLeaveit_count()+1;
//                        imageURL.get(position).setLeaveit_count(leave_it_count);
//                        final int finalLeave_it_count = leave_it_count;
//                        FirebaseHandler.getInstance().getPostRef()
//                                .child(String.valueOf(imageURL.get(position).getItemID()))
//                                .child("leaveit_count")
//                                .setValue(leave_it_count, new DatabaseReference.CompletionListener() {
//                                    @Override
//                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                                        //      ItemObject itemObject = new ItemObject(itemsList.get(i).getItemID(),itemsList.get(i).getItemImageURl(),true,false);
//
//                                        position = position+1;
//
//                                        FirebaseHandler.getInstance().getPostRef().child(String.valueOf(imageURL.get(position).getItemID())).child("leave_it_check").setValue(true);
//                                        FirebaseHandler.getInstance().getUser_postRef().child(imageURL.get(position).getUserID()).child(imageURL.get(position).getItemID()).child("leave_it_check").setValue(true);
//                                        FirebaseHandler.getInstance().getUser_postRef().child(imageURL.get(position).getUserID()).child(imageURL.get(position).getItemID()).child("leaveit_count").setValue(finalLeave_it_count);
//
//                                  //      imageURL.remove(0);
//
//                                        FirebaseHandler.getInstance().getUser_leaveit_post()
//                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
//                                                .child("user-leave-posts")
//                                                .child(imageURL.get(position).getItemID())
//                                                .setValue(new ItemObject(imageURL.get(position).getItemID(),
//                                                        imageURL.get(position).getItemImageURl(),imageURL.get(position).isTake_it_check(),
//                                                        true, imageURL.get(position).getUserID(),imageURL.get(position).getCaption(),
//                                                        imageURL.get(position).getLeaveit_count(),imageURL.get(position).getTakeit_count(),
//                                                        imageURL.get(position).getCountry(),imageURL.get(position).getCan_see(),System.currentTimeMillis()), new DatabaseReference.CompletionListener() {
//                                                    @Override
//                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                                  //      position = position+1;
//                                                 //       imageURL.remove(position);
//                                                  //      adapter = new SectionListDataAdapter(getActivity(),imageURL);
//                                                   //     recyclerView.setAdapter(adapter);
//                                                    }
//                                                });
//                                    }
//                                });
//
//                        //      Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
//                    }
//                }
////
////
//                   }
//
//
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//
//            }
//       });
        //setUpItemTouchHelper();
        //  setUpAnimationDecoratorHelper();
        //   my_recycler_view.setOnTouchListener(simpleItemTouchCallback);


        leave_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Leave It. ", Toast.LENGTH_SHORT).show();

                //    Home_Fragment.getInstance().my_recycler_view.smoothScrollToPosition(i--);
//                counterOne++;
//

//


            }
        });

        take_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Take it", Toast.LENGTH_SHORT).show();

//                int takit_count = 0;
                //   Home_Fragment.getInstance().my_recycler_view.smoothScrollToPosition(i++);
//                if (imageURL.size() > 0) {


//                }
            }
        });


        MainActivity.Uploadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Upload Image");
                alert.setMessage("Want to upload image..?");
                alert.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{
                                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    2);
                        } else {
                                   if(Build.VERSION.SDK_INT >20) {
                                      File imagesFolder = new File(Environment.getExternalStorageDirectory(), "Images");
                                     imagesFolder.mkdirs();
                                     File image = new File(imagesFolder.getPath(), "MyImage_.jpg");
                                    String fileName = "temp.jpg";
                                       ContentValues values = new ContentValues();
                                      values.put(MediaStore.Images.Media.TITLE, image.getAbsolutePath());
                                       mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                 }


                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                                 cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                });
                alert.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  mayRequestContacts();
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{
                                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    2);
                        } else {

                            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, SELECTED_PICTURE);
                        }
                    }
                });
                alert.create().show();


            }
        });


        FirebaseHandler.getInstance().getUser_privacy()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            if (dataSnapshot.getValue() != null) {
                                FilterItem filterItem = dataSnapshot.getValue(FilterItem.class);
                                filterItemObj.setCan_see(filterItem.getCan_see());
                                filterItemObj.setWant_see(filterItem.getWant_see());
                                //      FilterItem.getInstance(filterItem.getCan_see(),filterItem.getWant_see());
                            }
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        FirebaseHandler.getInstance().getPostRef()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        imageURL.clear();
                        if (dataSnapshot.getValue() != null) {
                            AppLogs.d(TAG, "" + dataSnapshot.getValue().toString());
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                ItemObject itemObject = data.getValue(ItemObject.class);
                                if (itemObject.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                                } else {
                                    if (UserModel.getInstanceIfNotNull() != null) {
                                        //        if (UserModel.getInstanceIfNotNull().getUser_country() != null) {
                                        //         if (!itemObject.getCan_see().equals("")) {
                                        //            if (filterItemObj != null) {
                                        //                 if (!filterItemObj.getWant_see().equals("")) {
                                        if (itemObject.getCan_see().equals(UserModel.getInstanceIfNotNull().getUser_country())) {
                                            if (itemObject.getCountry().equals(filterItemObj.getWant_see())) {
                                                imageURL.add(itemObject);
                                                placeHolderList.add("https://firebasestorage.googleapis.com/v0/b/vividways-1675b.appspot.com/o/profileImages%2Fuser.png?alt=media&token=1a586330-08ff-4832-b9c1-c85668e02e07");

                                                //   adapter1.add(itemObject);
                                                //   adapter1.notifyDataSetChanged();
                                                //            }
                                                //          }
                                                //      }
                                                //    }
                                            } else imageURL.add(itemObject);
                                            placeHolderList.add("https://firebasestorage.googleapis.com/v0/b/vividways-1675b.appspot.com/o/profileImages%2Fuser.png?alt=media&token=1a586330-08ff-4832-b9c1-c85668e02e07");

                                        } else
                                            imageURL.add(itemObject);
                                        placeHolderList.add("https://firebasestorage.googleapis.com/v0/b/vividways-1675b.appspot.com/o/profileImages%2Fuser.png?alt=media&token=1a586330-08ff-4832-b9c1-c85668e02e07");

                                    }
                                    ///   adapter.notifyDataSetChanged();
                                }
                            }
                            filterPost(imageURL);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //   populateAutoComplete();
            }
        } else {
            Toast.makeText(getActivity(), "Please Allow Storage ..", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean mayRequestContacts() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // getActivity() thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        2);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA},
                        2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
            }
        }
        return false;
    }


    private void filterPost(final ArrayList<ItemObject> imageURL) {
        FirebaseHandler.getInstance()
                .getUser_leaveit_post()
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            if (dataSnapshot.getValue() != null) {
                                AppLogs.d("Hello", dataSnapshot.getValue().toString());
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    for (DataSnapshot data_again : data.getChildren()) {
                                        ItemObject itemObject = data_again.getValue(ItemObject.class);
                                        for (int i = 0; i < imageURL.size(); i++) {
                                            if (imageURL.get(i).getItemID().equals(itemObject.getItemID())) {
                                                imageURL.remove(i);
                                                placeHolderList.remove(i);
                                                adapter1.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                }
                            } else {
                                adapter1.notifyDataSetChanged();
                            }
                        } else {
                            adapter1.notifyDataSetChanged();
                        }


                        layoutManager.scrollToPosition(placeHolderList.size());
                        dummyPosition = (imageURL.size() / 2) - 1;
                        //     adapter1.setSelection(0);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }


    public static Home_Fragment getInstance() {
        return home_fragment;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_OK) {

            //    if (requestCode == Crop.REQUEST_PICK && resultCode == getActivity().RESULT_OK) {

            //   }
            // else if (requestCode == Crop.REQUEST_CROP) {
            //     handleCrop(resultCode, data);
            //  }


            if (requestCode == 1888) {
                //for camera
//
//                if(Build.VERSION.SDK_INT <=19) {
//                    Intent intent = new Intent("com.android.camera.action.CROP");
//                    intent.setData(data.getData());
//                    intent.putExtra("crop", true);
//                    intent.putExtra("aspectX", 1);
//                    intent.putExtra("aspectY", 1);
//                    intent.putExtra("outputX", 96);
//                    intent.putExtra("outputY", 96);
//                    intent.putExtra("noFaceDetection", true);
//                    intent.putExtra("return-data", true);
//                    startActivityForResult(intent, 2);
//
//                }
                //     if(intent==null) {
                //          intent = data;
                //    }
//                String[] projection = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getActivity().getContentResolver().query(data.getData(), projection, null, null, null);
//                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                imgPath = cursor.getString(column_index_data);
//                cursor.close();

                if (Build.VERSION.SDK_INT <= 19) {
                    handleCrop(resultCode, data, mCapturedImageURI);
                } else {
                    if (data == null) {
                        String[] projectionn = {
//                                MediaStore.Images.Thumbnails._ID,  // The columns we want
//                                MediaStore.Images.Thumbnails.IMAGE_ID,
//                                MediaStore.Images.Thumbnails.KIND,
                                MediaStore.Images.Thumbnails.DATA};
                        String selection = MediaStore.Images.Thumbnails.KIND + "=" + // Select only mini's
                                MediaStore.Images.Thumbnails.MINI_KIND;

                        String sort = MediaStore.Images.Thumbnails._ID + " DESC";

//At the moment, this is a bit of a hack, as I'm returning ALL images, and just taking the latest one. There is a better way to narrow this down I think with a WHERE clause which is currently the selection variable
                        Cursor myCursor = getActivity().getContentResolver().query(mCapturedImageURI, projectionn, null, null, null);

                        long imageId = 0l;
                        long thumbnailImageId = 0l;
                        String thumbnailPath = "";

                        try {
                            myCursor.moveToFirst();
//                            imageId = myCursor.getLong(myCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
 //                           thumbnailImageId = myCursor.getLong(myCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID));
//                            thumbnailPath = myCursor.getString(myCursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                        } finally {
                            myCursor.close();
                        }

                        //Create new Cursor to obtain the file Path for the large image

//                        String[] largeFileProjection = {
//                                MediaStore.Images.ImageColumns._ID,
//                                MediaStore.Images.ImageColumns.DATA
//                        };
//
//                        String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
//                        myCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, largeFileProjection, null, null, largeFileSort);
//                        String largeImagePath = "";
//
//                        try {
//                            myCursor.moveToFirst();
//
////This will actually give yo uthe file path location of the image.
//                            largeImagePath = myCursor.getString(myCursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
//                        } finally {
//                            myCursor.close();
//                        }
//                        // These are the two URI's you'll be interested in. They give you a handle to the actual images
//                        Uri uriLargeImage = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(imageId));
//                        Uri uriThumbnailImage = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, String.valueOf(thumbnailImageId));
                        beginCrop(mCapturedImageURI);
                    } else {
                        beginCrop(data.getData());
                    }
                }

            } else if (requestCode == 9162) {
                //for gallery
                //   beginCrop(data.getData());

                mCapturedImageURI = data.getData();
                String[] imgHolder = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(mCapturedImageURI, imgHolder, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(imgHolder[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();


                if (Build.VERSION.SDK_INT > 19) {
                    beginCrop(data.getData());
                } else {
                    handleCrop(resultCode, data, mCapturedImageURI);
                }
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data, mCapturedImageURI);
            }

        } else {
            Toast.makeText(getActivity(), "Nothing Selected !", Toast.LENGTH_LONG).show();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(ImageView imgPath) {
        try {
            //   File fileRef = new File(imgPath);


            final Date date = new Date(System.currentTimeMillis());
            //   final String filenew = fileRef.getName();
            //    AppLogs.d("fileNewName", filenew);
            //    int dot = filenew.lastIndexOf('.');
            //     String base = (dot == -1) ? filenew : filenew.substring(0, dot);
            //     final String extension = (dot == -1) ? "" : filenew.substring(dot + 1);
            //     AppLogs.d("extensionsss", extension);
            mProgressDialog = ProgressDialog.show(getActivity(), "Uploading Image", "loading...", true, false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            UploadTask uploadTask;
            // Uri file = Uri.fromFile(new File(imgPath));
            imageRef = folderRef.child(String.valueOf(date) + ".png");

            imgPath.setDrawingCacheEnabled(true);
            imgPath.buildDrawingCache();
            Bitmap b = imgPath.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            //  UploadTask uploadTask = ref.child(id + ".png").putBytes(bytes);


            uploadTask = imageRef.putBytes(bytes);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(getActivity(), "UPLOAD FAILD", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                    Log.e("Image ka URL", "" + downloadUrl);
                    downloadURL = downloadUrl;
                    mProgressDialog.dismiss();
                    final DatabaseReference ref = firebase.child("user-post").child(mAuth.getCurrentUser().getUid()).push();
                    if (FilterItem.getInstanceIfNotNull() == null) {
                        FilterItem.getInstance("", "");
                    }


                    final ItemObject itemObject = new ItemObject(ref.getKey().toString(), downloadUrl, false, false, mAuth.getCurrentUser().getUid()
                            , string_caption, 0, 0, UserModel.getInstanceIfNotNull().getUser_country(),
                            FilterItem.getInstanceIfNotNull().getCan_see(), System.currentTimeMillis());
                    ref.setValue(itemObject, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            firebase.child("post").child(String.valueOf(ref.getKey().toString())).setValue(itemObject);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Uri beginCrop(Uri source) {

        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getActivity(), Home_Fragment.this);
//        Crop.of(source,destination).withMaxSize(100,100).start(getActivity(), TabFragment1.this);

        return destination;
    }

    private void handleCrop(int resultCode, final Intent result, Uri mCapturedImageURI) {

        // imgPath = null;
        if (resultCode == getActivity().RESULT_OK) {
            Bitmap bitmap = null;
            try {
                if (Build.VERSION.SDK_INT <= 19) {
                    //     bitmap =(Bitmap) mCapturedImageURI.
                    //   InputStream image_stream =getActivity().getContentResolver().openInputStream(mCapturedImageURI);

                    //   bitmap= BitmapFactory.decodeStream(image_stream);
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mCapturedImageURI);

                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Crop.getOutput(result));
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Want to Upload Image or not ?");
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View view1 = inflater.inflate(R.layout.image_view_alert, null);
                final ImageView alertImageView = (ImageView) view1.findViewById(R.id.imageView_Alert);
                final EditText editText_caption = (EditText) view1.findViewById(R.id.caption);
                //     if (getActivity() != null) {
                //            mCapturedImageURI = Crop.getOutput(result);
//                String[] imgHolder = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getActivity().getContentResolver().query(mCapturedImageURI, imgHolder, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(imgHolder[0]);
//                imgPath = cursor.getString(columnIndex);
//                cursor.close();
                //      if(Build.VERSION.SDK_INT <=19){
                //   bitmap = decodeFile(imgPath);
                //      bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                //            bitmap.getWidth(), bitmap.getHeight(), , true);
                //     bitmap = Bitmap.createScaledBitmap(myPictureBitmap, editText_caption.getWidth(),editText_caption.getHeight(),true);
                //      }
                alertImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                alertImageView.setImageBitmap(bitmap);
                alertImageView.invalidate();
                //   alertImageView.setImageURI(mCapturedImageURI);
                //    Glide.with(getActivity()).load(mCapturedImageURI).into(alertImageView);
                //    alertImageView.setImageURI(uri);
                //    }
                builder.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        string_caption = editText_caption.getText().toString();
                        uploadImage(alertImageView);


                    }
                });
                builder.setNeutralButton("Cancel", null);
                builder.setView(view1);
                builder.create().show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void calculatePositionAndScroll(RecyclerView recyclerView) {
        int expectedPosition = Math.round((allPixels + padding - firstItemWidth) / itemWidth);
        // Special cases for the padding items
        if (expectedPosition == -1) {
            expectedPosition = 0;
        } else if (expectedPosition >= recyclerView.getAdapter().getItemCount() - 2) {
            expectedPosition--;
        }
        scrollListToPosition(recyclerView, expectedPosition);
    }

    private void scrollListToPosition(RecyclerView recyclerView, int expectedPosition) {
        float targetScrollPos = expectedPosition * itemWidth + firstItemWidth - padding;
        float missingPx = targetScrollPos - allPixels;
        if (missingPx != 0) {
            recyclerView.smoothScrollBy((int) missingPx, 0);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("param", mCapturedImageURI);
        outState.putParcelable("paramm", intent);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mCapturedImageURI = savedInstanceState.getParcelable("param");
            intent = savedInstanceState.getParcelable("paramm");
        }
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Camera getCameraInstance() {
        Camera c = null;

        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d("cam", "Camera is not available - in use or does not exist");
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void paginate() {
        //   cardStackView.setPaginationReserved();
        ///   adapter..addAll(createTouristSpots());
        //  adapter.notifyDataSetChanged();
    }


}
