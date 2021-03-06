package com.example.user54.InventoryApp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.user54.InventoryApp.Model.MainSetting;
import com.example.user54.InventoryApp.Model.Password;
import com.example.user54.InventoryApp.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
//WORK
public class MainActivity2 extends AppCompatActivity {
//    Button exit, collecting, item, setting, report,send;
    LinearLayout  collecting, item, report,send;
    Button exit,  setting;
    Animation animFadein;
    private ScaleAnimation scale;
//    LinearLayout master;
    InventoryDatabase InventoryDb;
    String QrUse="0";
    String today,fromDateString="",ToDateString="";
    Date currentTimeAndDate;
    SimpleDateFormat df;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main3_new);
        if(controll.isYellow){
            setContentView(R.layout.activity_main3_yellow);
        }else{
            setContentView(R.layout.activity_main3);
        }

        InventoryDb=new InventoryDatabase(MainActivity2.this);
        myCalendar = Calendar.getInstance();
         currentTimeAndDate = Calendar.getInstance().getTime();
         df = new SimpleDateFormat("dd/MM/yyyy");
        today = df.format(currentTimeAndDate);

//        new SweetAlertDialog(MainActivity2.this)
//                .setTitleText("Here's a message!")
//                .show();


//        new SweetAlertDialog(MainActivity2.this, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("Are you sure?")
//                .setContentText("You won't be able to recover this file!")
//                .setConfirmText("Delete!")
//                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sDialog) {
//                        sDialog.dismissWithAnimation();
//                    }
//                })
//                .show();







//
//         master=(LinearLayout)findViewById(R.id.master);
       
        item = (LinearLayout) findViewById(R.id.item);
        setting = (Button) findViewById(R.id.setting);
        report = (LinearLayout) findViewById(R.id.report);
        exit = (Button) findViewById(R.id.exit);
        collecting = (LinearLayout) findViewById(R.id.collectData);
        send = (LinearLayout) findViewById(R.id.send);
//

//        Date currentTimeAndDate = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        String today = df.format(currentTimeAndDate);
//        date.setText(today);

        collecting.setOnClickListener(mainMenu);
        exit.setOnClickListener(mainMenu);
        item.setOnClickListener(mainMenu);
        setting.setOnClickListener(mainMenu);
        report.setOnClickListener(mainMenu);
        send.setOnClickListener(mainMenu);

//        List<MainSetting> mainSettings = InventoryDb.getAllMainSetting();
//        if (mainSettings.size() != 0) {
////            StkName = InventDB.getStkName(mainSettings.get(0).getStorNo());
////            StkNo = mainSettings.get(0).getStorNo();
//            QrUse = mainSettings.get(0).getIsQr();
//        }

        send.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final EditText editText = new EditText(MainActivity2.this);
                final TextView textView = new TextView(MainActivity2.this);
                editText.setHint("Enter Password");
                editText.setTextColor(Color.BLACK);
                textView.setTextColor(Color.RED);
                if (SweetAlertDialog.DARK_STYLE) {
                    editText.setTextColor(Color.BLACK);
                }
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(editText);
                linearLayout.addView(textView);

                SweetAlertDialog dialog = new SweetAlertDialog(MainActivity2.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Password")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                               String password=editText.getText().toString();
                                textView.setText("");
                                if(!password.equals("")){

                                    if(password.equals("2222")){

                                        textView.setText("");
                                        getChoesDialog();

                                        sweetAlertDialog.dismissWithAnimation();

                                    }else{
                                        textView.setText(getResources().getString(R.string.NotCorrectPassword));
                                    }

                                }

                            }
                        });
//                        .hideConfirmButton();

                dialog.setCustomView(linearLayout);
                dialog.show();


                return false;
            }
        });


//        master.setVisibility(View.GONE);

