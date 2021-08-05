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
import static com.onebyte.doveso.contants.Global.G_1;
import static com.onebyte.doveso.contants.Global.G_3;
import static com.onebyte.doveso.contants.Global.G_5;
import static com.onebyte.doveso.contants.Global.G_7;
import static com.onebyte.doveso.contants.Global.G_DB;

public class AdapterDetailResultsLotteryMT extends RecyclerView.Adapter<AdapterDetailResultsLotteryMT.ViewHolder> {

    private Activity mActivity;
    private static List<PrizeResults> mDataSet;
    private TextView txt_tbr_Prize_Name, txt_tbr_Results_Province1, txt_tbr_Results_Province2,
            txt_tbr_Results_Province3, txt_tbr_Results_Province4;
    private final String SPLITSPACE = " - ";

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      /*  private TextView txt_tbr_Prize_Name, txt_tbr_Results_Province1, txt_tbr_Results_Province2,
                txt_tbr_Results_Province3, txt_tbr_Results_Province4;*/
        View mView;
        LinearLayout lnl_Results_Lottery;

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

    public AdapterDetailResultsLotteryMT(Activity activity, List<PrizeResults> myDataSet) {
        mActivity = activity;
        mDataSet = myDataSet;
    }

    @NonNull
    @Override
    public AdapterDetailResultsLotteryMT.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rcv_detail_results_mien_nam_trung, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrizeResults prizeResults = mDataSet.get(position);
        try {
            if(prizeResults != null && prizeResults.getSizeProvince() != 0)
            {
               /* setColorDay(txt_tbr_Prize_Name.getText().toString()); */// thêm màu cho các textView khi nó là cột tên thứ và tên tỉnh
                if(prizeResults.getSizeProvince() == 1) // xuất hiện ở kết quả xổ số miền bắc và kết quả của 1 tỉnh nào đó
                {
                    //holder.txt_tbr_Prize_Name.setText(prizeResults.getPrize());
                    txt_tbr_Prize_Name.setText(prizeResults.getPrize());
                    txt_tbr_Results_Province1.setText(prizeResults.getResultsProvince1().replace(SPLITSPACE,"\n"));
                    txt_tbr_Results_Province2.setVisibility(View.GONE);
                    txt_tbr_Results_Province3.setVisibility(View.GONE);
                    txt_tbr_Results_Province4.setVisibility(View.GONE);
                }
                else if(prizeResults.getSizeProvince() == 2) // xuất hiện ở xổ số miền trung
                {
                    txt_tbr_Prize_Name.setText(prizeResults.getPrize());
                    txt_tbr_Results_Province1.setText(prizeResults.getResultsProvince1().replace(SPLITSPACE,"\n"));
                    txt_tbr_Results_Province2.setText(prizeResults.getResultsProvince2().replace(SPLITSPACE,"\n"));
                    txt_tbr_Results_Province3.setVisibility(View.GONE);
                    txt_tbr_Results_Province4.setVisibility(View.GONE);
                }
                else if(prizeResults.getSizeProvince() == 3) // xuất hiện ở xổ số miền nam và miền trung
                {
                    txt_tbr_Prize_Name.setText(prizeResults.getPrize());
                    txt_tbr_Results_Province1.setText(prizeResults.getResultsProvince1().replace(SPLITSPACE,"\n"));
                    txt_tbr_Results_Province2.setText(prizeResults.getResultsProvince2().replace(SPLITSPACE,"\n"));
                    txt_tbr_Results_Province3.setText(prizeResults.getResultsProvince3().replace(SPLITSPACE,"\n"));
                    txt_tbr_Results_Province4.setVisibility(View.GONE);
                }
                else { // prizeResults.getSizeProvince() == 4 chỉ xuất hiện duy nhất ở xổ số miền nam
                    txt_tbr_Prize_Name.setText(prizeResults.getPrize());
                    txt_tbr_Results_Province1.setText(prizeResults.getResultsProvince1().replace(SPLITSPACE,"\n"));
                    txt_tbr_Results_Province2.setText(prizeResults.getResultsProvince2().replace(SPLITSPACE,"\n"));
                    txt_tbr_Results_Province3.setText(prizeResults.getResultsProvince3().replace(SPLITSPACE,"\n"));
                    txt_tbr_Results_Province4.setText(prizeResults.getResultsProvince4().replace(SPLITSPACE,"\n"));
                }

                String day = prizeResults.getPrize();
                if(prizeResults.getPrize().length()>6)
                {
                    txt_tbr_Prize_Name.setText(getDayOfMonth(prizeResults.getPrize(), true));
                    day = getDayOfMonth(prizeResults.getPrize(), true);
                }
                setColorDay(day); // thêm màu cho các textView khi nó là cột tên thứ và tên tỉnh
            }
        }
        catch (Exception e)
        {
            e.getMessage();
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
        }
        else if(txtPrize.equals(G_DB))
        {
            setIDColor(R.color.red_g8);
            setBackgroundColor(R.color.white);//yellow_name_station
        }
        else if(txtPrize.equals(G_7)
                || txtPrize.equals(G_5)
                || txtPrize.equals(G_3)
                ||txtPrize.equals(G_1))
        {
            setBackgroundColor(R.color.grey_background);
        }
        else {
            setIDColor(R.color.black);
            setBackgroundColor(R.color.white);//yellow_name_station
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