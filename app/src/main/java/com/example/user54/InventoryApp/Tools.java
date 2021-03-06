package com.example.user54.InventoryApp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.user54.InventoryApp.Model.AssestItem;
import com.example.user54.InventoryApp.Model.ItemCard;
import com.example.user54.InventoryApp.Model.ItemInfo;
import com.example.user54.InventoryApp.Model.MainSetting;
import com.example.user54.InventoryApp.Model.Password;
import com.example.user54.InventoryApp.Model.Stk;
import com.example.user54.InventoryApp.Model.TransferItemsInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Tools extends AppCompatActivity {
    //    LinearLayout   pSetting, changePassword, sendServer, receiveServer;
    LinearLayout sendServer, mainSetting, ExportDb, importDb;

    Dialog dialog;

    InventoryDatabase InventDB;
    ArrayList<Password> passImport;
    TextView home;
    Animation animFadein;
    SweetAlertDialog dialogSwite;
    boolean isPermition=false;
    int flagINoUT=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(controll.isYellow){
        setContentView(R.layout.tools_yellow);
//        }else{
//            setContentView(R.layout.tools_menu2);
//        }


        InventDB = new InventoryDatabase(Tools.this);
        passImport = new ArrayList<>();
        initialization();


        animFadein = AnimationUtils.loadAnimation(Tools.this, R.anim.fade_in);
//        pSetting.startAnimation(animFadein);
//        changePassword.startAnimation(animFadein);
        sendServer.startAnimation(animFadein);
        mainSetting.startAnimation(animFadein);
        ExportDb.startAnimation(animFadein);
        importDb.startAnimation(animFadein);
//        receiveServer.startAnimation(animFadein);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


    View.OnClickListener showDialogOnClick = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

//                case R.id.password:
//                    showChangePasswordDialog();
//                    break;
//                case R.id.pSetting:
//                    showPrintSettingDialog();
//                    break;
                case R.id.sendSer:
//                    importJson sendCloud = new importJson(Tools.this,"");
//                    sendCloud.startSending("GetStory");
                    List<MainSetting> mainSetting = InventDB.getAllMainSetting();
                    if (mainSetting.size() != 0) {

                        List<ItemInfo> itemInfos = InventDB.getAllItemInfo();
                        List<TransferItemsInfo> itemTransInfos = InventDB.getAllTransferItemInfo();
                        List<AssestItem> itemTransAssets = InventDB.getAllAssesstItemInfo();
                        List<ItemCard> itemTransItemCard = InventDB.getAllItemCardNotExport();

                        List<ItemInfo> itemInfoBackup = InventDB.getAllItemInfoBackUp();

                        sendToServer(itemInfos, itemTransInfos, itemTransAssets, mainSetting.get(0).getIsAssest(),itemTransItemCard,itemInfoBackup);

                    } else {

                        new SweetAlertDialog(Tools.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getResources().getString(R.string.mainSetting) + "!")
                                .setContentText(getResources().getString(R.string.nomainSetting))
                                .setConfirmText(getResources().getString(R.string.cancel))
                                .showCancelButton(false)
                                .setCancelClickListener(null)
                                .setConfirmClickListener(null).show();


                    }


//                    showSendToServerDialog();
                    break;
//                case R.id.reciveSer:
//                    showReceiveFromServerDialog();
//                    break;
                case R.id.mainSetting:
                    showReceiveFromServerDialog();
                    break;

                case R.id.ExportDb:
                    flagINoUT=1;
                    isPermition= isStoragePermissionGranted();
                    if(isPermition) {
                        ExportDbToExternal();
                    }
                    break;

                case R.id.importDb:
                    flagINoUT=2;
                    isPermition= isStoragePermissionGranted();
                    if(isPermition) {
                    ImportDbToMyApp();
                    }
                    break;

            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    void ExportDbToExternal(){
        dialogSwite = new SweetAlertDialog(Tools.this, SweetAlertDialog.PROGRESS_TYPE);
        dialogSwite.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialogSwite.setTitleText("Export Db To External");
        dialogSwite.setCancelable(false);
        dialogSwite.show();
        File Db = new File("/data/data/com.example.user54.InventoryApp/databases/InventoryDBase");

        String dstPath =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "InventoryDBFolder"  ;
        File dst = new File(dstPath);
        Log.e("myAppDataBasedstPath"," "+dstPath);
        Log.e("myAppDataBaseDb"," "+Db);
        try {
           exportFile(Db, dst);
//            copyFiles(Db,dst);
        } catch (IOException e) {
            e.printStackTrace();
            dialogSwite.dismissWithAnimation();
        }
    }

    void ImportDbToMyApp(){

        dialogSwite = new SweetAlertDialog(Tools.this, SweetAlertDialog.PROGRESS_TYPE);
        dialogSwite.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialogSwite.setTitleText("Import Db To MyApp");
        dialogSwite.setCancelable(false);
        dialogSwite.show();

        File Dba = new File("/data/data/com.example.user54.InventoryApp/databases");

        String dstPathw =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "InventoryDBFolder" + File.separator;
        File dsts = new File(dstPathw+"InventoryDBase");
        Log.e("myAppDataBasedstPath"," "+dstPathw);
        Log.e("myAppDataBaseDb"," "+Dba);
        try {
            exportFile( dsts,Dba);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void alertMessageDialog(String title, String message, final int swith, final String itemName, final String ItemCode) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Tools.this);
        dialog.setTitle(title)
                .setMessage(message)
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        switch (swith) {
                            case 0:
                                finish();
                                moveTaskToBack(true);
                                break;
//                            case 1:
//                                InventDB.deleteAllItem("ITEM_CARD");
//                                Toast.makeText(CollectingData.this, "All Item Delete ", Toast.LENGTH_SHORT).show();
////                             progressDialog();
//                                break;
//                            case 2:
//                                InventDB.delete(ItemCode,itemName);
//                                break;

                        }
                    }
                }).show();
    }


    void showChangePasswordDialog() {
        dialog = new Dialog(Tools.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        if (controll.isYellow) {
            dialog.setContentView(R.layout.change_password_yellow1);
        } else {
            dialog.setContentView(R.layout.change_password);
        }

        dialog.setCanceledOnTouchOutside(false);


        Button exit, update, reset;
        final EditText oldPass, newPass;

        oldPass = (EditText) dialog.findViewById(R.id.oldPass);
        newPass = (EditText) dialog.findViewById(R.id.newPass);

        exit = (Button) dialog.findViewById(R.id.exit);
        update = (Button) dialog.findViewById(R.id.updaCh);
        reset = (Button) dialog.findViewById(R.id.reset);

        final boolean[] passFound = {false};
        final boolean[] passFoundNew = {false};

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passFound[0] = false;
                passFoundNew[0] = false;
                passImport = InventDB.getAllPassword();

                if (!oldPass.getText().toString().equals("") && !newPass.getText().toString().equals("")) {

                    for (int i = 0; i < passImport.size(); i++) {
                        if (oldPass.getText().toString().equals(passImport.get(i).getPasswords())) {

                            passFound[0] = true;
                            break;
                        }
                    }


                    for (int x = 0; x < passImport.size(); x++) {

                        if (newPass.getText().toString().equals(passImport.get(x).getPasswords())) {

                            passFoundNew[0] = true;
                            break;

                        }

                    }

                    if (passFound[0] && !passFoundNew[0]) {

                        InventDB.updatePasswordTable(newPass.getText().toString(), "0", oldPass.getText().toString());
//                        Toast.makeText(Tools.this, getResources().getString(R.string.upsucess), Toast.LENGTH_SHORT).show();
                        TostMesage(getResources().getString(R.string.upsucess));
                    } else {

//                        Toast.makeText(Tools.this,  getResources().getString(R.string.oldornewnotcorect), Toast.LENGTH_SHORT).show();
                        TostMesage(getResources().getString(R.string.oldornewnotcorect));
                    }

                } else {
//                    Toast.makeText(Tools.this,  getResources().getString(R.string.insertData), Toast.LENGTH_SHORT).show();
                    TostMesage(getResources().getString(R.string.insertData));
                }

            }
        });


        dialog.show();
    }


    void showPrintSettingDialog() {
        dialog = new Dialog(Tools.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_collecting_data);
        dialog.setCanceledOnTouchOutside(false);

        Button exit;

        exit = (Button) dialog.findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    void showSendToServerDialog() {
        dialog = new Dialog(Tools.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_collecting_data);
        dialog.setCanceledOnTouchOutside(false);

        Button exit;

        exit = (Button) dialog.findViewById(R.id.exit);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    void showReceiveFromServerDialog() {
        final Dialog MainSettingdialog = new Dialog(Tools.this,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        MainSettingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MainSettingdialog.setCancelable(false);
        MainSettingdialog.setContentView(R.layout.main_setting_dialog_new);
        MainSettingdialog.setCanceledOnTouchOutside(false);

        LinearLayout exit, save;

        final EditText companyNo=MainSettingdialog.findViewById(R.id.companyNo);
        final CheckBox onlinePrice =MainSettingdialog.findViewById(R.id.onlineCheckBox);
        final TextView StoreName = MainSettingdialog.findViewById(R.id.StoreName);
        final EditText ipSetting = MainSettingdialog.findViewById(R.id.ipSetting);
        final CheckBox AssestCheckBox = MainSettingdialog.findViewById(R.id.AssestCheckBox);
        final CheckBox QrCheckBox = MainSettingdialog.findViewById(R.id.QrCheckBox);
        exit = MainSettingdialog.findViewById(R.id.exit);
        save = MainSettingdialog.findViewById(R.id.saveSetting);
        String StkNo = "";

        final Spinner stkSpinner = MainSettingdialog.findViewById(R.id.spinner);
        ArrayAdapter<String> StkAdapter = null;


        final List<MainSetting> mainSettings = InventDB.getAllMainSetting();
        final List<Stk> STKList = InventDB.getAllStk();
        final List<String> StokNo = new ArrayList<>();
        if (STKList.size() != 0) {
            StokNo.clear();
            for (int i = 0; i < STKList.size(); i++) {
                StokNo.add(STKList.get(i).getStkNo());
            }

            StkAdapter = new ArrayAdapter<String>(Tools.this, R.layout.spinner_style, StokNo);
            stkSpinner.setAdapter(StkAdapter);


        }


        stkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                StoreName.setText(STKList.get(position).getStkName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mainSettings.size() != 0) {
            ipSetting.setText(mainSettings.get(0).getIP());
            StoreName.setText(InventDB.getStkName(mainSettings.get(0).getStorNo()));

            companyNo.setText(mainSettings.get(0).getCompanyNo());
            if (mainSettings.get(0).getOnlinePrice().equals("1")) {
                onlinePrice.setChecked(true);
            } else {
                onlinePrice.setChecked(false);
            }

            if (mainSettings.get(0).getIsAssest().equals("1")) {
                AssestCheckBox.setChecked(true);
            } else {
                AssestCheckBox.setChecked(false);
            }
            if (mainSettings.get(0).getIsQr().equals("1")) {
                QrCheckBox.setChecked(true);
            } else {
                QrCheckBox.setChecked(false);
            }
            int index = StokNo.indexOf(mainSettings.get(0).getStorNo());
//            StkNo = StkAdapter.getItem(index);
            Log.e("indexofSpinner = ", "= " + index + "   " + StkNo + "    " + mainSettings.get(0).getStorNo());
            stkSpinner.setSelection(index);
        }else {
            companyNo.setText("290");
        }
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainSettingdialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ipSetting.getText().toString().equals("")) {
                    InventDB.deleteAllItem("MAIN_SETTING_TABLE");
                    String Store = "0";
                    String isAssest = "0";//0->not 1-->assest
                    String isQr="0";//0->not 1-->QR
                    String isOnline="0";//0->not 1-->online
                    if (StokNo.size() != 0) {
                        Store = stkSpinner.getSelectedItem().toString();
                    } else {
                        Store = "0";
                    }

                    if (AssestCheckBox.isChecked()) {
                        isAssest = "1";
                    } else {
                        isAssest = "0";
                    }


                    if (QrCheckBox.isChecked()) {
                        isQr = "1";
                    } else {
                        isQr = "0";
                    }
                    if (onlinePrice.isChecked()) {
                        isOnline = "1";
                    } else {
                        isOnline = "0";
                    }

                    InventDB.addAllMainSetting(new MainSetting(ipSetting.getText().toString(), Store, isAssest,isQr,isOnline,companyNo.getText().toString()));
//                    Toast.makeText(Tools.this, getResources().getString(R.string.saveMainSetting), Toast.LENGTH_SHORT).show();
                    MainSettingdialog.dismiss();

                    new SweetAlertDialog(Tools.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(getResources().getString(R.string.sucess))
                            .setContentText(getResources().getString(R.string.saveMainSetting))
                            .show();


                }
            }
        });

        MainSettingdialog.show();
    }


    public void notClickable() {

//        pSetting.setClickable(false);
//        changePassword.setClickable(false);
        sendServer.setClickable(false);
//        receiveServer.setClickable(false);
        mainSetting.setClickable(false);
        ExportDb.setClickable(false);
        importDb.setClickable(false);

    }

    public void Clickable() {

//        pSetting.setClickable(true);
//        changePassword.setClickable(true);
        sendServer.setClickable(true);
//        receiveServer.setClickable(true);
        mainSetting.setClickable(true);
        ExportDb.setClickable(true);
        importDb.setClickable(true);

    }

    void TostMesage(String message) {

        SweetAlertDialog sd2 = new SweetAlertDialog(this);
        sd2.setCancelable(true);
        sd2.setCanceledOnTouchOutside(true);
        sd2.setContentText(message);
        sd2.hideConfirmButton();
        sd2.show();


    }

    void sendToServer(List<ItemInfo> itemInfo, List<TransferItemsInfo> transferItemsInfos, List<AssestItem> AssestItem, String isAssest, List<ItemCard>itemCard ,List<ItemInfo> itemInfosBackup) {
        if (itemInfo.size() != 0 || transferItemsInfos.size() != 0 || AssestItem.size() != 0 || itemInfosBackup.size() != 0) {
            boolean isExported = false, isExportedTranse = false,isExportItemCard=false,isExportedBackup = false;
            try {

                JSONArray obj2 = new JSONArray();
                for (int i = 0; i < itemInfo.size(); i++) {
                    if (Integer.parseInt(itemInfo.get(i).getIsExport()) != 1) {
                        Log.e("ExportData_", itemInfo.get(i).getSalePrice() + "");
                        obj2.put(itemInfo.get(i).getJSONObject2());
                        isExported = true;
                    }
                }


                JSONObject obj = new JSONObject();
                obj.put("JRD", obj2);


                Log.e("ExportDataJrd_=", obj2.toString());

                Log.e("ExportData_", obj.toString());

                if (isExported) {
                    ExportJeson exportJeson = new ExportJeson(Tools.this, obj);
                    exportJeson.startSending("ExportData");
                } else {
//                    Toast.makeText(Tools.this, getResources().getString(R.string.allExport), Toast.LENGTH_SHORT).show();
                    TostMesage(getResources().getString(R.string.allExport));

                }

                //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                JSONArray obj2Backup = new JSONArray();
                for (int i = 0; i < itemInfosBackup.size(); i++) {
                    if (Integer.parseInt(itemInfosBackup.get(i).getIsExport()) != 1) {
                        Log.e("ExportData_Backup", itemInfosBackup.get(i).getSalePrice() + "");
                        obj2Backup.put(itemInfosBackup.get(i).getJSONObjectBacup());
                        isExportedBackup = true;
                    }
                }


                JSONObject objBackup = new JSONObject();
                objBackup.put("JRD", obj2Backup);


                Log.e("ExportDataBackup=", obj2Backup.toString());

                Log.e("ExportDataBackup", objBackup.toString());

                if (isExportedBackup) {
                    ExportJeson exportJeson = new ExportJeson(Tools.this, objBackup);
                    exportJeson.startSending("ExportDataBackup");
                } else {
//                    Toast.makeText(Tools.this, getResources().getString(R.string.allExport), Toast.LENGTH_SHORT).show();
                    TostMesage(getResources().getString(R.string.allExport)+"Backup");

                }

                //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


////////////////////Item Card new trans begin

//                JSONArray objItemCard2 = new JSONArray();
//                for (int i = 0; i < itemCard.size(); i++) {
//                        objItemCard2.put(itemCard.get(i).getJSONObject2());
//                        isExportItemCard = true;
//                }
//
//
//                JSONObject objItemCard = new JSONObject();
//                objItemCard.put("JRD", objItemCard2);
//
//
//                Log.e("ExportDataJrd_=", objItemCard2.toString());
//
//                Log.e("ExportData_", objItemCard2.toString());
//
//                if (isExportItemCard) {
//                    ExportJeson exportJesonItemCard = new ExportJeson(Tools.this, objItemCard);
//                    exportJesonItemCard.startSending("ExportItemCard");
//                } else {
////                    Toast.makeText(Tools.this, getResources().getString(R.string.allExport), Toast.LENGTH_SHORT).show();
//                    TostMesage(getResources().getString(R.string.allExport));
//
//                }

////////////////////Item Card new trans end

/////////////////////trans begin

//
//                JSONArray objTrans = new JSONArray();
//                for (int t = 0;t< transferItemsInfos.size(); t++) {
//                    if (Integer.parseInt(transferItemsInfos.get(t).getIsExport())!=1) {
//                        objTrans.put(transferItemsInfos.get(t).getJSONObjectTranse());
//                        isExportedTranse=true;
//                    }
//                }
//
//
//                JSONObject objTrans1 = new JSONObject();
//                objTrans1.put("TRNS", objTrans);
//
//
//                if(isExportedTranse){
//                    ExportJeson exportJesons = new ExportJeson(Tools.this, objTrans1);
//                    exportJesons.startSending("ExportTransferData");
//                }else{
////                    Toast.makeText(Tools.this,  getResources().getString(R.string.allTexoprt), Toast.LENGTH_SHORT).show();
//                    TostMesage(getResources().getString(R.string.allTexoprt));
//                }
/////////////////////trans end

                if (isAssest.equals("1")) {
                    boolean isExportedTranseAss = false;
                    JSONArray ObjAssest = new JSONArray();
                    for (int t = 0; t < AssestItem.size(); t++) {
                        if (Integer.parseInt(AssestItem.get(t).getIsExport()) != 1) {
                            ObjAssest.put(AssestItem.get(t).getJSONObjectAssets());
                            isExportedTranseAss = true;
                        }
                    }


                    JSONObject objTransAssest = new JSONObject();
                    objTransAssest.put("JRD", ObjAssest);

                    Log.e("Assets", "send" + objTransAssest.toString());
                    if (isExportedTranseAss) {
                        ExportJeson exportJesonsA = new ExportJeson(Tools.this, objTransAssest);
                        exportJesonsA.startSending("ExportAssets");
                    } else {
                        TostMesage(getResources().getString(R.string.allAssestExport));
                    }

                }


                Log.e("ExportData_", "send");
            } catch (JSONException e) {
                Log.e("Tag", "JSONException");
            }
        } else {
            TostMesage(getResources().getString(R.string.noexport));
        }
    }

    void initialization() {

        home = (TextView) findViewById(R.id.home);

//        pSetting = (LinearLayout) findViewById(R.id.pSetting);
//        changePassword = (LinearLayout) findViewById(R.id.password);
        sendServer = (LinearLayout) findViewById(R.id.sendSer);
        mainSetting = (LinearLayout) findViewById(R.id.mainSetting);
        ExportDb= (LinearLayout) findViewById(R.id.ExportDb);
        importDb= (LinearLayout) findViewById(R.id.importDb);
//        receiveServer = (LinearLayout) findViewById(R.id.reciveSer);


//        pSetting.setOnClickListener(showDialogOnClick);
//        changePassword.setOnClickListener(showDialogOnClick);
        sendServer.setOnClickListener(showDialogOnClick);
        mainSetting.setOnClickListener(showDialogOnClick);
        ExportDb.setOnClickListener(showDialogOnClick);
        importDb.setOnClickListener(showDialogOnClick);

//        receiveServer.setOnClickListener(showDialogOnClick);

        Clickable();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void copyFiles(File src,  File dst) throws IOException{
//        Path src = Paths.get(from);
//        Path dest = Paths.get(to);

//        FileInputStream var2 = new FileInputStream(src);
        FileOutputStream var3 = new FileOutputStream(dst);
        Files.copy(Paths.get(src.getPath()), var3);
        dialogSwite.dismissWithAnimation();
    }

    public void deleteFiles(String path) {
        File file = new File(path);

        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {

            }
        }

    }

    private File exportFile(File src, File dst) throws IOException {

        //if folder does not exist

//        deleteFiles(dst.getPath() + File.separator + "InventoryDBase");
        if (!dst.exists()) {
            if (!dst.mkdir()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File expFile = new File(dst.getPath() + File.separator + "InventoryDBase" );

        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(expFile).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }catch (Exception e){

            if(flagINoUT==1){
                new SweetAlertDialog(Tools.this, SweetAlertDialog.ERROR_TYPE)//Tools.this.getResources().getString(R.string.save_SUCCESS)
                        .setTitleText("Exception ")
                        .setContentText("Exception Error in read File"+e.toString() +" DataBaseFile ")
                        .show();
            }else if(flagINoUT==1){
                new SweetAlertDialog(Tools.this, SweetAlertDialog.ERROR_TYPE)//Tools.this.getResources().getString(R.string.save_SUCCESS)
                        .setTitleText("Exception ")
                        .setContentText("Exception Error in read File"+e.toString() +"External File")
                        .show();
            }


        }
         finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }

        dialogSwite.dismissWithAnimation();
        return expFile;
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("gg1","Permission is granted");
                return true;
            } else {

                Log.e("gg2","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.e("gg3","Permission is granted");
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.e("jj4","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
         if(flagINoUT==1){
             ExportDbToExternal();
         }else if (flagINoUT==2){
             ImportDbToMyApp();
         }


        }
    }


}
