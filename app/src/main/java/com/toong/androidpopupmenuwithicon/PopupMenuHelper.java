package com.toong.androidpopupmenuwithicon;

import android.support.annotation.DrawableRes;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by PhanVanLinh on 12/07/2017.
 * phanvanlinh.94vn@gmail.com
 */

public class PopupMenuHelper {

    public void show(final View anchor, List<PopupMenuItem> items, int gravity, final ItemClickListener itemClickListener) {
        PopupMenu popupMenu = new PopupMenu(anchor.getContext(), anchor, gravity);

        for (int i = 0; i < items.size(); i++){
            PopupMenuItem popupMenuItem = items.get(i);
            popupMenu.getMenu().add(0, i, Menu.NONE, popupMenuItem.getTitle()).setIcon(
                    popupMenuItem.getDrawableRes());
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(anchor.getContext(), item.getTitle() + "clicked", Toast.LENGTH_SHORT)
                        .show();
                itemClickListener.onItemClick(item.getTitle().toString());

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
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons =
                            classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popupMenu.show();
    }

    public interface ItemClickListener{
        void onItemClick(String item);
    }

    public  static class PopupMenuItem {
        private String title;
        @DrawableRes
        private int drawableRes;

        public PopupMenuItem(String title, int drawableRes) {
            this.title = title;
            this.drawableRes = drawableRes;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getDrawableRes() {
            return drawableRes;
        }

        public void setDrawableRes(int drawableRes) {
            this.drawableRes = drawableRes;
        }
    }
}