//        animFadein = AnimationUtils.loadAnimation(MainActivity2.this, R.anim.fade_in);
//        collecting.startAnimation(animFadein);
//        item.startAnimation(animFadein);
//        report.startAnimation(animFadein);
//        send.startAnimation(animFadein);
        callAnimation();
    }

    public void callAnimation(){
        scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.INFINITE, .2f, ScaleAnimation.RELATIVE_TO_SELF, .2f);
        scale.setStartOffset(100);
        scale.setDuration(500);
        scale.setInterpolator(new OvershootInterpolator());
        collecting.startAnimation(scale);

        scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setStartOffset(300);
        scale.setDuration(500);
        scale.setInterpolator(new OvershootInterpolator());
        item.startAnimation(scale);

        scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RESTART, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setStartOffset(500);
        scale.setDuration(500);
        scale.setInterpolator(new OvershootInterpolator());
        send.startAnimation(scale);

        scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RESTART, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
        scale.setStartOffset(700);
        scale.setDuration(500);
        scale.setInterpolator(new OvershootInterpolator());
        report.startAnimation(scale);

    }


    View.OnClickListener mainMenu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.collectData:

                    Intent coll = new Intent(MainActivity2.this, CollectingData.class);
                    startActivity(coll);

                    break;
                case R.id.item:

//                    master.setVisibility(View.VISIBLE);

                    Intent item = new Intent(MainActivity2.this, Item.class);
                    startActivity(item);
                    break;
                case R.id.report:
                    Intent report = new Intent(MainActivity2.this, Report.class);
                    startActivity(report);
                    break;
                case R.id.setting:
                    Intent tools = new Intent(MainActivity2.this, Tools.class);
                    startActivity(tools);

                    break;
                case R.id.send:
//                    Intent coll = new Intent(MainActivity.this, CllectingData.class);
//                    startActivity(coll);
//                      importJson sendCloud = new importJson(MainActivity2.this,"");
//                      sendCloud.startSending("ItemCard");
//                     sendCloud.startSending("ItemSwitch");
                    List<MainSetting> mainSetting=InventoryDb.getAllMainSetting();
                    if(mainSetting.size()!=0) {
                        DateDialog(1, 2,mainSetting.get(0).getIsAssest());
                       // alertMessageDialog(getResources().getString(R.string.importData), getResources().getString(R.string.importDataMessage), 2, "", mainSetting.get(0).getIsAssest());
                    }else{

                        new SweetAlertDialog(MainActivity2.this, SweetAlertDialog.WARNING_TYPE)
                                 .setTitleText(getResources().getString(R.string.mainSetting) + "!")
                                .setContentText(getResources().getString(R.string.nomainSetting))
                                .setConfirmText(getResources().getString(R.string.cancel))
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null).show();


                    }
                    break;
                case R.id.exit:
                    alertMessageDialog(getResources().getString(R.string.exitfromapp), getResources().getString(R.string.exitMessage), 0, "", "");

