package com.belivnat.multiselect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.belivnat.multiselect.multisearch.MultiSelectModel;
import com.belivnat.multiselect.multisearch.MultiSelectPopupFragment;

public class MainActivity extends AppCompatActivity implements MultiSelectPopupFragment.ISelectedChoice {
    Button btnShowPopup;
    TextView txtSelectedChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnShowPopup = findViewById(R.id.btn_show_popup);
        txtSelectedChoice = findViewById(R.id.txt_selected_choice);
        btnShowPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMultiSelectPopup();
            }
        });

    }

    @Override
    public void selectedChoice(MultiSelectModel multiSelectedData, int position) {
        txtSelectedChoice.setText("Selected Choice: " + multiSelectedData.dataString);
    }

    public void showMultiSelectPopup() {
        // close existing dialog fragments
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag("multi_select_popup");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        MultiSelectPopupFragment multiSelectPopupFragment = new MultiSelectPopupFragment();
        multiSelectPopupFragment.show(manager, "multi_select_popup");
    }
}
