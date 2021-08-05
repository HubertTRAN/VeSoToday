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
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.onebyte.doveso.R;
import com.onebyte.doveso.model.TraditionalLottery;
import com.onebyte.doveso.model.WinningLottery;
import com.onebyte.doveso.temporaryfiledbmanager.TemporaryFileDBTraditionalLottery;
import java.util.List;
import java.util.Objects;
import static com.onebyte.doveso.api.ApiMethod.getColoredSpanned;
import static com.onebyte.doveso.api.ApiMethod.getDayOfMonthNumber;
import static com.onebyte.doveso.api.ApiMethod.getMoneyFromLotteryMB;
import static com.onebyte.doveso.api.ApiMethod.getMoneyFromLotteryMN;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_BAC;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_NAM;
import static com.onebyte.doveso.contants.Global.KET_QUA_MIEN_TRUNG;
import static com.onebyte.doveso.contants.Global.KHUYEN_KHICH;
import static com.onebyte.doveso.contants.Global.KHUYEN_KHICH_MB;
import static com.onebyte.doveso.contants.Global.KHUYEN_KHICH_MN;

public class AdapterResultsLottery extends RecyclerView.Adapter<AdapterResultsLottery.ViewHolder> {

    private Activity mActivity;
    private static List<WinningLottery> mDataSet;
    public static Dialog dialog;
    private TextView txt_day, txt_Province_Name, txt_Province_Name_1,
            txt_result_G8,txt_Name_Prize_8,
            txt_result_G7_1, txt_result_G7_2, txt_result_G7_3, txt_result_G7_4,
            txt_result_G6_1, txt_result_G6_2, txt_result_G6_3,
            txt_result_G5_1, txt_result_G5_2, txt_result_G5_3,
            txt_result_G4_1, txt_result_G4_2, txt_result_G4_3,
            txt_result_G3_1, txt_result_G3_2, txt_result_G3_3,
            txt_result_G2_1, txt_result_G2_2,
            txt_result_G1,
            txt_result_DB,
            txt_dialog_Results_Lottery;


    private static int CheckPositionMB = 100;

    public static ImageView img_dialog_gif_Results_Lottery;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_Results_Lottery;
        View mView;
        LinearLayout lnl_Results_Lottery_All;
        ImageView img_gif_Results_Lottery;

