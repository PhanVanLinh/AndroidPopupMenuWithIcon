package com.toong.androidpopupmenuwithicon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<PopupMenuHelper.PopupMenuItem> items = new ArrayList<>();

        items.add(new PopupMenuHelper.PopupMenuItem("a", R.drawable.ic_launcher));
        items.add(new PopupMenuHelper.PopupMenuItem("b", R.drawable.ic_launcher));
        items.add(new PopupMenuHelper.PopupMenuItem("c", R.drawable.ic_launcher));
        items.add(new PopupMenuHelper.PopupMenuItem("d", R.drawable.ic_launcher));

        final Button btn = (Button) findViewById(R.id.show);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPopup(btn);

                new PopupMenuHelper().show(v, items, Gravity.START, new PopupMenuHelper.ItemClickListener(){

                    @Override
                    public void onItemClick(String item) {

                    }
                });
            }
        });
    }

    private void showPopup(final View anchor) {
        PopupMenu popupMenu = new PopupMenu(anchor.getContext(), anchor, Gravity.START);
        popupMenu.getMenu().add(R.id.menuGroup, R.id.menu1, Menu.NONE, "slot1").setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        popupMenu.getMenu().add(R.id.menuGroup, R.id.menu1, Menu.NONE,"slot2");
        popupMenu.getMenu().add(R.id.menuGroup, R.id.menu1, Menu.NONE,"slot3").setIcon(R.mipmap.ic_launcher);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(anchor.getContext(), item.getTitle() + "clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // This will help to show icon in dialog
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.show();
    }
}
