package com.fairmontsintenational.fis_remote_learning.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fairmontsintenational.fis_remote_learning.R;
import com.fairmontsintenational.fis_remote_learning.adapters.FeeInvoiceAdapter;
import com.fairmontsintenational.fis_remote_learning.models.InvoiceFee;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeesFragment extends Fragment {
    private View root;
    RecyclerView recyclerView;
    FeeInvoiceAdapter adapter;
    List<InvoiceFee> feesClassList;
    TextView balance, total_invoiced, total_paid;
    String names;
    Button Mpesa, Statement;
    private Context mContext;

    public FeesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_fees, container, false);

        balance=root.findViewById(R.id.Fees_balance);
        total_invoiced=root.findViewById(R.id.Fees_total_invoiced);
        total_paid=root.findViewById(R.id.Fees_total_paid);
        Mpesa = root.findViewById(R.id.Fees_Mpesa);
        Statement = root.findViewById(R.id.Fees_Statement);


        recyclerView=root.findViewById(R.id.fees_logs_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        fetchfeebalance();

        Statement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(mContext, FeeStatements.class));
            }
        });

        Mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(mContext, Mpesa.class));
            }
        });

        return root;
    }

    private void fetchfeebalance() {
        feesClassList = new ArrayList<>();

        feesClassList.add(new InvoiceFee("April Monthly subscription  ", "Payments Ksh:  7,000", "Paid Ksh:  7,000", "Balance Ksh: 0",
                "Balance Ksh:  120.00"));

        feesClassList.add(new InvoiceFee("May Monthly subscription  ", "Payments Ksh:  7,000", "Paid Ksh:  7,000", "Balance Ksh: 0",
                "Balance Ksh:  0.00"));

        adapter = new FeeInvoiceAdapter(mContext, feesClassList);
        recyclerView.setAdapter(adapter);
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
