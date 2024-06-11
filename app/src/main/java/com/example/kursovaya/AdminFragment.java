package com.example.kursovaya;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminFragment extends Fragment {

    private FirebaseFirestore db;
    private List<User> userList;
    private UserAdapter adapter;
    private boolean isSoundEnabled = false;
    private Intent serviceIntent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin, container, false);

        db = FirebaseFirestore.getInstance();
        ListView listView = rootView.findViewById(R.id.listView);
        Button logout = rootView.findViewById(R.id.logout);
        SwitchCompat switchSound = rootView.findViewById(R.id.switch_sound);
        ImageView imageView = rootView.findViewById(R.id.imageView);

        serviceIntent = new Intent(getActivity(), MediaService.class);

        logout.setOnClickListener(v -> {
            getActivity().stopService(serviceIntent);
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        switchSound.setChecked(isSoundEnabled);
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isSoundEnabled = isChecked;
            if (isChecked) {
                getActivity().startService(serviceIntent);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_animation);
                imageView.setVisibility(View.VISIBLE);
                imageView.startAnimation(animation);
            } else {
                getActivity().stopService(serviceIntent);
                imageView.clearAnimation();
                imageView.setVisibility(View.GONE);
            }
        });

        userList = new ArrayList<>();
        adapter = new UserAdapter(getActivity(), userList);
        listView.setAdapter(adapter);

        loadUsers();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            User user = userList.get(position);
            if (user.getEmail().equals("sonvhx@icloud.com")) {
                Toast.makeText(getActivity(), "Невозможно заблокировать администратора", Toast.LENGTH_SHORT).show();
            } else {
                if (user.isBlocked()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Разблокировка пользователя")
                            .setMessage("Вы уверены, что хотите разблокировать этого пользователя?")
                            .setPositiveButton("Да", (dialog, which) -> unblockUser(user))
                            .setNegativeButton("Отмена", null)
                            .show();
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Блокировка пользователя")
                            .setMessage("Вы уверены, что хотите заблокировать этого пользователя?")
                            .setPositiveButton("Да", (dialog, which) -> blockUser(user))
                            .setNegativeButton("Отмена", null)
                            .show();
                }
            }
        });

        return rootView;
    }

    private void unblockUser(User user) {
        db.collection("Users").document(user.getUserId())
                .update("blocked", false)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Пользователь разблокирован", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Ошибка при разблокировке пользователя", Toast.LENGTH_SHORT).show());
    }

    private void blockUser(User user) {
        db.collection("Users").document(user.getUserId())
                .update("blocked", true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getActivity(), "Пользователь заблокирован", Toast.LENGTH_SHORT).show();
                    loadUsers();
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Ошибка при блокировке пользователя", Toast.LENGTH_SHORT).show());
    }

    private void loadUsers() {
        db.collection("Users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String email = document.getString("Email");
                    String userId = document.getId();

                    Boolean blocked = document.getBoolean("blocked");
                    boolean isBlocked = blocked != null && blocked;

                    User user = new User(email, userId);
                    user.setBlocked(isBlocked);
                    userList.add(user);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "Ошибка загрузки пользователей!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

