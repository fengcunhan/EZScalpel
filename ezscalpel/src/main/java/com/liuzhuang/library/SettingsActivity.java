package com.liuzhuang.library;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;

import com.liuzhuang.library.utils.EZSharedPreferenceUtil;

public class SettingsActivity extends Activity {

    private EditText editText;
    private ActivityListAdapter adapter;
    private Switch filterSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editText = (EditText) findViewById(R.id.ezscalpel_activity_name_et);
        final Button addBtn = (Button) findViewById(R.id.ezscalpel_btn);
        final ListView activityListView = (ListView) findViewById(R.id.ezscalpel_listview);
        filterSwitch = (Switch) findViewById(R.id.ezscalpel_filter_switcher);
        final ViewGroup filterArea = (ViewGroup) findViewById(R.id.ezscalpel_filter_area);

        adapter = new ActivityListAdapter(this);
        activityListView.setAdapter(adapter);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    EZSharedPreferenceUtil.addActivityName(SettingsActivity.this, name);
                    adapter.refresh();
                    editText.setText("");
                }
            }
        });

        adapter.refresh();

        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EZSharedPreferenceUtil.setSwitcherState(SettingsActivity.this, isChecked);
                if (isChecked) {
                    filterArea.setVisibility(View.INVISIBLE);
                } else {
                    filterArea.setVisibility(View.VISIBLE);
                }
            }
        });

        filterSwitch.setChecked(EZSharedPreferenceUtil.getSwitcherState(this));

    }

}
