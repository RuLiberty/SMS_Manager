package ru.geekbrains.sms_manager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.Realm;

public class AdapterSmsManager extends RecyclerView.Adapter<AdapterSmsManager.ViewHolder> {

    @NonNull
    @Override
    public AdapterSmsManager.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_recycler_view, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSmsManager.ViewHolder viewHolder, int position) {
        viewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int size = realm.where(SMSTable.class).findAll().size();
        realm.commitTransaction();
        realm.close();
        return size;
    }

    public void adapterNotifyDataSetChange(){
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView from;
        private TextView message;
        private TextView statusInOut;
        private SMSTable note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.text_from);
            message = itemView.findViewById(R.id.text_message);
            statusInOut = itemView.findViewById(R.id.type_SMS);
        }

        public void bind(int idx){
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            SMSTable st = realm.where(SMSTable.class).findAll().get(idx);
            this.note = new SMSTable(st.getFrom(), st.getMessage(), st.getStatusInOut());
            realm.commitTransaction();
            from.setText("Сообщение от: " + this.note.getFrom());
            message.setText("Текст сообщения: " + this.note.getMessage());
            statusInOut.setText(this.note.getStatusInOut());
            realm.close();
        }

    }
}