///data/data/com.example.user54.InventoryApp/databases
//                    /data/data/com.example.user54.InventoryApp/databases/InventoryDBase

            }


        }
    };


    void getChoesDialog(){
        final Button itemCard = new Button(this);
        final Button itemSwitch = new Button(this);
        final Button itemStore = new Button(this);
        final Button itemUnite = new Button(this);
        final Button itemAssets = new Button(this);
        final Button itemQr = new Button(this);
        List<MainSetting> mainSettings = InventoryDb.getAllMainSetting();
        if (mainSettings.size() != 0) {
//            StkName = InventDB.getStkName(mainSettings.get(0).getStorNo());
//            StkNo = mainSettings.get(0).getStorNo();
            QrUse = mainSettings.get(0).getIsQr();
        }

        itemCard.setText(getResources().getString(R.string.Import_Item_Card));
        itemSwitch.setText(getResources().getString(R.string.ImportItemSwitch));
        itemStore.setText(getResources().getString(R.string.ImporItemStore));
        itemUnite.setText("Import Item Unite");
        itemAssets.setText("Import Item Assets");
        itemQr.setText("Import Item QR");
        if (SweetAlertDialog.DARK_STYLE) {
            itemCard.setTextColor(Color.WHITE);
            itemSwitch.setTextColor(Color.WHITE);
            itemStore.setTextColor(Color.WHITE);
        }
//        itemCard.setBackgroundColor(Color.parseColor("#FDD835"));
//        itemSwitch.setBackgroundColor(Color.parseColor("#1F6381"));
//        itemStore.setBackgroundColor(Color.parseColor("#A5DC86"));


        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(itemCard);
        linearLayout.addView(itemSwitch);
        if(QrUse.equals("1")) {
            linearLayout.addView(itemQr);
        }
        linearLayout.addView(itemUnite);
        linearLayout.addView(itemStore);
        linearLayout.addView(itemAssets);


        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(getResources().getString(R.string.importData))
                .hideConfirmButton();

        dialog.setCustomView(linearLayout);
        dialog.show();


        itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages(2);
                dialog.dismissWithAnimation();
            }
        });

        itemSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages(3);
                dialog.dismissWithAnimation();

            }
        });

        itemStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages(4);
                dialog.dismissWithAnimation();


            }
        });

        itemUnite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages(5);
                dialog.dismissWithAnimation();


            }
        });

        itemAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages(6);
                dialog.dismissWithAnimation();
            }
        });


        itemQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messages(7);
                dialog.dismissWithAnimation();
            }
        });



    }

    void messages(int y){
        List<MainSetting> mainSetting=InventoryDb.getAllMainSetting();
        if(mainSetting.size()!=0) {
            if(y==2||y==3||y==5 ) {
                DateDialog(2, y,"");
            }else {
            alertMessageDialog(getResources().getString(R.string.importData), getResources().getString(R.string.importDataMessage), y, "", "");
            }

        }else{

            new SweetAlertDialog(MainActivity2.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getResources().getString(R.string.mainSetting) + "!")
                    .setContentText(getResources().getString(R.string.nomainSetting))
                    .setConfirmText(getResources().getString(R.string.cancel))
                    .showCancelButton(false)
                    .setCancelClickListener(null)
                    .setConfirmClickListener(null).show();
        }
    }


    void alertMessageDialog(String title, String message, final int swith, final String itemName, final String ItemCode) {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity2.this);
