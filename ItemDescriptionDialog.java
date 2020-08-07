package com.example.foodapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.example.foodapp.FoodieDashboard;
import com.example.foodapp.R;

public class ItemDescriptionDialog extends Dialog {

    FoodieDashboard activity;
    String name,desc;

    public ItemDescriptionDialog(FoodieDashboard activity,String name, String desc) {
        super(activity);
        this.activity=activity;
        this.name=name;
        this.desc=desc;
    }


    TextView txt_itemname,txt_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_item_description);

        txt_itemname=(TextView) findViewById(R.id.txt_itemname);
        txt_description=(TextView) findViewById(R.id.txt_description);

        txt_itemname.setText(name);
        txt_description.setText(desc);

    }
}
