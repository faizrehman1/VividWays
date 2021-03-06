package com.example.faiz.vividways.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.faiz.vividways.Models.ItemObject;
import com.example.faiz.vividways.R;
import com.example.faiz.vividways.UI.FullImageActivity;
import com.example.faiz.vividways.Utils.AppLogs;

import java.io.ByteArrayOutputStream;

public class TouristSpotCardAdapter extends ArrayAdapter<ItemObject> {

    public Context mContext;

    public TouristSpotCardAdapter(@NonNull Context context) {
        super(context, 0);
            this.mContext = context;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        final ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_view, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        final ItemObject singleItem;
        singleItem =getItem(position);


        //   Home_Fragment.getInstance().position = i;


//        holder.tvTitle.setBackgroundResource(R.mipmap.hanger_img);
        AppLogs.e("Image Ka URL", singleItem.getItemImageURl());


        holder.takeIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Take IT", Toast.LENGTH_SHORT).show();
            //    openListFragment(mContext, true, singleItem, 0);

            }
        });

        holder.leaveIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Leave IT", Toast.LENGTH_SHORT).show();
              //  openListFragment(mContext, false, singleItem, 0);

            }
        });


//        holder.take_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//
//        holder.leave_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int leave_it_count=0;
//           //     Home_Fragment.getInstance().my_recycler_view.smoothScrollToPosition(i+1);
//                AppLogs.d(TAG,itemsList.get(i).leaveit_count+"");
//                Home_Fragment.getInstance().my_recycler_view.smoothScrollToPosition(i+1);
//                AppLogs.d(TAG,itemsList.get(i).getLeaveit_count()+"");
//                leave_it_count = itemsList.get(i).getLeaveit_count()+1;
//                itemsList.get(i).setLeaveit_count(leave_it_count);
//                final int finalLeave_it_count = leave_it_count;
//                FirebaseHandler.getInstance().getPostRef()
//                        .child(String.valueOf(itemsList.get(i).getItemID()))
//                        .child("leaveit_count")
//                        .setValue(leave_it_count, new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                          //      ItemObject itemObject = new ItemObject(itemsList.get(i).getItemID(),itemsList.get(i).getItemImageURl(),true,false);
//                                FirebaseHandler.getInstance().getPostRef().child(String.valueOf(itemsList.get(i).getItemID())).child("leave_it_check").setValue(true);
//                                FirebaseHandler.getInstance().getUser_postRef().child(itemsList.get(i).getUserID()).child(itemsList.get(i).getItemID()).child("leave_it_check").setValue(true);
//                                FirebaseHandler.getInstance().getUser_postRef().child(itemsList.get(i).getUserID()).child(itemsList.get(i).getItemID()).child("leaveit_count").setValue(finalLeave_it_count);
//
//                                FirebaseHandler.getInstance().getUser_leaveit_post()
//                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString())
//                                        .child("user-leave-posts")
//                                        .child(itemsList.get(i).getItemID())
//                                        .setValue(new ItemObject(itemsList.get(i).getItemID(),
//                                                itemsList.get(i).getItemImageURl(),itemsList.get(i).isTake_it_check(),
//                                                true, itemsList.get(i).getUserID(),itemsList.get(i).getCaption(),
//                                                itemsList.get(i).getLeaveit_count(),itemsList.get(i).getTakeit_count(),
//                                                itemsList.get(i).getCountry(),itemsList.get(i).getCan_see(),System.currentTimeMillis()), new DatabaseReference.CompletionListener() {
//                                            @Override
//                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                            }
//                                        });
//                            }
//                        });
//
//
//            }
//        });


        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //     BitmapDrawable drawable = (BitmapDrawable) holder.itemImage.getDrawable();
                //     Bitmap bitmap = drawable.getBitmap();
                //     Bitmap bmap = holder.itemImage.getDrawingCache();
                //       Bitmap bmp = ((GlideBitmapDrawable)holder.itemImage.getDrawable().getCurrent()).getBitmap();
                Bitmap bm = ((BitmapDrawable) holder.itemImage.getDrawable()).getBitmap();
                //      Bitmap bitmap = ((BitmapDrawable)holder.wallPaperImageView.getDrawable()).getBitmap();
                //       Bitmap bimap = ((BitmapDrawable)holder.itemImage.getDrawable()).getBitmap();
                //    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                //  View view = ((MainActivity) mContext).getLayoutInflater().inflate(R.layout.full_image_view,null);
                //   LayoutInflater inflater = LayoutInflater.from(mContext);
                //   View view = inflater.inflate(R.layout.full_image_view, null);
                //   ImageView full_img = (ImageView) view.findViewById(R.id.full_img);
                //  full_img.setBackground(bm);
                //  full_img.setImageBitmap(bm);

                //  builder.setView(view);
                //    builder.create().show();


//                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//                editor = sharedPreferences.edit();
//           //     editor.clear();
//                SharedPreferences preferences = mContext.getSharedPreferences("img", Context.MODE_PRIVATE);
//                preferences.edit().putString("img",bm).apply();
//                editor.commit();
                //  String s= createImageFromBitmap(bm);

                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    Intent intent = new Intent(mContext, FullImageActivity.class);
                    intent.putExtra("img", singleItem);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //      Bundle bundle = new Bundle();
                //      bundle.putParcelable("img",bm);
                //    Full_Image_Fragment statistic_fragment = new Full_Image_Fragment();
                //    statistic_fragment.setArguments(bundle);
                //   FragmentTransaction ft = ((MainActivity) mContext).getSupportFragmentManager()
                //          .beginTransaction();
                //  mainActivity.Uploadbutton.setVisibility(View.GONE);
                //   mainActivity.getSupportActionBar().hide();
//                mainActivity.appbar_TextView.setText("Statistic");
//                MainActivity.back_image.setVisibility(View.VISIBLE);
//                MainActivity.delete_image.setVisibility(View.VISIBLE);
//                MainActivity.report_image.setVisibility(View.VISIBLE);
                //  ft.replace(R.id.fragment_container,statistic_fragment);
                //  ft.addToBackStack("");
                //  ft.commit();
            }
        });


        Glide.with(mContext).load(singleItem.getItemImageURl()).asBitmap().placeholder(R.mipmap.placeholder).into(holder.itemImage);
        holder.take_num.setText(String.valueOf(singleItem.getTakeit_count()));
        holder.leave_num.setText(String.valueOf(singleItem.getLeaveit_count()));
        holder.caption.setText(singleItem.getCaption());
       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
        //}

        return contentView;
    }

    private static class ViewHolder {

        protected CardView tvTitle;
        protected LinearLayout leaveIT,takeIT;
        protected ImageView itemImage;
        protected Button leave_btn,take_btn;
        private TextView leave_num,take_num,caption;

        public ViewHolder(View view) {

            this.leave_num = (TextView)view.findViewById(R.id.leave_no);
            this.leave_btn = (Button)view.findViewById(R.id.leave_btn);
            this.take_num = (TextView)view.findViewById(R.id.take_no);
            this.take_btn = (Button)view.findViewById(R.id.take_btn);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);
            this.leaveIT =(LinearLayout)view.findViewById(R.id.itemleave);
            this.takeIT =(LinearLayout)view.findViewById(R.id.itemtake);
            this.caption = (TextView)view.findViewById(R.id.caption_item);
        }
    }

}

