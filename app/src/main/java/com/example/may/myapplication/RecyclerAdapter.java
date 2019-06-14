package com.example.may.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.may.myapplication.device.Aieconditioner;
import com.example.may.myapplication.device.Co2;
import com.example.may.myapplication.device.Curtain;
import com.example.may.myapplication.device.Device;
import com.example.may.myapplication.device.Hcho;
import com.example.may.myapplication.device.Ill;
import com.example.may.myapplication.device.Pm;
import com.example.may.myapplication.device.Set;
import com.example.may.myapplication.device.Socket;
import com.example.may.myapplication.device.Switch;
import com.example.may.myapplication.device.TH;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<Device> deviceList;
    private Resources resources;
    private LayoutInflater inflater;

    public RecyclerAdapter(Context context, List<Device> itemList) {
        this.context = context;
        this.deviceList = itemList;
        this.resources = context.getResources();
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_recyclerview, parent, false);
        final MyViewHolder holder = new MyViewHolder(view);
        holder.deviceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                Device device = deviceList.get(position);
                if (device instanceof Co2) {
                    Intent intent = new Intent(context, Co2Activity.class);
                    Co2 co2 = (Co2) device;
                    intent.putExtra("c",co2.getCo2());
                    context.startActivity(intent);
                } else if (device instanceof Curtain) {
                    Intent intent = new Intent(context, CurtainActivity.class);
                    context.startActivity(intent);
                } else if (device instanceof Hcho) {
                    Intent intent = new Intent(context, HchoActivity.class);
                    Hcho hcho = (Hcho) device;
                    intent.putExtra("hcho",hcho.getHcho());
                    context.startActivity(intent);
                } else if (device instanceof Ill) {
                    Intent intent = new Intent(context, IllActivity.class);
                    Ill ill = (Ill) device;
                    intent.putExtra("ill",ill.getIll());
                    context.startActivity(intent);
                } else if (device instanceof Pm) {
                    Intent intent = new Intent(context, PmActivity.class);
                    Pm pm = (Pm) device;
                    intent.putExtra("p",pm.getPm());
                    context.startActivity(intent);
                } else if (device instanceof Socket) {
                    Intent intent = new Intent(context, SocketActivity.class);
                    context.startActivity(intent);
                } else if (device instanceof Switch) {
                    Intent intent = new Intent(context, SwitchActivity.class);
                    context.startActivity(intent);
                } else if (device instanceof TH) {
                    Intent intent = new Intent(context, THActivity.class);
                    TH th = (TH) device;
                    intent.putExtra("t", th.getT());
                    intent.putExtra("h", th.getH());
                    context.startActivity(intent);
                }else if (device instanceof Aieconditioner) {
                    Intent intent = new Intent(context, AieconditionerActivity.class);
                    context.startActivity(intent);
                } else if (device instanceof Set) {
                    Intent intent = new Intent(context, SetActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Device device = deviceList.get(position);
        holder.title.setText(device.getName());
        holder.Viewimage.setImageResource(device.getImage());


    }

    @Override
    public int getItemCount() {
        return null == deviceList ? 0 : deviceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        View deviceView;
        ImageView Viewimage;

        public MyViewHolder(View view) {

            super(view);
            deviceView = view;
            title = view.findViewById(R.id.item_title);
            Viewimage = (ImageView) view.findViewById(R.id.item_image);
        }
    }


}
