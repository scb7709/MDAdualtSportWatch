package com.headlth.management.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.headlth.management.R;
import com.headlth.management.activity.PrescriptionDetailsActivity;
import com.headlth.management.adapter.PrescriptionAdapter;
import com.headlth.management.entity.Prescription;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 2016/9/28.
 */
@ContentView(R.layout.fragment_prescription)
public class MyPrescriptionFragment extends BaseFragment {
    @ViewInject(R.id.fragment_presciption_listview)
    private ListView fragment_presciption_listview;
    private String Prescription;
    private PrescriptionAdapter prescriptionAdapter;

    @SuppressLint("ValidFragment")
    public MyPrescriptionFragment() {
    }
    @SuppressLint("ValidFragment")
    public MyPrescriptionFragment(String  Prescription) {
        this.Prescription = Prescription;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return x.view().inject(this, inflater, container);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
      /*  List<Prescription> PrescriptionList=new ArrayList();

                for(int i=0;i<50;i++){
                    PrescriptionList.add(new Prescription(Prescription+"  "+i,"天之道,损有余而补不足...",(i%2)+""));
                }

        prescriptionAdapter=new PrescriptionAdapter(getActivity(),PrescriptionList);
        fragment_presciption_listview.setAdapter(prescriptionAdapter);
        fragment_presciption_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), PrescriptionDetailsActivity.class));
            }
        });*/
    }
}
