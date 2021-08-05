/*
  ClassName: AdapterCallHistory.java
  Project: VeSoToday
  author  huy.tran@1byte.com
  Created Date: 2018-06-05
  Description: class AdapterCallHistory used to customize the adapter for the RecyclerView of the "CallHistory.class"
  History:2018-10-08
  Copyright © 2018 Jexpa LLC. All rights reserved.
 */

package com.onebyte.doveso.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onebyte.doveso.R;
import com.onebyte.doveso.model.PrizeResults;

import java.util.List;

import static com.onebyte.doveso.api.ApiMethod.getDayOfMonth;
import static com.onebyte.doveso.contants.Global.DB;
import static com.onebyte.doveso.contants.Global.G_1;
import static com.onebyte.doveso.contants.Global.G_2;
import static com.onebyte.doveso.contants.Global.G_3;
import static com.onebyte.doveso.contants.Global.G_4;
import static com.onebyte.doveso.contants.Global.G_5;
import static com.onebyte.doveso.contants.Global.G_6;
import static com.onebyte.doveso.contants.Global.G_7;
import static com.onebyte.doveso.contants.Global.G_8;
import static com.onebyte.doveso.contants.Global.G_DB;

public class AdapterDetailResultsLotteryMB extends RecyclerView.Adapter<AdapterDetailResultsLotteryMB.ViewHolder> {

    private Activity mActivity;
    private static List<PrizeResults> mDataSet;
    private TextView txt_tbr_Prize_Name, txt_tbr_Results_Province1, txt_tbr_Results_Province2,
            txt_tbr_Results_Province3, txt_tbr_Results_Province4;
    private final String SPLITSPACE = " - ";
    private LinearLayout lnl_Results_Lottery;
    boolean checkMB;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      /*  private TextView txt_tbr_Prize_Name, txt_tbr_Results_Province1, txt_tbr_Results_Province2,
                txt_tbr_Results_Province3, txt_tbr_Results_Province4;*/
        View mView;