        ViewHolder(View v) {
            super(v);

            txt_Results_Lottery = v.findViewById(R.id.txt_Results_Lottery);
            img_gif_Results_Lottery = v.findViewById(R.id.img_gif_Results_Lottery);
            lnl_Results_Lottery_All = v.findViewById(R.id.lnl_Results_Lottery_All);
            mView = v;
            lnl_Results_Lottery_All.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            WinningLottery winningLottery = mDataSet.get(position);
            Log.d("startxxx", "positionKhuyenKhich = " + winningLottery.getCodeLottery());
            // Lightning click event.
            TemporaryFileDBTraditionalLottery traditionalLotteryRowID = new TemporaryFileDBTraditionalLottery(mActivity);
            TraditionalLottery traditionalLottery = traditionalLotteryRowID.TraditionalLotteryDBReadWithRowID(winningLottery.getRowID());
            if(traditionalLottery != null)
            {
//                if(checkLotteryImage)
//                {
//                    showDialogResultsLotteryImage(traditionalLottery, mActivity, winningLottery.getCodeLottery());
//                }else {
                    if(traditionalLottery.getTEN_DAI() != null)
                    {

                        Log.d("startxxx", "positionKhuyenKhich = " + winningLottery.getCodePrizeLottery());
                        if(winningLottery.getCodePrizeLottery().contains(KHUYEN_KHICH_MB) || winningLottery.getCodePrizeLottery().contains(KHUYEN_KHICH_MN))
                        {
                            if(winningLottery.getCodePrizeLottery().contains(KHUYEN_KHICH_MN))
                            {
                                int pointKhuyeKhich = Integer.parseInt(winningLottery.getCodePrizeLottery().replace(KHUYEN_KHICH_MN,""));
                                showDialogResultsLottery(traditionalLottery, mActivity, winningLottery.getCodeLottery(), pointKhuyeKhich);
                            }
                            else {
                                showDialogResultsLottery(traditionalLottery, mActivity, winningLottery.getCodeLottery(), CheckPositionMB);  // 100 là xét mặc định cho giải trúng khuyến khích miền bắc.
                                // pointKhuyeKhich sau vị trí này lấy thêm 1 số nữa vì Khuyến khích miền Bắc thì tô 2 số trúng của giải đặc biệt
                            }
                        }
                        else {
                            showDialogResultsLottery(traditionalLottery, mActivity, winningLottery.getCodeLottery(), -1);
                        }
                    }
               // }

            }
        }
    }

    public AdapterResultsLottery(Activity activity, List<WinningLottery> myDataSet) {
        mActivity = activity;
        mDataSet = myDataSet;
    }

    @NonNull
    @Override
    public AdapterResultsLottery.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rcv_results_lottery, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WinningLottery winningLottery = mDataSet.get(position);
        if(winningLottery != null)
        {
            if(!winningLottery.getPrize().equals("imageLottery"))
            {
                //String Giai = getColoredSpanned(winningLottery.getPrize(), "#cc0029");
                holder.txt_Results_Lottery.setText(Html.fromHtml(setTextResultsLottery(winningLottery))); //+ ", Mã Vé số: "+ winningLottery.getCodeLottery()

                Glide.with(mActivity)
                        .load(R.drawable.gif_than_tai_trung_thuong)
                        .into(holder.img_gif_Results_Lottery);
            }else {
                holder.txt_Results_Lottery.setText(Html.fromHtml("Rất tiếc vé số của bạn không trúng giải !<br>" +
                        "Chúc bạn may mắn lần sau!..."));

                Glide.with(mActivity)
                        .load(R.drawable.gif_khong_trung)
                        .into(holder.img_gif_Results_Lottery);
            }

        }
    }

    private String setTextResultsLottery(WinningLottery winningLottery)
    {
        Log.d("WinningLottery", winningLottery.getPrize() + " ==  " +  winningLottery.getDomainLottery() + " ==== " + checkPrizeLottery(winningLottery.getPrize(), winningLottery.getDomainLottery()));
        return "Chúc mừng bạn !..." +
                "Vé số của bạn đã trúng thưởng " + getColoredSpanned(winningLottery.getPrize().replace("MB","").replace("MN",""), "#cc0029") +
                " với số "+ getColoredSpanned((winningLottery.getCodePrizeLottery().contains(KHUYEN_KHICH) ? winningLottery.getCodeLottery(): winningLottery.getCodePrizeLottery()), "#cc0029")
                +"<br>Tổng giá trị giải thưởng là: "+ getColoredSpanned(checkPrizeLottery(winningLottery.getPrize(), winningLottery.getDomainLottery()), "#cc0029")
                + "<br>Đài: "+ getColoredSpanned(winningLottery.getNameLottery(), "#cc0029") + ", Ngày: "+ getColoredSpanned(winningLottery.getDateLottery(), "#cc0029");
    }

    private String checkPrizeLottery(String prize, String Domain)
    {
        String prizeLottery;
        if(Domain.equals(String.valueOf(KET_QUA_MIEN_NAM) )|| Domain.equals(String.valueOf(KET_QUA_MIEN_TRUNG)))
        {
            prizeLottery = getMoneyFromLotteryMN(prize);
        }else {
            prizeLottery = getMoneyFromLotteryMB(prize);
        }
        return prizeLottery;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    /**
     * showDialogResultsLottery đây là phương thức show lên một Dialog kết quả chi tiết của kết quả số xố.
     * @param traditionalLottery đối tượng traditionalLottery
     * @param context Context
     * @param code mã vé số
     * @param positionKhuyenKhich Vị trí sai của giải khuyến khích. Nếu người dùng chúng giải khuyến khích thì tô dãy số đúng màu đỏ và vô vị trí số sai màu đen.
     */
    private void showDialogResultsLottery(TraditionalLottery traditionalLottery, final Context context, String code, int positionKhuyenKhich) {

        try {
            if (traditionalLottery != null)
            {

                dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_alert_results_lottery);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                Objects.requireNonNull(dialog.getWindow()).setLayout(context.getResources().getDisplayMetrics().widthPixels,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                Button btn_Close_Alert = dialog.findViewById(R.id.btn_Close_Alert);

                // Khai báo ID cho tất cả các giá trị TextView, Button, ImageView...
                setIDAll();

                if(traditionalLottery.getMIEN_XO_SO().equals(String.valueOf(KET_QUA_MIEN_BAC))) // kết quả chi tiết này của đài miền Bắc
                {
                    if(positionKhuyenKhich == -1)
                        setColorForResultsLotteryMB(code, traditionalLottery, -1);
                    else
                        setColorForResultsLotteryMB(code, traditionalLottery, CheckPositionMB);

                }else { // kết quả chi tiết này của đài miền Nam hoặc Miền Trung vì 2 cái này cơ cấu giải hoàn toàn giống nhau
                    if(positionKhuyenKhich == -1) // Vé số đã trúng nhưng không phải trúng giải khuyến khích
                        setColorForResultsLotteryMN(code, traditionalLottery, -1);
                    else // Vé số đã trúng giải khuyến khích
                        setColorForResultsLotteryMN(code, traditionalLottery, positionKhuyenKhich);
                }

                // btn_Close_Alert Nhấn để đóng Dialog
                btn_Close_Alert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                // show dialog
                dialog.show();
            }
        }catch (Exception e)
        {
            Log.e("ERROR",e.getMessage()+"");
        }
    }

    /**
     * Method: setColorForResultsLottery là phương thức gán các kết quả xổ số vào TextView của Dialog.
     * @param code mã vé số người dùng nhập vào
     * @param traditionalLottery đối tượng vé số đã được lưu trong SQLite
     * @param positionKhuyenKhich đây là giá trị để xác định trúng thưởng giải khuyến khích hay là trúng thưởng các giải khác
     */
    @SuppressLint("SetTextI18n")
    private void setColorForResultsLotteryMN(String code, TraditionalLottery traditionalLottery, int positionKhuyenKhich)
    {
        int dayOfWeek = 0;
        dayOfWeek = getDayOfMonthNumber(traditionalLottery.getNGAY_XO_SO());
        if(dayOfWeek == 1)
        {
            txt_day.setText("Chủ Nhật"); // chuyển đổi thành thứ
        }
        else {
            txt_day.setText("Thứ " + dayOfWeek);
        }

        //txt_Province_Name.setText(traditionalLottery.getTEN_DAI() + "\n" + traditionalLottery.getNGAY_XO_SO());
        txt_Province_Name.setText("Kết Quả Xổ Số "+ traditionalLottery.getTEN_DAI());
        txt_Province_Name_1.setText("(" + traditionalLottery.getNGAY_XO_SO() + ")");
        Log.d("giai8", code.substring(4));

        txt_result_G8.setVisibility(View.VISIBLE);
        txt_Name_Prize_8.setVisibility(View.VISIBLE);
        txt_result_G8.setText(Html.fromHtml(checkColorCode(code.substring(4),traditionalLottery.getKET_QUA_G8())));

        txt_result_G7_1.setText(Html.fromHtml(checkColorCode(code.substring(3),traditionalLottery.getKET_QUA_G7())));
        txt_result_G7_2.setVisibility(View.GONE);
        txt_result_G7_3.setVisibility(View.GONE);
        txt_result_G7_4.setVisibility(View.GONE);

        String [] splits_G6 = traditionalLottery.getKET_QUA_G6().split("-");
        if(splits_G6.length == 3)
        {
            // giải 6 ở TextView này sẽ hiển thị kết quả của 3 giải sáu
            txt_result_G6_1.setText(Html.fromHtml(checkColorCode(code.substring(2),splits_G6[0])));
            txt_result_G6_2.setText(Html.fromHtml(checkColorCode(code.substring(2),splits_G6[1])));
            txt_result_G6_3.setText(Html.fromHtml(checkColorCode(code.substring(2),splits_G6[2])));
        }


        txt_result_G5_1.setText(Html.fromHtml(checkColorCode(code.substring(2),traditionalLottery.getKET_QUA_G5())));
        txt_result_G5_2.setVisibility(View.GONE);
        txt_result_G5_3.setVisibility(View.GONE);

        String [] splits_G4 = traditionalLottery.getKET_QUA_G4().split("-");
        if(splits_G4.length == 7)
        {
            // giải 4 ở TextView1 này sẽ hiển thị kết quả của 2 giải tư đầu tiên
            txt_result_G4_1.setText(Html.fromHtml(checkColorCode(code.substring(1), splits_G4[0]) + "<br>"+checkColorCode(code.substring(1), splits_G4[1]))); // 2
            // giải 4 ở TextView2 này sẽ hiển thị kết quả của 3 giải tư tiếp theo
            txt_result_G4_2.setText(Html.fromHtml(checkColorCode(code.substring(1), splits_G4[2]) + "<br>" + checkColorCode(code.substring(1), splits_G4[3]) + "<br>" + checkColorCode(code.substring(1), splits_G4[4]))); // 3
            // giải 4 ở TextView3 này sẽ hiển thị kết quả của 2 giải tư cuối
            txt_result_G4_3.setText(Html.fromHtml(checkColorCode(code.substring(1), splits_G4[5]) + "<br>" + checkColorCode(code.substring(1), splits_G4[6]))); // 3

        }

        String [] splits_G3 = traditionalLottery.getKET_QUA_G3().split("-");
        if(splits_G3.length == 2)
        {
            // giải 3 ở TextView này sẽ hiển thị kết quả của 2 giải ba
            txt_result_G3_1.setText(Html.fromHtml(checkColorCode(code.substring(1),splits_G3[0])));
            txt_result_G3_2.setText(Html.fromHtml(checkColorCode(code.substring(1),splits_G3[1])));
            txt_result_G3_3.setVisibility(View.GONE);
        }

        txt_result_G2_1.setText(Html.fromHtml(checkColorCode(code.substring(1),traditionalLottery.getKET_QUA_G2())));
        txt_result_G2_2.setVisibility(View.GONE);

        txt_result_G1.setText(Html.fromHtml(checkColorCode(code.substring(1),traditionalLottery.getKET_QUA_G1())));

        Log.d("startxxx", "positionKhuyenKhich = " + positionKhuyenKhich);
        if(positionKhuyenKhich == -1)
        {
            // đây là kiểm tra tô màu cho giải đặc biệt và giải phụ đặc biệt
            txt_result_DB.setText(Html.fromHtml(checkColorCode(code,traditionalLottery.getKET_QUA_DB())));

            //txt_result_DB.setText(Html.fromHtml(checkColorCodePDB(code,traditionalLottery.getKET_QUA_DB())));

        }else {
            // đây là kiểm tra cho giải khuyến khích
            Log.d("startxxx", "đây là kiểm tra cho giải khuyến khích");
            txt_result_DB.setText(Html.fromHtml(checkColorCode(traditionalLottery.getKET_QUA_DB(), positionKhuyenKhich)));
        }
    }

    /**
     * Method: setColorForResultsLottery là phương thức gán các kết quả xổ số vào TextView của Dialog.
     * @param code mã vé số người dùng nhập vào
     * @param traditionalLottery đối tượng vé số đã được lưu trong SQLite
     * @param positionKhuyenKhich đây là giá trị để xác định trúng thưởng giải khuyến khích hay là trúng thưởng các giải khác
     */
    @SuppressLint("SetTextI18n")
    private void setColorForResultsLotteryMB(String code, TraditionalLottery traditionalLottery, int positionKhuyenKhich)
    {
        String tab_two = "&ensp;";
        String br = "<br>";

        int dayOfWeek = 0;
        dayOfWeek = getDayOfMonthNumber(traditionalLottery.getNGAY_XO_SO());
        if(dayOfWeek == 1)
        {
            txt_day.setText("Chủ Nhật"); // chuyển đổi thành thứ
        }
        else {
            txt_day.setText("Thứ " + dayOfWeek);
        }

        txt_Province_Name.setText("Kết Quả Xổ Số "+ traditionalLottery.getTEN_DAI());
        txt_Province_Name_1.setText("(" + traditionalLottery.getNGAY_XO_SO() + ")");
        Log.d("giai8", code.substring(4)); // miền bắc không có giải 8
        txt_result_G8.setVisibility(View.GONE);
        txt_Name_Prize_8.setVisibility(View.GONE);

        String [] splits_G7 = traditionalLottery.getKET_QUA_G7().split("-");
        if(splits_G7.length == 4)
        {
            txt_result_G7_1.setText(Html.fromHtml(checkColorCode(code.substring(3), splits_G7[0])));
            txt_result_G7_2.setText(Html.fromHtml(checkColorCode(code.substring(3), splits_G7[1])));
            txt_result_G7_3.setText(Html.fromHtml(checkColorCode(code.substring(3), splits_G7[2])));
            txt_result_G7_4.setText(Html.fromHtml(checkColorCode(code.substring(3), splits_G7[3])));
        }


        String [] splits_G6 = traditionalLottery.getKET_QUA_G6().split("-");
        if(splits_G6.length == 3)
        {
            // giải 6 ở TextView này sẽ hiển thị kết quả của 3 giải sáu
            txt_result_G6_1.setText(Html.fromHtml(checkColorCode(code.substring(2), splits_G6[0])));
            txt_result_G6_2.setText(Html.fromHtml(checkColorCode(code.substring(2), splits_G6[1])));
            txt_result_G6_3.setText(Html.fromHtml(checkColorCode(code.substring(2), splits_G6[2])));
        }

        String [] splits_G5 = traditionalLottery.getKET_QUA_G5().split("-");
        if(splits_G5.length == 6)
        {
            // giải 6 ở TextView này sẽ hiển thị kết quả của 3 giải sáu
            txt_result_G5_1.setText(Html.fromHtml(checkColorCode(code.substring(1), splits_G5[0]) +  tab_two +
                    checkColorCode(code.substring(1), splits_G5[1])));
            txt_result_G5_2.setText(Html.fromHtml(checkColorCode(code.substring(1), splits_G5[2]) + tab_two +
                    checkColorCode(code.substring(1), splits_G5[3]) ));
            txt_result_G5_3.setText(Html.fromHtml(checkColorCode(code.substring(1), splits_G5[4]) + tab_two +
                    checkColorCode(code.substring(1), splits_G5[5])));
        }

        String [] splits_G4 = traditionalLottery.getKET_QUA_G4().split("-");
        if(splits_G4.length == 4)
        {
            // giải 4 ở TextView1 này sẽ hiển thị kết quả của 2 giải tư đầu tiên
            txt_result_G4_1.setText(Html.fromHtml(checkColorCode(code.substring(1), splits_G4[0]) + "<br>"+checkColorCode(code.substring(1), splits_G4[1]))); // 2

            txt_result_G4_2.setVisibility(View.GONE);
            // giải 4 ở TextView2 này sẽ hiển thị kết quả của 3 giải tư tiếp theo
            //txt_result_G4_2.setText(Html.fromHtml(checkColorCode(code.substring(1), splits_G4[2]) + "<br>" + checkColorCode(code.substring(1), splits_G4[3]) + "<br>" + checkColorCode(code.substring(1), splits_G4[4]))); // 3
            // giải 4 ở TextView3 này sẽ hiển thị kết quả của 2 giải tư cuối
            txt_result_G4_3.setText(Html.fromHtml(checkColorCode(code.substring(1), splits_G4[2]) + "<br>" + checkColorCode(code.substring(1), splits_G4[3]))); // 3

        }

        String [] splits_G3 = traditionalLottery.getKET_QUA_G3().split("-");
        if(splits_G3.length == 6)
        {
            // giải 3 ở TextView này sẽ hiển thị kết quả của 2 giải ba
            txt_result_G3_1.setText(Html.fromHtml(checkColorCode(code,splits_G3[0])  + "<br>" +
                    checkColorCode(code,splits_G3[1])));

            txt_result_G3_2.setText(Html.fromHtml(checkColorCode(code,splits_G3[2])  + "<br>" +
                    checkColorCode(code,splits_G3[3])));

            txt_result_G3_3.setText(Html.fromHtml(checkColorCode(code,splits_G3[4])  + "<br>" +
                    checkColorCode(code,splits_G3[5])));
        }

        String [] splits_G2 = traditionalLottery.getKET_QUA_G2().split("-");
        if(splits_G2.length == 2)
        {
            // giải 3 ở TextView này sẽ hiển thị kết quả của 2 giải ba
            txt_result_G2_1.setText(Html.fromHtml(checkColorCode(code,splits_G2[0])));
            txt_result_G2_2.setText(Html.fromHtml(checkColorCode(code,splits_G2[1])));
        }

        txt_result_G1.setText(Html.fromHtml(checkColorCode(code, traditionalLottery.getKET_QUA_G1())));

       if(positionKhuyenKhich == CheckPositionMB)
        {
            // đây là kiểm tra cho giải khuyến khích
            txt_result_DB.setText(Html.fromHtml(checkColorCode(traditionalLottery.getKET_QUA_DB(), CheckPositionMB)));

        }
       else {
           // đây là kiểm tra tô màu cho giải đặc biệt và đặc biệt
           txt_result_DB.setText(Html.fromHtml(checkColorCode(code,traditionalLottery.getKET_QUA_DB())));
       }

    }

    public static String checkColorCode(String code, String results)
    {
        Log.d("caas", "code = "+ code + " ketqua = "+ results);
        if(code.contains(results.trim()))
        {
            //return "<b>" + getColoredSpanned(results, "#cc0029") + "</b>";
            return getColoredSpanned(results, "#cc0029");
        }
        else if(results.length() == 5 && results.substring(1).contains(code.substring(1)))
        {
            return results.charAt(0) + getColoredSpanned(results.substring(1), "#cc0029");
        }
        else if(code.length() == 6 & (code.charAt(0) != results.trim().charAt(0)))
        {
            //return "<b>" + getColoredSpanned(results, "#cc0029") + "</b>";
            if(code.substring(1).equals(results.trim().substring(1)))
            {
                return results.charAt(0) + getColoredSpanned(results.substring(1), "#cc0029");
            }
            else {
                return results;
            }
        }
        else
        {
            return results;
        }
    }

    private String checkColorCodePDB(String code, String results)
    {
        Log.d("caa1s", "code = "+ code + " ketqua = "+ results);
        if(code.charAt(0) != results.trim().charAt(0))
        {
            //return "<b>" + getColoredSpanned(results, "#cc0029") + "</b>";
            if(code.substring(1).equals(results.trim().substring(1)))
            {
                return results.charAt(0) + getColoredSpanned(results.substring(1), "#cc0029");
            }
            else {
                return results;
            }
        }else if(code.equals(results.trim()))
        {
            return getColoredSpanned(results, "#cc0029");
        }
        else
        {
            return results;
        }

    }

    private static String checkColorCode(String results, int positionKhuyenKhich)
    {
            return toMauKetQuaDung(positionKhuyenKhich, results);
    }

    private static String toMauKetQuaDung(int point, String codeLottery) {
        String start;
        String position;
        String end;

        if(point == CheckPositionMB)
        {
            start = codeLottery.substring(0,2); // lấy 3 kí tự đầu của mã vé số miền bắc giữ nguyên màu đen
            end = codeLottery.substring(3); // lấy 2 kí tự cuối của mã vé số để tô màu đỏ hiển thị trúng giải Khuyến Khích Miền Bắc
            end = getColoredSpanned(end, "#cc0029");
            return start + end;
        }else if(point == 1)
        {
            start = String.valueOf(codeLottery.charAt(0));
            position = String.valueOf(codeLottery.charAt(1));
            end = codeLottery.substring(2);
        }
        else if(point == 2)
        {
            start = codeLottery.substring(0,2);
            position = String.valueOf(codeLottery.charAt(2));
            end = codeLottery.substring(3);
        }
        else if(point == 3)
        {
            start = codeLottery.substring(0,4);
            position = String.valueOf(codeLottery.charAt(3));
            end = codeLottery.substring(4);
        }
        else if(point == 4)
        {
            start = codeLottery.substring(0,4);
            position = String.valueOf(codeLottery.charAt(4));
            end = codeLottery.substring(5);
        }
        else
        {
            //657739
            start = codeLottery.substring(0,5);
            position = String.valueOf(codeLottery.charAt(5));
            end = "";
        }

        Log.d("startxxx", "Code= "+ codeLottery +" start= " + start + " position = "+ position + " end= "+ end);
        start = getColoredSpanned(start, "#cc0029");
        end = getColoredSpanned(end, "#cc0029");
        return start + position + end;
    }

    /**
     * Method: setID đây là phương thức gán giá trị id cho các TextView
     */
    private void setIDAll()
    {
        txt_day = dialog.findViewById(R.id.txt_day);
        txt_Province_Name = dialog.findViewById(R.id.txt_Province_Name);
        txt_Province_Name_1 = dialog.findViewById(R.id.txt_Province_Name_1);
        txt_result_G8 = dialog.findViewById(R.id.txt_result_G8);
        txt_Name_Prize_8 = dialog.findViewById(R.id.txt_Name_Prize_8);

        txt_result_G7_1 = dialog.findViewById(R.id.txt_result_G7_1);
        txt_result_G7_2 = dialog.findViewById(R.id.txt_result_G7_2);
        txt_result_G7_3 = dialog.findViewById(R.id.txt_result_G7_3);
        txt_result_G7_4 = dialog.findViewById(R.id.txt_result_G7_4);

        txt_result_G6_1 = dialog.findViewById(R.id.txt_result_G6_1);
        txt_result_G6_2 = dialog.findViewById(R.id.txt_result_G6_2);
        txt_result_G6_3 = dialog.findViewById(R.id.txt_result_G6_3);

        txt_result_G5_1 = dialog.findViewById(R.id.txt_result_G5_1);
        txt_result_G5_2 = dialog.findViewById(R.id.txt_result_G5_2);
        txt_result_G5_3 = dialog.findViewById(R.id.txt_result_G5_3);

        txt_result_G4_1 = dialog.findViewById(R.id.txt_result_G4_1);
        txt_result_G4_2 = dialog.findViewById(R.id.txt_result_G4_2);
        txt_result_G4_3 = dialog.findViewById(R.id.txt_result_G4_3);

        txt_result_G3_1 = dialog.findViewById(R.id.txt_result_G3_1);
        txt_result_G3_2 = dialog.findViewById(R.id.txt_result_G3_2);
        txt_result_G3_3 = dialog.findViewById(R.id.txt_result_G3_3);

        txt_result_G2_1 = dialog.findViewById(R.id.txt_result_G2_1);
        txt_result_G2_2 = dialog.findViewById(R.id.txt_result_G2_2);

        txt_result_G1 = dialog.findViewById(R.id.txt_result_G1);
        txt_result_DB = dialog.findViewById(R.id.txt_result_DB);
        //txt_dialog-Results_Lottery
        //txt_dialog_Results_Lottery = dialog.findViewById(R.id.txt_dialog_Results_Lottery);
        //img_dialog_gif_Results_Lottery = dialog.findViewById(R.id.img_dialog_gif_Results_Lottery);
    }
}