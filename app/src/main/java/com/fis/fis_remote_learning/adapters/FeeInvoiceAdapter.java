package com.fis.fis_remote_learning.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fis.fis_remote_learning.R;
import com.fis.fis_remote_learning.models.InvoiceFee;

import java.text.ParseException;
import java.util.List;

import static com.fis.fis_remote_learning.utils.Utils.convertCapitalText;
import static com.fis.fis_remote_learning.utils.Utils.convertDate;

public class FeeInvoiceAdapter extends RecyclerView.Adapter<FeeInvoiceAdapter.invoiceViewHolder> {
    private Context context;
    private List<InvoiceFee> invoiceFeeList;

    public FeeInvoiceAdapter(Context context, List<InvoiceFee> invoiceFeeList){
        this.context = context;
        this.invoiceFeeList = invoiceFeeList;
    }

    @NonNull
    @Override
    public invoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fee_invoice_list, null);
        return new FeeInvoiceAdapter.invoiceViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull invoiceViewHolder holder, int position){
        final  InvoiceFee invoiceFee =invoiceFeeList.get(position);
        try {
            holder.FeesDate.setText(context.getString(R.string.date_paid)+" "+convertDate(invoiceFee.getTxnDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.FeesAmount.setText(context.getString(R.string.payments)+" "+invoiceFee.getPayments());
        holder.FeesDebit.setText(context.getString(R.string.debit)+" "+invoiceFee.getDebit());
        holder.FeesDesc.setText(convertCapitalText(invoiceFee.getDescription()));

    }
    @Override
    public  int getItemCount() {
        return  invoiceFeeList.size();
    }
    class invoiceViewHolder extends RecyclerView.ViewHolder{

        TextView FeesDate,FeesAmount,FeesDebit,FeesPeriod,FeesDesc;

        public invoiceViewHolder(View itemView) {
            super(itemView);
            FeesDate=itemView.findViewById(R.id.Platform);
            FeesAmount=itemView.findViewById(R.id.fees_amount);
            FeesDesc=itemView.findViewById(R.id.fees_description);
            FeesDebit=itemView.findViewById(R.id.Username);
        }
    }
}
