package com.example.faiz.vividways.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faiz.vividways.Adapters.Disco_Can_Adapter;
import com.example.faiz.vividways.Adapters.Disco_Want_Adapter;
import com.example.faiz.vividways.Models.FilterItem;
import com.example.faiz.vividways.Models.ItemObject;
import com.example.faiz.vividways.R;
import com.example.faiz.vividways.Utils.AppLogs;
import com.example.faiz.vividways.Utils.FirebaseHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Faiz on 7/25/2017.
 */

public class Discovery_Fragment extends android.support.v4.app.Fragment {


    public View view;
    public Button btn1_cansee, btn2_cansee, btn3_cansee, btn4_cansee, btn5_cansee, btn6_cansee;
    public Button btn1_wantSee, btn2_wantSee, btn3_wantSee, btn4_wantSee, btn5_wantSee, btn6_wantSee;
    public Button done_btn;
    public boolean flag_1 = false, flag_2 = false, flag_3 = false, flag_4 = false, flag_5 = false, flag_6 = false;
    public boolean check_1 = false, check_2 = false, check_3 = false, check_4 = false, check_5 = false, check_6 = false;
    public String cansee = "", wantPost = "";
    public ArrayList<ItemObject> itemObjectArrayList;
    public ListView ListviewCan, ListviewWant,sideListView;
    public int pos = 222222222;
    public ArrayList<Boolean> selectedList_can;
    public ArrayList<Boolean> selectedList_want;
    LinearLayout indexLayout;
     ArrayList<String> countries;
    Map<String, Integer> mapIndex;
    final String[] alphabaticalList = { "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.discovery_layout, null);
        itemObjectArrayList = new ArrayList<>();
        selectedList_can = new ArrayList<>();
        selectedList_want = new ArrayList<>();
        Locale[] locale = Locale.getAvailableLocales();
        countries = new ArrayList<String>();
        String country;

      //   indexLayout = (LinearLayout) view.findViewById(R.id.side_index);

        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
                selectedList_can.add(false);
                selectedList_want.add(false);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);


        final Disco_Can_Adapter adapter_can_country = new Disco_Can_Adapter(countries, getActivity());
        final Disco_Want_Adapter adapter_want_country = new Disco_Want_Adapter(countries, getActivity());

        ListviewCan = (ListView) view.findViewById(R.id.gridc_countries);

        ListviewCan.setAdapter(adapter_can_country);
        ListviewCan.setFastScrollEnabled(true);
      //  setListViewHeightBasedOnChildren(ListviewCan);
        ListviewWant = (ListView) view.findViewById(R.id.gridw_countries);
        ListviewWant.setAdapter(adapter_want_country);
        ListviewWant.setFastScrollEnabled(true);
      //  setListViewHeightBasedOnChildren(ListviewWant);
//        btn1_cansee = (Button) view.findViewById(R.id.who_see1);
//        btn2_cansee = (Button) view.findViewById(R.id.who_see2);
//        btn3_cansee = (Button) view.findViewById(R.id.who_see3);
//        btn4_cansee = (Button) view.findViewById(R.id.who_see4);
//        btn5_cansee = (Button) view.findViewById(R.id.who_see5);
//        btn6_cansee = (Button) view.findViewById(R.id.who_see6);
//
        done_btn = (Button) view.findViewById(R.id.done);


        ListviewWant.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        ListviewCan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        ListviewCan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //for the repeated time

                if (selectedList_can.get(position)) {
                    selectedList_can.set(position, false);
                    adapter_can_country.setSelect(selectedList_can);
                    adapter_can_country.notifyDataSetChanged();
                } else {
                    // first time selection
                    selectedList_can.set(position, true);
                    adapter_can_country.setSelect(selectedList_can);
                    adapter_can_country.notifyDataSetChanged();
                }


            }
        });

        ListviewWant.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //       Toast.makeText(getActivity(),"countries Want Post:"+countries.get(position),Toast.LENGTH_SHORT).show();

                //for the repeated time

                if (selectedList_want.get(position)) {
                    selectedList_want.set(position, false);
                    adapter_want_country.setSelect(selectedList_want);
                    adapter_want_country.notifyDataSetChanged();
                } else {
                    // first time selection
                    selectedList_want.set(position, true);
                    adapter_want_country.setSelect(selectedList_want);
                    adapter_want_country.notifyDataSetChanged();
                }

            }
        });