//        dialog.setTitle(title)
//                .setMessage(message)
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialoginterface, int i) {
//                        dialoginterface.cancel();
//                    }
//                })
//                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialoginterface, int i) {
//                        switch (swith) {
//                            case 0:
//                                finish();
//                                System.exit(0);
//                                break;
//                            case 1:
//
//                                Toast.makeText(MainActivity2.this, "All Item Delete ", Toast.LENGTH_SHORT).show();
////                             progressDialog();
//                                break;
//                            case 2:
//                                importJson sendCloud = new importJson(MainActivity2.this,"");
//                                sendCloud.startSending("ItemCard");
//
////                                importJson sendCloud2 = new importJson(MainActivity2.this,"");
////                                sendCloud2.startSending("ItemSwitch");
//
////                                importJson sendCloud3 = new importJson(MainActivity2.this,"");
////                                sendCloud3.startSending("GetStory");
//
//                                break;
//
//                        }
//                    }
//                }).show();


        new SweetAlertDialog(MainActivity2.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setCancelText(getResources().getString(R.string.cancel))
                .setConfirmText(getResources().getString(R.string.ok))
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        // reuse previous dialog instance, keep widget user state, reset them if you need



                        switch (swith) {
                            case 0:
                                sDialog.setTitleText(getResources().getString(R.string.cancel)+"!")
                                        .setContentText(getResources().getString(R.string.canselexit))
                                        .setConfirmText(getResources().getString(R.string.ok))
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                break;
                            case 1:
//
//                                Toast.makeText(MainActivity2.this, "All Item Delete ", Toast.LENGTH_SHORT).show();
////                             progressDialog();
                                break;
                            case 2:

                                   sDialog.setTitleText(getResources().getString(R.string.cancel) + "!")
                                           .setContentText(getResources().getString(R.string.cancelImport))
                                           .setConfirmText(getResources().getString(R.string.ok))
                                           .showCancelButton(false)
                                           .setCancelClickListener(null)
                                           .setConfirmClickListener(null)
                                           .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                break;
                            case 3:

                                sDialog.setTitleText(getResources().getString(R.string.cancel) + "!")
                                        .setContentText(getResources().getString(R.string.cancelImport))
                                        .setConfirmText(getResources().getString(R.string.ok))
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                break;
                            case 4:

                                sDialog.setTitleText(getResources().getString(R.string.cancel) + "!")
                                        .setContentText(getResources().getString(R.string.cancelImport))
                                        .setConfirmText(getResources().getString(R.string.ok))
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                break;



                            case 5:

                                sDialog.setTitleText(getResources().getString(R.string.cancel) + "!")
                                        .setContentText(getResources().getString(R.string.cancelImport))
                                        .setConfirmText(getResources().getString(R.string.ok))
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                break;
                            case 6:

                                sDialog.setTitleText(getResources().getString(R.string.cancel) + "!")
                                        .setContentText(getResources().getString(R.string.cancelImport))
                                        .setConfirmText(getResources().getString(R.string.ok))
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                break;

                            case 7:

                                sDialog.setTitleText(getResources().getString(R.string.cancel) + "!")
                                        .setContentText(getResources().getString(R.string.cancelImport))
                                        .setConfirmText(getResources().getString(R.string.ok))
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                break;

                        }




                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        switch (swith) {
                            case 0:
                                finish();
                                System.exit(0);
                                break;
                            case 1:

                                Toast.makeText(MainActivity2.this, "All Item Delete ", Toast.LENGTH_SHORT).show();
//                             progressDialog();
                                break;
                            case 2:
                                importJson sendCloud = new importJson(MainActivity2.this,"",1,fromDateString,ToDateString);

                                if(ItemCode.equals("1")) {
                                    sendCloud.startSending("GetAssest");
                                    Log.e("GetAssest","GetAssest get from server");
                                }else{
                                    sendCloud.startSending("ItemCard");

                                    Log.e("GetAssest","GetAssest not get  from server");
                                }
//                                importJson sendCloud2 = new importJson(MainActivity2.this,"");
//                                sendCloud2.startSending("ItemSwitch");

//                                importJson sendCloud3 = new importJson(MainActivity2.this,"");
//                                sendCloud3.startSending("GetStory");
                                sDialog.dismissWithAnimation();
                                break;
                            case 3:
                                importJson sendCloud2 = new importJson(MainActivity2.this,"",1,fromDateString,ToDateString);
                                sendCloud2.startSending("ItemSwitch");

//                                importJson sendCloud2 = new importJson(MainActivity2.this,"");
//                                sendCloud2.startSending("ItemSwitch");

//                                importJson sendCloud3 = new importJson(MainActivity2.this,"");
//                                sendCloud3.startSending("GetStory");
                                sDialog.dismissWithAnimation();
                                break;
                            case 4:
                                importJson sendCloud3 = new importJson(MainActivity2.this,"",1,fromDateString,ToDateString);
                                sendCloud3.startSending("GetStory");

//                                importJson sendCloud2 = new importJson(MainActivity2.this,"");
//                                sendCloud2.startSending("ItemSwitch");

//                                importJson sendCloud3 = new importJson(MainActivity2.this,"");
//                                sendCloud3.startSending("GetStory");
                                sDialog.dismissWithAnimation();
                                break;

                            case 5:
                                importJson sendCloud5 = new importJson(MainActivity2.this,"",1,fromDateString,ToDateString);
                                sendCloud5.startSending("itemUnite");

//                                importJson sendCloud2 = new importJson(MainActivity2.this,"");
//                                sendCloud2.startSending("ItemSwitch");

//                                importJson sendCloud3 = new importJson(MainActivity2.this,"");
//                                sendCloud3.startSending("GetStory");
                                sDialog.dismissWithAnimation();
                                break;

                            case 6:
                                importJson sendCloud6 = new importJson(MainActivity2.this,"",1,fromDateString,ToDateString);
                                sendCloud6.startSending("GetAssest");

                                sDialog.dismissWithAnimation();
                                break;

                            case 7:
                                importJson sendCloud7 = new importJson(MainActivity2.this,"",1,fromDateString,ToDateString);
                                sendCloud7.startSending("SyncItemQR");

                                sDialog.dismissWithAnimation();
                                break;

                        }


//                        sDialog.setTitleText(getResources().getString(R.string.delete))
//                                .setContentText(getResources().getString(R.string.rowdelete))
//                                .setConfirmText(getResources().getString(R.string.ok))
//                                .showCancelButton(false)
//                                .setCancelClickListener(null)
//                                .setConfirmClickListener(null)
//                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    }
                })
                .show();


    }


    void DateDialog(final int cases , final int y, final String isAssest) {

        final TextView fromDateText = new TextView(MainActivity2.this);
        final TextView toDateText = new TextView(MainActivity2.this);

        final TextView fromDate = new TextView(MainActivity2.this);
        final TextView toDate = new TextView(MainActivity2.this);
//        final TextView textView = new TextView(CollectingData.this);
        //fromDate.setHint(MainActivity2.this.getResources().getString(R.string.enter_location));
        fromDate.setTextColor(Color.BLACK);
        toDate.setTextColor(Color.BLACK);
        toDateText.setTextColor(Color.BLACK);
        fromDateText.setTextColor(Color.BLACK);

        fromDate.setGravity(Gravity.CENTER);
        toDate.setGravity(Gravity.CENTER);

        if (SweetAlertDialog.DARK_STYLE) {
            fromDate.setTextColor(Color.BLACK);
        }

        toDateText.setText(MainActivity2.this.getResources().getString(R.string.toDate));
        fromDateText.setText(MainActivity2.this.getResources().getString(R.string.fromDate));
        fromDate.setPadding(10,10,10,10);
        toDate.setPadding(10,10,10,10);

        LinearLayout linearLayoutHF = new LinearLayout(getApplicationContext());
        linearLayoutHF.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutHF.addView(fromDateText);
        linearLayoutHF.addView(fromDate);
        linearLayoutHF.setPadding(10,10,10,10);

        LinearLayout linearLayoutHT = new LinearLayout(getApplicationContext());
        linearLayoutHT.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutHT.addView(toDateText);
        linearLayoutHT.addView(toDate);
        linearLayoutHT.setPadding(10,10,10,10);


        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(linearLayoutHF);
        linearLayout.addView(linearLayoutHT);

        fromDateString=today;
        ToDateString=today;
        fromDate.setText(today);
        toDate.setText(today);
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateClick(fromDate);
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateClick(toDate);
            }
        });

        final SweetAlertDialog dialog = new SweetAlertDialog(MainActivity2.this, SweetAlertDialog.NORMAL_TYPE);
        dialog.setTitleText(MainActivity2.this.getResources().getString(R.string.location));
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                String from = fromDate.getText().toString();
                String to = toDate.getText().toString();
//                        textView.setText("");
                if (!from.equals("")&&!to.equals("")) {

                    fromDateString=from;
                    ToDateString=to;

                    switch (cases){
                        case 1:
                             alertMessageDialog(getResources().getString(R.string.importData), getResources().getString(R.string.importDataMessage), 2, "",isAssest);

                            break;
                        case 2:
                            alertMessageDialog(getResources().getString(R.string.importData), getResources().getString(R.string.importDataMessage), y, "", "");
                            break;
                    }

                        dialog.dismissWithAnimation();



                } else {
                    Toast.makeText(MainActivity2.this, "Please Enter from Date And To Date ", Toast.LENGTH_SHORT).show();
                }

            }
        });

//                        .hideConfirmButton();

        dialog.setCustomView(linearLayout);
        dialog.show();

    }


    public void DateClick(TextView dateText){

        new DatePickerDialog(MainActivity2.this, openDatePickerDialog(dateText), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final TextView DateText) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                DateText.setText(sdf.format(myCalendar.getTime()));
            }

        };
        return date;
    }
    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

}
