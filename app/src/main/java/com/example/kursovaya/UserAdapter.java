package com.example.kursovaya;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import android.content.Context;
import android.widget.BaseAdapter;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        TextView emailTextView = convertView.findViewById(R.id.emailTextView);
        TextView userIdTextView = convertView.findViewById(R.id.userIdTextView);
        ImageView blockIconImageView = convertView.findViewById(R.id.blockIconImageView);

        User user = userList.get(position);

        emailTextView.setText(user.getEmail());
        userIdTextView.setText(user.getUserId());

        if (user.isBlocked()) {
            blockIconImageView.setVisibility(View.VISIBLE);
        } else {
            blockIconImageView.setVisibility(View.GONE);
        }

        return convertView;
    }
}