        ViewHolder(View v) {
            super(v);
            lnl_Results_Lottery = v.findViewById(R.id.lnl_Results_Lottery);
            txt_tbr_Prize_Name = v.findViewById(R.id.txt_tbr_Prize_Name);
            txt_tbr_Results_Province1 = v.findViewById(R.id.txt_tbr_Results_Province1);
            txt_tbr_Results_Province2 = v.findViewById(R.id.txt_tbr_Results_Province2);
            txt_tbr_Results_Province3 = v.findViewById(R.id.txt_tbr_Results_Province3);
            txt_tbr_Results_Province4 = v.findViewById(R.id.txt_tbr_Results_Province4);
            mView = v;
            lnl_Results_Lottery.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // xử lý sự kiện ở đây khi người dùng click vào item
        }
    }

    public AdapterDetailResultsLotteryMB(Activity activity, List<PrizeResults> myDataSet, boolean checkMB) {
        mActivity = activity;
        mDataSet = myDataSet;
        Log.d("CheckHienThi", "mDataSet = " + mDataSet.size());
        this.checkMB = checkMB;
    }

    @NonNull
    @Override
    public AdapterDetailResultsLotteryMB.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rcv_detail_results_mien_bac, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrizeResults prizeResults = mDataSet.get(position);

        try {
            Log.d("CheckHienThi", "prizeResults  onBindViewHolder = " + prizeResults.getResultsProvince1());
            if(prizeResults != null && prizeResults.getSizeProvince() != 0)
            {
               /* setColorDay(txt_tbr_Prize_Name.getText().toString()); */// thêm màu cho các textView khi nó là cột tên thứ và tên tỉnh
                if(prizeResults.getSizeProvince() == 1) // xuất hiện ở kết quả xổ số miền bắc và kết quả của 1 tỉnh nào đó
                {
                    //holder.txt_tbr_Prize_Name.setText(prizeResults.getPrize());
                   /* txt_tbr_Prize_Name.setText(prizeResults.getPrize());
                    txt_tbr_Results_Province1.setText(prizeResults.getResultsProvince1().replace(SPLITSPACE,"\n"));
                    txt_tbr_Results_Province2.setVisibility(View.GONE);
                    txt_tbr_Results_Province3.setVisibility(View.GONE);
                    txt_tbr_Results_Province4.setVisibility(View.GONE);*/
                    String day = prizeResults.getPrize();
                    if(prizeResults.getPrize().length()>6)
                    {
                        txt_tbr_Prize_Name.setText(getDayOfMonth(prizeResults.getPrize(), true));
                        day = getDayOfMonth(prizeResults.getPrize(), true);
                    }
                    if(checkMB)
                    {
                        setDataForItemMB(day, prizeResults);
                        setColorDay(day); // thêm màu cho các textView khi nó là cột tên thứ và tên tỉnh
                    }else {
                        setDataForItemMN(day, prizeResults);
                        setColorDay(day); // thêm màu cho các textView khi nó là cột tên thứ và tên tỉnh
                    }

                }
            }
        }
        catch (Exception e)
        {
            Log.d("CheckHienThi", "Exception = " +  e.getMessage());
            e.getMessage();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDataForItemMB(String txtPrize, PrizeResults prizeResults)
    {
        if(txtPrize.equals("G.1") || txtPrize.equals("ĐB") || txtPrize.contains("T") || txtPrize.contains("CN"))
        {
            txt_tbr_Prize_Name.setText(txtPrize);
            txt_tbr_Results_Province1.setText(prizeResults.getResultsProvince1().replace(SPLITSPACE,"\n"));
            txt_tbr_Results_Province2.setVisibility(View.GONE);
            txt_tbr_Results_Province3.setVisibility(View.GONE);
            txt_tbr_Results_Province4.setVisibility(View.GONE);

        }else if(txtPrize.equals(G_7))
        {
            txt_tbr_Prize_Name.setText(txtPrize);
            Log.d("G&&&", prizeResults.getResultsProvince1());
            String [] splitG_7 = prizeResults.getResultsProvince1().split(SPLITSPACE);
            txt_tbr_Results_Province1.setText(splitG_7[0]);
            txt_tbr_Results_Province2.setText(splitG_7[1]);
            txt_tbr_Results_Province3.setText(splitG_7[2]);
            txt_tbr_Results_Province4.setText(splitG_7[3]);
        }
        else if(txtPrize.equals(G_6))
        {
            txt_tbr_Prize_Name.setText(txtPrize);
            String [] splitG_6 = prizeResults.getResultsProvince1().split(SPLITSPACE);
            txt_tbr_Results_Province1.setText(splitG_6[0]);
            txt_tbr_Results_Province2.setText(splitG_6[1]);
            txt_tbr_Results_Province3.setText(splitG_6[2]);
            txt_tbr_Results_Province4.setVisibility(View.GONE);
        }
        else if(txtPrize.equals(G_5) || txtPrize.equals(G_3))
        {
            txt_tbr_Prize_Name.setText(txtPrize);
            String [] splitG_5 = prizeResults.getResultsProvince1().split(SPLITSPACE);
            txt_tbr_Results_Province1.setText(splitG_5[0] + "\n" + splitG_5[1]);
            txt_tbr_Results_Province2.setText(splitG_5[2] + "\n" + splitG_5[3]);
            txt_tbr_Results_Province3.setText(splitG_5[4] + "\n" + splitG_5[5]);
            txt_tbr_Results_Province4.setVisibility(View.GONE);
        }
        else if(txtPrize.equals(G_4))
        {
            txt_tbr_Prize_Name.setText(txtPrize);
            String [] splitG_4 = prizeResults.getResultsProvince1().split(SPLITSPACE);
            txt_tbr_Results_Province1.setText(splitG_4[0] + "\n" + splitG_4[1]);
            txt_tbr_Results_Province2.setText(splitG_4[2] + "\n" + splitG_4[3]);
            txt_tbr_Results_Province3.setVisibility(View.GONE);
            txt_tbr_Results_Province4.setVisibility(View.GONE);
        }
        else if(txtPrize.equals(G_2))
        {
            txt_tbr_Prize_Name.setText(txtPrize);
            String [] splitG_2 = prizeResults.getResultsProvince1().split(SPLITSPACE);
            txt_tbr_Results_Province1.setText(splitG_2[0]);
            txt_tbr_Results_Province2.setText(splitG_2[1]);
            txt_tbr_Results_Province3.setVisibility(View.GONE);
            txt_tbr_Results_Province4.setVisibility(View.GONE);
        }


    }

    @SuppressLint("SetTextI18n")
    private void setDataForItemMN(String txtPrize, PrizeResults prizeResults)
    {
        Log.d("CheckHienThi", "prizeResultsListMB_Province = " + prizeResults.getResultsProvince1());
       if(txtPrize.equals(G_6))
        {
            txt_tbr_Prize_Name.setText(txtPrize);
            String [] splitG_6 = prizeResults.getResultsProvince1().split(SPLITSPACE);
            txt_tbr_Results_Province1.setText(splitG_6[0]);
            txt_tbr_Results_Province2.setText(splitG_6[1]);
            txt_tbr_Results_Province3.setText(splitG_6[2]);
            txt_tbr_Results_Province4.setVisibility(View.GONE);
        }
        else if(txtPrize.equals(G_4))
        {
            txt_tbr_Prize_Name.setText(txtPrize);
            String [] splitG_5 = prizeResults.getResultsProvince1().split(SPLITSPACE);
            txt_tbr_Results_Province1.setText(splitG_5[0] + "\n" + splitG_5[1]);
            txt_tbr_Results_Province2.setText(splitG_5[2] + "\n" + splitG_5[3] + "\n" + splitG_5[4]);
            txt_tbr_Results_Province3.setText(splitG_5[5] + "\n" + splitG_5[6]);
            txt_tbr_Results_Province4.setVisibility(View.GONE);
        }
        else if(txtPrize.equals(G_3))
        {
            txt_tbr_Prize_Name.setText(txtPrize);
            String [] splitG_3 = prizeResults.getResultsProvince1().split(SPLITSPACE);
            txt_tbr_Results_Province1.setText(splitG_3[0] );
            txt_tbr_Results_Province2.setText(splitG_3[1]);
            txt_tbr_Results_Province3.setVisibility(View.GONE);
            txt_tbr_Results_Province4.setVisibility(View.GONE);
        }
        else
        {
            txt_tbr_Prize_Name.setText(txtPrize);
            txt_tbr_Results_Province1.setText(prizeResults.getResultsProvince1().replace(SPLITSPACE,"\n"));
            txt_tbr_Results_Province2.setVisibility(View.GONE);
            txt_tbr_Results_Province3.setVisibility(View.GONE);
            txt_tbr_Results_Province4.setVisibility(View.GONE);
        }


    }
    /**
     * setColorDay đây là phương thức gáng màu hoặc size cho các textView riêng biệt
     * @param txtPrize đây là giải mấy hoặc tên thứ
     */
    private void setColorDay(String txtPrize)
    {
        Log.d("txt_tbr_Prize_Name", " txtPrize = "+ txtPrize );
        if(txtPrize.contains("T") || txtPrize.contains("CN"))
        {
            setBackgroundColor(R.color.yellow_name_station);//yellow_name_station
            setSizeTextView(16);
            txt_tbr_Results_Province1.setTextSize(20);
        }
        else if(txtPrize.equals("G.7")
                || txtPrize.equals("G.5")
                || txtPrize.equals("G.3")
                ||txtPrize.equals("G.1"))
        {
            setBackgroundColor(R.color.grey_background);
        }
        else {
            setIDColor(R.color.black);
            setBackgroundColor(R.color.white);//yellow_name_station
            if(txtPrize.equals("ĐB"))
            {
                txt_tbr_Results_Province1.setTextColor(mActivity.getResources().getColor(R.color.red_g8));
            }
        }
    }

    /**
     * setIDColor đây là phương thức set màu của tất cả các textView
     * @param idColor màu chữ của textView
     */
    private void setIDColor(int idColor)
    {
        txt_tbr_Prize_Name.setTextColor(mActivity.getResources().getColor(idColor));
        txt_tbr_Results_Province1.setTextColor(mActivity.getResources().getColor(idColor));
        txt_tbr_Results_Province2.setTextColor(mActivity.getResources().getColor(idColor));
        txt_tbr_Results_Province3.setTextColor(mActivity.getResources().getColor(idColor));
        txt_tbr_Results_Province4.setTextColor(mActivity.getResources().getColor(idColor));
    }

    /**
     * setIDColor đây là phương thức set Background của tất cả các textView
     * @param idColor màu chữ của textView
     */
    private void setBackgroundColor(int idColor)
    {
        txt_tbr_Prize_Name.setBackgroundColor(mActivity.getResources().getColor(idColor));
        txt_tbr_Results_Province1.setBackgroundColor(mActivity.getResources().getColor(idColor));
        txt_tbr_Results_Province2.setBackgroundColor(mActivity.getResources().getColor(idColor));
        txt_tbr_Results_Province3.setBackgroundColor(mActivity.getResources().getColor(idColor));
        txt_tbr_Results_Province4.setBackgroundColor(mActivity.getResources().getColor(idColor));
    }


    /**
     * setIDColor đây là phương thức set size của tất cả các textView
     * @param sizeTextView size cỡ chữ mong muốn của textView
     */
    private void setSizeTextView(float sizeTextView)
    {
        txt_tbr_Prize_Name.setTextSize(sizeTextView);
        txt_tbr_Results_Province1.setTextSize(sizeTextView);
        txt_tbr_Results_Province2.setTextSize(sizeTextView);
        txt_tbr_Results_Province3.setTextSize(sizeTextView);
        txt_tbr_Results_Province4.setTextSize(sizeTextView);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}