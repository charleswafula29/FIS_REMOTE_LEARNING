package com.fairmontsintenational.fis_remote_learning.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fairmontsintenational.fis_remote_learning.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {
    private View root;
    Context mContext;

    public StudentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_student_profile, container, false);

        TextView Email = root.findViewById(R.id.StudentEmail);
        TextView EmailPassword = root.findViewById(R.id.StudentPassword);
        TextView Names  = root.findViewById(R.id.UserNames);
        TextView AccountStatus  = root.findViewById(R.id.AccountStatus);


        return  root;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}