//
//        btn1_wantSee = (Button) view.findViewById(R.id.want_see1);
//        btn2_wantSee = (Button) view.findViewById(R.id.want_see2);
//        btn3_wantSee = (Button) view.findViewById(R.id.want_see3);
//        btn4_wantSee = (Button) view.findViewById(R.id.want_see4);
//        btn5_wantSee = (Button) view.findViewById(R.id.want_see5);
//        btn6_wantSee = (Button) view.findViewById(R.id.want_see6);
//
//        btn1_cansee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                flag_2 = false;
//                flag_3 = false;
//                flag_4 = false;
//                flag_5 = false;
//                flag_6 = false;
//
//
//                if (!flag_1) {
//                    cansee = btn1_cansee.getText().toString();
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn1_cansee.setTextColor(Color.WHITE);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_1 = true;
//                } else {
//                    cansee = "";
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_1 = false;
//                }
//
//            }
//        });
//
//        btn2_cansee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                flag_1 = false;
//                flag_3 = false;
//                flag_4 = false;
//                flag_5 = false;
//                flag_6 = false;
//
//
//                if (!flag_2) {
//
//                    cansee = btn2_cansee.getText().toString();
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn2_cansee.setTextColor(Color.WHITE);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_2 = true;
//                } else {
//                    cansee = "";
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_2 = false;
//                }
//            }
//        });
//        btn3_cansee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                flag_1 = false;
//                flag_2 = false;
//                flag_4 = false;
//                flag_5 = false;
//                flag_6 = false;
//
//
//                if (!flag_3) {
//                    cansee = btn3_cansee.getText().toString();
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn3_cansee.setTextColor(Color.WHITE);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_3 = true;
//                } else {
//                    cansee = "";
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_3 = false;
//                }
//            }
//        });
//        btn4_cansee.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//                flag_1 = false;
//                flag_2 = false;
//                flag_3 = false;
//                flag_5 = false;
//                flag_6 = false;
//
//
//                if (!flag_4) {
//                    cansee = btn4_cansee.getText().toString();
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn4_cansee.setTextColor(Color.WHITE);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_4 = true;
//                } else {
//                    cansee = "";
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_4 = false;
//                }
//            }
//        });
//        btn5_cansee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                flag_1 = false;
//                flag_2 = false;
//                flag_3 = false;
//                flag_4 = false;
//                flag_6 = false;
//
//
//                if (!flag_5) {
//                    cansee = btn5_cansee.getText().toString();
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn5_cansee.setTextColor(Color.WHITE);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_5 = true;
//                } else {
//                    cansee = "";
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_5 = false;
//                }
//            }
//        });
//        btn6_cansee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                flag_1 = false;
//                flag_2 = false;
//                flag_3 = false;
//                flag_4 = false;
//                flag_5 = false;
//
//
//                if (!flag_6) {
//                    cansee = btn6_cansee.getText().toString();
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn6_cansee.setTextColor(Color.WHITE);
//                    flag_6 = true;
//                } else {
//                    cansee = "";
//                    btn1_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_cansee.setTextColor(Color.BLACK);
//                    btn2_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_cansee.setTextColor(Color.BLACK);
//                    btn3_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_cansee.setTextColor(Color.BLACK);
//                    btn4_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_cansee.setTextColor(Color.BLACK);
//                    btn5_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_cansee.setTextColor(Color.BLACK);
//                    btn6_cansee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_cansee.setTextColor(Color.BLACK);
//                    flag_6 = false;
//                }
//            }
//        });
//
//
//        btn1_wantSee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                check_2 = false;
//                check_3 = false;
//                check_4 = false;
//                check_5 = false;
//                check_6 = false;
//
//
//                if (!check_1) {
//                    wantPost = btn1_wantSee.getText().toString();
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn1_wantSee.setTextColor(Color.WHITE);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_1 = true;
//                } else {
//                    wantPost = "";
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_1 = false;
//                }
//
//            }
//        });
//
//        btn2_wantSee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                check_1 = false;
//                check_3 = false;
//                check_4 = false;
//                check_5 = false;
//                check_6 = false;
//
//
//                if (!check_2) {
//                    wantPost = btn2_wantSee.getText().toString();
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn2_wantSee.setTextColor(Color.WHITE);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_2 = true;
//                } else {
//                    wantPost = "";
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_2 = false;
//                }
//            }
//        });
//        btn3_wantSee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                check_1 = false;
//                check_2 = false;
//                check_4 = false;
//                check_5 = false;
//                check_6 = false;
//
//
//                if (!check_3) {
//                    wantPost = btn3_wantSee.getText().toString();
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn3_wantSee.setTextColor(Color.WHITE);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_3 = true;
//                } else {
//                    wantPost = "";
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_3 = false;
//                }
//            }
//        });
//        btn4_wantSee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                check_1 = false;
//                check_2 = false;
//                check_3 = false;
//                check_5 = false;
//                check_6 = false;
//
//
//                if (!check_4) {
//                    wantPost = btn4_wantSee.getText().toString();
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn4_wantSee.setTextColor(Color.WHITE);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_4 = true;
//                } else {
//                    wantPost = "";
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_4 = false;
//                }
//            }
//        });
//        btn5_wantSee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                check_1 = false;
//                check_2 = false;
//                check_3 = false;
//                check_4 = false;
//                check_6 = false;
//
//
//                if (!check_5) {
//                    wantPost = btn5_wantSee.getText().toString();
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn5_wantSee.setTextColor(Color.WHITE);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_5 = true;
//                } else {
//                    wantPost = "";
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_5 = false;
//                }
//            }
//        });
//        btn6_wantSee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                check_1 = false;
//                check_2 = false;
//                check_3 = false;
//                check_4 = false;
//                check_5 = false;
//
//
//                if (!check_6) {
//                    wantPost = btn6_wantSee.getText().toString();
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges_r));
//                    btn6_wantSee.setTextColor(Color.WHITE);
//                    check_6 = true;
//                } else {
//                    wantPost = "";
//                    btn1_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn1_wantSee.setTextColor(Color.BLACK);
//                    btn2_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn2_wantSee.setTextColor(Color.BLACK);
//                    btn3_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn3_wantSee.setTextColor(Color.BLACK);
//                    btn4_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn4_wantSee.setTextColor(Color.BLACK);
//                    btn5_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn5_wantSee.setTextColor(Color.BLACK);
//                    btn6_wantSee.setBackgroundDrawable(getResources().getDrawable(R.drawable.dis_pref_button_edges));
//                    btn6_wantSee.setTextColor(Color.BLACK);
//                    check_6 = false;
//                }
//            }
//        });

       // getIndexList(countries);

       // displayIndex();

        done_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                FirebaseHandler.getInstance().getUser_privacy()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(new FilterItem(cansee, wantPost));


                final DatabaseReference reference =
                        FirebaseHandler.getInstance().getUser_postRef()
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            if (dataSnapshot.getValue() != null) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    ItemObject itemObject = data.getValue(ItemObject.class);
                                    itemObjectArrayList.add(itemObject);

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                Query query = FirebaseHandler.getInstance().getPostRef().orderByChild("userID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            if (dataSnapshot.getValue() != null) {
                                AppLogs.d("TAG_SNAP", dataSnapshot.getValue().toString() + "");
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    FirebaseHandler.getInstance().getPostRef().child(snap.getKey())
                                            .child("can_see")
                                            .setValue(cansee);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Query query1 = FirebaseHandler.getInstance().getUser_postRef()
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .orderByChild("userID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            if (dataSnapshot.getValue() != null) {
                                AppLogs.d("TAG_SNAP1", dataSnapshot.getValue().toString() + "");
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    FirebaseHandler.getInstance().getUser_postRef()
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child(snap.getKey())
                                            .child("can_see")
                                            .setValue(cansee);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Bundle bundle = new Bundle();
                bundle.putString("cansee", cansee);
                bundle.putString("wantpost", wantPost);


                Home_Fragment home_fragment = new Home_Fragment();
                home_fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                transaction.replace(R.id.fragment_container, home_fragment);
                transaction.commit();
            }
        });


        return view;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) ;
        listView.setLayoutParams(params);
    }




}
